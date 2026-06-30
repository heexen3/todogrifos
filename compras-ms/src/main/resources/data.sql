INSERT IGNORE INTO compras (id, numero_factura, fecha_compra, proveedor, total) VALUES (1, 'FAC-2026-0001', NOW(), '76.123.456-K', 150000.0);
INSERT IGNORE INTO compra_detalles (id, compra_id, sku, cantidad, precio_costo, subtotal) VALUES (1, 1, 'GRF-COC-001', 10, 15000.0, 150000.0);
