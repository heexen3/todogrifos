INSERT IGNORE INTO ventas (id, folio, fecha_venta, cliente_id, total) VALUES (1, 'BOL-2026-0001', NOW(), 1, 229950.0);
INSERT IGNORE INTO venta_detalles (id, venta_id, sku, cantidad, precio_unitario, subtotal) VALUES (1, 1, 'GRF-COC-001', 5, 45990.0, 229950.0);
