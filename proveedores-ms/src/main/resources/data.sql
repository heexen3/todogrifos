-- =====================================================
-- proveedores-ms: Proveedores del rubro grifería/ferretería
-- Estructura: id, rut, razon_social, email, telefono, direccion, fecha_registro
-- =====================================================
INSERT IGNORE INTO proveedores (id, rut, razon_social, email, telefono, direccion, fecha_registro) VALUES
  (1, '76.123.456-K', 'Distribuidora Grifos del Sur SpA',   'ventas@grifosdelsur.cl',    '+56223456789', 'Av. Lo Espejo 100, San Miguel',         '2023-01-15 09:00:00'),
  (2, '77.234.567-1', 'Nibsa S.A.',                         'pedidos@nibsa.cl',           '+56222345678', 'Av. Vicuña Mackenna 8000, La Florida',  '2023-03-20 09:00:00'),
  (3, '76.890.123-5', 'Importadora Grohe Chile Ltda.',      'comercial@grohe.cl',         '+56229876543', 'Av. Isidora Goyenechea 3000, Las Condes','2023-06-10 09:00:00'),
  (4, '76.456.789-2', 'Ferretería Industrial Tarapacá SA',  'administracion@fitsa.cl',    '+56225678901', 'Calle San Pablo 3300, Pudahuel',         '2024-01-08 09:00:00');
