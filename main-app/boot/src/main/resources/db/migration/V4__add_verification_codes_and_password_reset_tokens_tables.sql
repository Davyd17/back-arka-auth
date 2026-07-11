CREATE TABLE verification_codes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL UNIQUE,
        CONSTRAINT fk_verification_codes_users
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)

);

CREATE INDEX idx_verification_code_user_id ON verification_codes(user_id);

CREATE TABLE password_reset_tokens (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_password_reset_tokens_users
        FOREIGN KEY (user_id)
            REFERENCES users(id)

);

CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);
