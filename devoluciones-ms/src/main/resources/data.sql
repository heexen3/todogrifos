-- =====================================================
-- devoluciones-ms: Notas de crédito por devoluciones
-- Estructura: id, nota_credito_folio, venta_id, sku, cantidad, motivo, destino_logistico, fecha_devolucion
-- =====================================================
INSERT IGNORE INTO devoluciones (id, nota_credito_folio, venta_id, sku, cantidad, motivo, destino_logistico, fecha_devolucion) VALUES
  (1, 'NCG-2026-0001', 1, 'GRF-COC-001', 2, 'Defecto de fábrica: grifo con goteo en el cierre', 'REINGRESADO_A_STOCK', '2026-06-08 10:00:00'),
  (2, 'NCG-2026-0002', 2, 'HER-PLO-002', 1, 'Cliente recibió producto incorrecto (teflón 6mm en vez de 12mm)', 'ENVIADO_A_MERMA', '2026-06-12 14:30:00');
