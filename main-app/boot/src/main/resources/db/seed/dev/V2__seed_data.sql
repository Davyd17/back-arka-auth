-- Roles -----------------------------------------------------------------------------------------
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Full system access and user management'),
('GUEST', 'Limited access for temporary users'),
('USER', 'Standard access to platform features');

-- Users ------------------------------------------------------------------------------------------
INSERT INTO users (username, email, password, enabled, created_at, updated_at, role_id) VALUES
('admin_user', 'admin@example.com', 'hashed_password_123', true, NOW(), NOW(), 1),
('jane_smith', 'jane_smith@example.com', 'hashed_password_456', true, NOW(), NOW(), 2),
('bob_johnson', 'bob_johnson@example.com', 'hashed_password_789', true, NOW(), NOW(), 3),
('guest_account', 'guest@example.com','hashed_password_000', false, NOW(), NOW(), 3);