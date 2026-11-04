package com.enesincekara.fren.decision;

import com.enesincekara.fren.decision.support.PostgresTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestConfig.class)
class DecisionServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
