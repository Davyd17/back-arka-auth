-- ========================================================
-- AUTH SERVICE TEST SEED DATA
-- ========================================================

-- 1. Roles
INSERT INTO roles (name, description)
VALUES
    ('ADMIN', 'Administrator with full access'),
    ('USER', 'Standard user with limited access');

-- 2. Users
-- admin@arka.com      -> password: Admin1234  | ADMIN role | verified | enabled
-- user@arka.com       -> password: User1234   | USER role  | verified | enabled
-- unverified@arka.com -> password: User1234   | USER role  | NOT verified | enabled
-- disabled@arka.com   -> password: User1234   | USER role  | verified | NOT enabled

INSERT INTO users (username, email, password, is_enabled, is_verified, created_at, updated_at, role_id)
VALUES
    ('admin',
     'admin@arka.com',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     TRUE, TRUE, NOW(), NOW(), 1),

    ('user',
     'user@arka.com',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     TRUE, TRUE, NOW(), NOW(), 2),

    ('unverified',
     'unverified@arka.com',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     TRUE, FALSE, NOW(), NOW(), 2),

    ('disabled',
     'disabled@arka.com',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     FALSE, TRUE, NOW(), NOW(), 2);

-- 3. Verification codes
-- Active code for unverified user (user_id = 3)
-- Expired code for user (user_id = 2)
INSERT INTO verification_codes (code, expires_at, is_used, user_id)
VALUES
    ('123456',
     NOW() + INTERVAL '15 minutes',
     FALSE, 3),

    ('654321',
     NOW() - INTERVAL '1 hour',
     FALSE, 2);

-- 4. Password reset tokens
-- Active token for user (user_id = 2)
-- Expired token for admin (user_id = 1)
-- Already used token for user (user_id = 3) -- reusing unverified user
INSERT INTO password_reset_tokens (token, expires_at, is_used, user_id)
VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890',
     NOW() + INTERVAL '10 minutes',
     FALSE, 2),

    ('expired-token-0000-0000-000000000000',
     NOW() - INTERVAL '1 hour',
     FALSE, 1);
