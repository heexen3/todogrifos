-- =====================================================
-- vendedores-ms: Equipo de vendedores de TodoGrifos
-- Estructura: id, codigo_interno, rut, nombre, sucursal, porcentaje_comision, comision_acumulada, fecha_ingreso
-- =====================================================
INSERT IGNORE INTO vendedores (id, codigo_interno, rut, nombre, sucursal, porcentaje_comision, comision_acumulada, fecha_ingreso) VALUES
  (1, 'VEND-001', '13.456.789-0', 'Juan Pérez Salinas',      'Santiago Centro',  5.0, 184956.0, '2024-03-01 08:00:00'),
  (2, 'VEND-002', '16.234.567-8', 'Francisca Lagos Muñoz',   'Ñuñoa',            4.5, 98450.0,  '2024-07-15 08:00:00'),
  (3, 'VEND-003', '14.789.012-6', 'Rodrigo Cárdenas Bravo',  'Maipú',            5.5, 231200.0, '2023-11-20 08:00:00'),
  (4, 'VEND-004', '18.345.678-1', 'Carolina Tapia Vera',     'Providencia',      4.0, 67800.0,  '2025-01-10 08:00:00');
