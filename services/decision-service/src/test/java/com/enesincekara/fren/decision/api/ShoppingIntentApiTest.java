package com.enesincekara.fren.decision.api;

import com.enesincekara.fren.decision.support.PostgresTestConfig;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestConfig.class)
class ShoppingIntentApiTest {

    private static final String BASE_URL =
            "/api/v1/shopping-intents";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createsIntentAndRetrievesItById() throws Exception {
        String requestBody = """
                {
                  "productName": "Kablosuz kulaklık",
                  "amount": 3499.90,
                  "currency": "TRY",
                  "reason": "Mevcut kulaklığımın pili çabuk bitiyor",
                  "cooldownHours": 72
                }
                """;

        MvcResult createResult = mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(
                        jsonPath("$.productName")
                                .value("Kablosuz kulaklık")
                )
                .andExpect(jsonPath("$.currency").value("TRY"))
                .andExpect(jsonPath("$.cooldownHours").value(72))
                .andReturn();

        String responseBody = createResult
                .getResponse()
                .getContentAsString();

        String id = JsonPath.read(responseBody, "$.id");

        Integer rowCount = jdbcTemplate.queryForObject(
                """
                        select count(*) from shopping_intents where id = ?
                        """, Integer.class, UUID.fromString(id)
        );

        assertEquals(Integer.valueOf(1), rowCount);

        String location = createResult
                .getResponse()
                .getHeader("Location");

        assertNotNull(location);

        assertEquals(
                BASE_URL + "/" + id,
                location
        );

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(
                        jsonPath("$.productName")
                                .value("Kablosuz kulaklık")
                )
                .andExpect(jsonPath("$.amount").value(3499.90))
                .andExpect(jsonPath("$.currency").value("TRY"))
                .andExpect(jsonPath("$.cooldownHours").value(72));
    }

    @Test
    void returns404WhenIntentDoesNotExist() throws Exception {
        UUID unknownId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/{id}", unknownId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsUnsupportedCooldown() throws Exception {
        String requestBody = """
                {
                  "productName": "Kulaklık",
                  "amount": 1500.00,
                  "currency": "TRY",
                  "reason": "Yeni kulaklık istiyorum",
                  "cooldownHours": 48
                }
                """;

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.title")
                                .value("Invalid shopping intent")
                );
    }

    @Test
    void rejectsBlankProductName() throws Exception {
        String requestBody = """
                {
                  "productName": " ",
                  "amount": 1500.00,
                  "currency": "TRY",
                  "reason": "Yeni kulaklık istiyorum",
                  "cooldownHours": 24
                }
                """;

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsInvalidCurrency() throws Exception {
        String requestBody = """
                {
                  "productName": "Kitap",
                  "amount": 250.00,
                  "currency": "INVALID",
                  "reason": "Okumak istiyorum",
                  "cooldownHours": 24
                }
                """;

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.title")
                                .value("Invalid shopping intent")
                );
    }

    @Test
    void usesDedicatedTestDatabase()throws Exception {
        String databaseName = jdbcTemplate.queryForObject(
                "select current_database()",String.class
        );
        assertEquals("fren_test", databaseName);
    }
}