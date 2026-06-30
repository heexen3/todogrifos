-- =====================================================
-- productos-ms: Marcas, Categorías y Productos de grifería/ferretería
-- =====================================================

-- MARCAS
INSERT IGNORE INTO marcas (id, nombre, pais_origen) VALUES
  (1, 'Nibsa',       'Chile'),
  (2, 'Grohe',       'Alemania'),
  (3, 'FV',          'Argentina'),
  (4, 'Corona',      'Colombia');

-- CATEGORÍAS
INSERT IGNORE INTO categorias (id, nombre, descripcion) VALUES
  (1, 'Grifería de Cocina',    'Llaves y monomandos para lavaplatos y piletas de cocina'),
  (2, 'Grifería de Baño',      'Mezcladores, duchas y llaves para lavamanos y tinas'),
  (3, 'Válvulas y Llaves',     'Válvulas de corte, llaves de paso y accesorios de control de flujo'),
  (4, 'Calefont y Termos',     'Equipos de calentamiento de agua a gas y eléctricos'),
  (5, 'Herramientas y Fixings','Herramientas de plomería, teflón, sellantes y accesorios de instalación');

-- PRODUCTOS (12 artículos variados del rubro)
INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio, activo, garantia_meses, marca_id, categoria_id) VALUES
  (1,  'GRF-COC-001', 'Grifo Cocina Monomando Cromado',      'Grifo extensible monomando acabado brillante',          45990.00, true,  12, 1, 1),
  (2,  'GRF-COC-002', 'Grifo Cocina Cuello de Ganso',        'Grifo de cuello alto, ideal para ollas grandes',        62990.00, true,  24, 2, 1),
  (3,  'GRF-COC-003', 'Llave de Cocina 1/2 Pulgada',         'Llave de paso tradicional 1/2", latón cromado',         18990.00, true,  12, 1, 1),
  (4,  'GRF-BAÑ-001', 'Mezclador de Ducha Termostático',     'Mezclador con control de temperatura incorporado',      89990.00, true,  36, 2, 2),
  (5,  'GRF-BAÑ-002', 'Grifo Lavamanos Monocomando',         'Monomando para lavamanos con vaciador automático',      37990.00, true,  24, 3, 2),
  (6,  'GRF-BAÑ-003', 'Kit Ducha Efecto Lluvia',             'Kit completo con ducha de 25cm y brazo articulado',     54990.00, true,  12, 4, 2),
  (7,  'VAL-PAS-001', 'Válvula de Bola 3/4 Pulgada',         'Válvula esférica de corte total, latón niquelado',       9990.00, true,  24, 1, 3),
  (8,  'VAL-PAS-002', 'Válvula Check 1/2 Pulgada',           'Válvula de retención para instalaciones a presión',     12990.00, true,  24, 1, 3),
  (9,  'CAL-GAS-001', 'Calefont 6 Litros Gas Natural',       'Calefont mural instantáneo, encendido electrónico',    129990.00, true,  24, 1, 4),
  (10, 'CAL-GAS-002', 'Calefont 13 Litros Gas Licuado',      'Calefont alto rendimiento para baños y cocinas',       189990.00, true,  24, 3, 4),
  (11, 'HER-PLO-001', 'Juego Llaves Ajustables Fontanero',   'Set 3 llaves stilson 10", 14" y 18"',                   24990.00, true,   6, 1, 5),
  (12, 'HER-PLO-002', 'Teflón Industrial Rollos x10',        'Pack 10 rollos teflón 12mm x 10m certificado',          4990.00, true,   0, 1, 5);
