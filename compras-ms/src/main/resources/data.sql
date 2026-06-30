-- =====================================================
-- compras-ms: Órdenes de compra a proveedores
-- Estructura: id, numero_factura, fecha_compra, proveedor (RUT), total
-- =====================================================
INSERT IGNORE INTO compras (id, numero_factura, fecha_compra, proveedor, total) VALUES
  (1, 'FAC-2026-0001', '2026-05-10 09:00:00', '76.123.456-K', 1500000.00),
  (2, 'FAC-2026-0002', '2026-05-20 10:30:00', '77.234.567-1', 850000.00),
  (3, 'FAC-2026-0003', '2026-06-05 11:00:00', '76.890.123-5', 2250000.00);

-- =====================================================
-- compra_detalles: Líneas de cada orden de compra
-- Estructura: id, compra_id, sku, cantidad, precio_costo, subtotal
-- =====================================================
INSERT IGNORE INTO compra_detalles (id, compra_id, sku, cantidad, precio_costo, subtotal) VALUES
  (1, 1, 'GRF-COC-001', 50, 22000.00, 1100000.00),
  (2, 1, 'GRF-COC-003', 20,  8000.00,  160000.00),
  (3, 1, 'HER-PLO-002', 48,  5000.00,  240000.00),
  (4, 2, 'VAL-PAS-001', 80,  4500.00,  360000.00),
  (5, 2, 'VAL-PAS-002', 50,  5500.00,  275000.00),
  (6, 2, 'HER-PLO-001', 43,  5000.00,  215000.00),
  (7, 3, 'GRF-BAÑ-001', 15, 45000.00,  675000.00),
  (8, 3, 'GRF-BAÑ-002', 30, 18000.00,  540000.00),
  (9, 3, 'CAL-GAS-001', 12, 60000.00,  720000.00),
  (10,3, 'CAL-GAS-002',  5, 85000.00,  425000.00);  -- ajuste demo
