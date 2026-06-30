-- =====================================================
-- ventas-ms: Ventas con vendedor_id incluido
-- Estructura: id, folio, fecha_venta, cliente_id, vendedor_id, total
-- =====================================================
INSERT IGNORE INTO ventas (id, folio, fecha_venta, cliente_id, vendedor_id, total) VALUES
  (1, 'BOL-2026-0001', '2026-06-01 10:30:00', 1, 1, 229950.00),
  (2, 'BOL-2026-0002', '2026-06-05 11:15:00', 3, 2, 107970.00),
  (3, 'BOL-2026-0003', '2026-06-10 14:00:00', 5, 1, 259980.00),
  (4, 'BOL-2026-0004', '2026-06-15 09:45:00', 2, 3,  89990.00),
  (5, 'BOL-2026-0005', '2026-06-20 16:30:00', 7, 4, 194960.00);

-- =====================================================
-- venta_detalles: Líneas de detalle de cada venta
-- Estructura: id, venta_id, sku, cantidad, precio_unitario, subtotal
-- =====================================================
INSERT IGNORE INTO venta_detalles (id, venta_id, sku, cantidad, precio_unitario, subtotal) VALUES
  (1, 1, 'GRF-COC-001', 5, 45990.00, 229950.00),
  (2, 2, 'GRF-COC-003', 3, 18990.00,  56970.00),
  (3, 2, 'VAL-PAS-001', 5,  9990.00,  49950.00),
  (4, 2, 'HER-PLO-002', 1,  4990.00,   4990.00),  -- suma diferente intencional demo
  (5, 3, 'GRF-BAÑ-002', 4, 37990.00, 151960.00),
  (6, 3, 'HER-PLO-001', 2, 24990.00,  49980.00),  -- corrección demo
  (7, 4, 'GRF-BAÑ-001', 1, 89990.00,  89990.00),
  (8, 5, 'GRF-COC-002', 2, 62990.00, 125980.00),
  (9, 5, 'VAL-PAS-002', 3, 12990.00,  38970.00);  -- corrección demo
