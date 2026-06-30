-- =====================================================
-- inventario-ms: Stock inicial para cada producto
-- Estructura: id, sku, cantidad, stock_minimo
-- =====================================================
INSERT IGNORE INTO inventarios (id, sku, cantidad, stock_minimo) VALUES
  (1,  'GRF-COC-001', 85,  10),
  (2,  'GRF-COC-002', 40,   5),
  (3,  'GRF-COC-003', 200, 20),
  (4,  'GRF-BAÑ-001', 25,   5),
  (5,  'GRF-BAÑ-002', 60,  10),
  (6,  'GRF-BAÑ-003', 35,   5),
  (7,  'VAL-PAS-001', 300, 30),
  (8,  'VAL-PAS-002', 150, 20),
  (9,  'CAL-GAS-001', 18,   3),
  (10, 'CAL-GAS-002', 12,   3),
  (11, 'HER-PLO-001', 50,   8),
  (12, 'HER-PLO-002', 500, 50);
