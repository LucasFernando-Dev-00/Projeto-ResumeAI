CREATE TABLE tb_analyses (
                             id BIGSERIAL PRIMARY KEY,
                             insights TEXT NOT NULL,
                             analyzed_at TIMESTAMP NOT NULL,
                             resume_id BIGINT NOT NULL UNIQUE,
                             CONSTRAINT fk_analyses_resume FOREIGN KEY (resume_id) REFERENCES tb_resumes (id) ON DELETE CASCADE
);