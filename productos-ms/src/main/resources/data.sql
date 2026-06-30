INSERT IGNORE INTO marcas (id, nombre, pais_origen) VALUES (1, 'Nibsa', 'Chile');
INSERT IGNORE INTO categorias (id, nombre, descripcion) VALUES (1, 'Grifería de Cocina', 'Llaves y monomandos para lavaplatos');
INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio, activo, garantia_meses, marca_id, categoria_id) VALUES (1, 'GRF-COC-001', 'Grifo de Cocina Monomando Cromado', 'Grifo monomando extensible con acabado brillante', 45990.00, true, 12, 1, 1);
