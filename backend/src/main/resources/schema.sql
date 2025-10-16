CREATE TABLE IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    app_user_role VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS short_url (
                    code VARCHAR(20) PRIMARY KEY,
                    long_url VARCHAR(2048) NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    click_count BIGINT DEFAULT 0,
                    user_id BIGINT,
                    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(id) ON DELETE SET NULL
);
