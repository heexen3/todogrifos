-- =====================================================
-- despachos-ms: Despachos de ventas a domicilio
-- Estructura: id, codigo_seguimiento, venta_id, direccion_envio, comuna, estado, fecha_creacion, fecha_entrega_estimada
-- =====================================================
INSERT IGNORE INTO despachos (id, codigo_seguimiento, venta_id, direccion_envio, comuna, estado, fecha_creacion, fecha_entrega_estimada) VALUES
  (1, 'TRK-2026-0001', 1, 'Av. Vicuña Mackenna 4500',  'Macul',            'ENTREGADO', '2026-06-01 12:00:00', '2026-06-03 18:00:00'),
  (2, 'TRK-2026-0002', 2, 'Pasaje Los Aromos 210',     'La Florida',       'ENTREGADO', '2026-06-05 13:00:00', '2026-06-07 18:00:00'),
  (3, 'TRK-2026-0003', 3, 'Calle Maestranza 85',       'Santiago Centro',  'EN_RUTA', '2026-06-10 15:00:00', '2026-06-25 18:00:00'),
  (4, 'TRK-2026-0004', 4, 'Av. Gran Avenida 1200',     'La Granja',        'PENDIENTE', '2026-06-15 10:00:00', '2026-06-28 18:00:00');
