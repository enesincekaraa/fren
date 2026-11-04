CREATE TABLE shopping_intents (
                                  id UUID PRIMARY KEY,

                                  product_name TEXT NOT NULL,
                                  amount NUMERIC NOT NULL,
                                  currency VARCHAR(3) NOT NULL,
                                  reason TEXT NOT NULL,

                                  cooldown_hours INTEGER NOT NULL,

                                  created_at TIMESTAMPTZ NOT NULL,
                                  review_available_at TIMESTAMPTZ NOT NULL,

                                  decision VARCHAR(32),

                                  CONSTRAINT ck_shopping_intents_product_name_not_blank
                                      CHECK (length(trim(product_name)) > 0),

                                  CONSTRAINT ck_shopping_intents_amount_positive
                                      CHECK (amount > 0),

                                  CONSTRAINT ck_shopping_intents_reason_not_blank
                                      CHECK (length(trim(reason)) > 0),

                                  CONSTRAINT ck_shopping_intents_cooldown_valid
                                      CHECK (cooldown_hours IN (24, 72)),

                                  CONSTRAINT ck_shopping_intents_review_time_valid
                                      CHECK (review_available_at > created_at),

                                  CONSTRAINT ck_shopping_intents_decision_valid
                                      CHECK (
                                          decision IS NULL
                                              OR decision IN (
                                                              'PURCHASED',
                                                              'SKIPPED',
                                                              'USED_OWNED_ITEM'
                                              )
                                          )
);