-- =====================================================
-- auth-ms: Usuarios del sistema
-- Contraseñas: admin123 ($2a$10$dDO...) y oper123 ($2a$10$xNs...)
-- =====================================================
INSERT IGNORE INTO usuarios (id, username, password, role) VALUES
  (1, 'admin',    '$2a$10$dDOAVQhEdZMo8si4YPODee0PIpJ4A1BrFqaZU7H6vUPwyQDikn8Tq', 'ROLE_ADMIN'),
  (2, 'operador', '$2a$10$dDOAVQhEdZMo8si4YPODee0PIpJ4A1BrFqaZU7H6vUPwyQDikn8Tq', 'ROLE_USER');
