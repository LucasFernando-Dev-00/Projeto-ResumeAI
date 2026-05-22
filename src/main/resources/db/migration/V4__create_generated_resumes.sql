CREATE TABLE tb_generated_resumes (
                                      id BIGSERIAL PRIMARY KEY,
                                      optimized_text TEXT NOT NULL,
                                      pdf_url VARCHAR(255),
                                      generated_at TIMESTAMP NOT NULL,
                                      user_id BIGINT NOT NULL,
                                      CONSTRAINT fk_generated_resumes_user FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE
);