CREATE TABLE tb_resumes (
                            id BIGSERIAL PRIMARY KEY,
                            file_name VARCHAR(255) NOT NULL,
                            extracted_text TEXT NOT NULL,
                            uploaded_at TIMESTAMP NOT NULL,
                            user_id BIGINT NOT NULL,
                            CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE
);