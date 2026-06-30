-- =====================================================
-- clientes-ms: Clientes personas naturales chilenas
-- Estructura: rut, nombres, ap_paterno, ap_materno, email, telefono, direccion
-- =====================================================
INSERT IGNORE INTO clientes (id, rut, nombres, ap_paterno, ap_materno, email, telefono, direccion) VALUES
  (1, '12.345.678-9', 'Carlos Andrés',  'Fuentes',    'Morales',   'cfuentes@gmail.com',        '+56912345678', 'Av. Vicuña Mackenna 4500, Macul'),
  (2, '15.678.901-2', 'María José',     'González',   'Ramos',     'mgonzalez@hotmail.com',     '+56923456789', 'Calle Apoquindo 3500, Las Condes'),
  (3, '11.223.344-5', 'Pedro Antonio',  'Soto',       'Valdivia',  'psoto@empresa.cl',          '+56934567890', 'Pasaje Los Aromos 210, La Florida'),
  (4, '16.789.012-3', 'Ana Lucía',      'Martínez',   'Herrera',   'amartinez@outlook.com',     '+56945678901', 'Av. Gran Avenida 1200, La Granja'),
  (5, '13.456.789-0', 'Roberto',        'Jiménez',    'Castro',    'rjimenez@ferreteria.cl',    '+56956789012', 'Calle Maestranza 85, Santiago Centro'),
  (6, '17.890.123-4', 'Claudia Paola',  'Rojas',      'Pizarro',   'crojas@gmail.com',          '+56967890123', 'Av. Recoleta 2300, Recoleta'),
  (7, '14.567.890-1', 'Marcelo',        'Álvarez',    'Contreras', 'malvarez@constructora.cl',  '+56978901234', 'Calle O''Higgins 455, Rancagua'),
  (8, '10.111.222-3', 'Valentina',      'Espinoza',   'Núñez',     'vespinoza@personal.cl',     '+56989012345', 'Av. Independencia 780, Independencia');
