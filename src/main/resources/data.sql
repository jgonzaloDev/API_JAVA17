
-- Nota: Si usas schema.sql para crear las tablas, asegúrate de que se ejecute primero.

-- =============================================
-- Inserción de Datos
-- =============================================

-- 1. Roles (Usuarios)
INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('WORKER');

-- 2. Usuarios (internos)
-- La contraseña aquí es un ejemplo, idealmente debe estar encriptada (BCrypt)
INSERT INTO users (username, password, role_id, created_at)
VALUES
    ('admin', 'admin123', 1, CURRENT_TIMESTAMP),
    ('employee', 'worker123', 2, CURRENT_TIMESTAMP);

-- 3. Categorías de Productos
INSERT INTO categories (name, description) VALUES
                                               ('Electrónica', 'Productos electrónicos y gadgets'),
                                               ('Ropa', 'Prendas para hombre y mujer'),
                                               ('Hogar', 'Artículos para el hogar'),
                                               ('Juguetes', 'Juguetes y juegos'),
                                               ('Deportes', 'Artículos deportivos');

-- 4. Productos
-- Se asume que los IDs de categorías se asignan en orden: 1=Electrónica, 2=Ropa, etc.


INSERT INTO products (name, category_id, purchase_price, sale_price, stock, min_stock, description, image_url, brand, model, status, unit, barcode,specification_material,specification_capacity, specification_color, sku, created_at, updated_at, discount)
VALUES
    ('Smartphone', 1, 500.00, 700.00, 20, 2, 'Smartphone Android', 'https://example.com/smartphone.jpg', 'BrandX', 'ModelY', 'activo', 'unidad', '9876543210', 'aluminio','2L','Azul','SMRT-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00),
    ('Pelota de fútbol', 4, 15.00, 30.00, 100, 10, 'Pelota de fútbol de alta calidad', 'https://example.com/pelota.jpg', 'BrandY', 'ModelZ', 'activo', 'unidad', '1122334455','plástico','1L','Rojo', 'PEL-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10.00),
    ('Pelota de playa', 4, 15.00, 30.00, 100, 10, 'Pelota de fútbol de baja calidad', 'https://example.com/pelota.jpg', 'BrandY', 'ModelZ', 'activo', 'unidad', '1122334455','aluminio','2L','Rosado', 'PEL-001',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10.00);



-- 5. Clientes

INSERT INTO customers (document_id,name, phone, email, nick_tiktok, initial_deposit, created_at)
VALUES
    ('70315256','Ana García', '5551234567', 'ana.garcia@example.com', 'anagarcia', 20.00, '2024-10-27 10:30:00'),
    ('80204745','Carlos Pérez', '5559876543', 'carlos.perez@example.com', 'carlosperez', 10.00, '2024-10-27 10:30:00'),
    ('47452589','Laura Rodríguez', '5551122334', 'laura.rodriguez@example.com', 'laurarodriguez', 15.00, '2024-10-27 10:30:00');

-- 6. Sesiones en Vivo
INSERT INTO live_sessions (title, platform, start_time, end_time, created_at)
VALUES
    ('Live de Ofertas', 'TikTok', '2025-03-01 18:00:00', '2025-03-01 20:00:00', CURRENT_TIMESTAMP),
    ('Descuentos Flash', 'Instagram', '2025-03-02 19:00:00', '2025-03-02 21:00:00', CURRENT_TIMESTAMP);

-- 7. Campañas
INSERT INTO campaigns (name, description, start_date, end_date)
VALUES
    ('Black Friday', 'Descuentos en tecnología', '2025-11-20 00:00:00', '2025-11-30 23:59:59');

-- 8. Pedidos (Orders)
-- Supongamos que el cliente con id=1 (Juan Pérez) realiza un pedido
INSERT INTO orders (customer_id, campaign_id, live_session_id, aperture, total_amount,real_amount_to_pay, status, accumulation, payment_due_date, dias_sin_pagar, created_at, updated_at)
VALUES
    (1, 1, 1, 100.00, 750.00, 200.00,'NO_PAGADO', TRUE, '2025-03-05 23:59:59', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- 9. Items de Pedido (Order Items)
-- Suponiendo que el pedido insertado tenga id=1 y se venden dos productos: Smartphone y Pelota de fútbol.


INSERT INTO order_items (order_id, product_id, product_name, price, quantity, discount)
VALUES
    (1, 2, 'Smartphone', 700.00, 1, 0.00),
    (1, 3, 'Pelota de fútbol', 30.00, 1, 10.00);



-- 10. Pagos
-- Se registra un pago parcial para el pedido 1.
INSERT INTO payments (order_id, amount, payment_date, payment_method)
VALUES
    (1, 100.00, CURRENT_TIMESTAMP, 'Tarjeta de Crédito');

-- 11. Resumen de Transacciones (opcional, para reportes)
-- Se calcula un resumen para el cliente con id=1.
INSERT INTO transaction_summary (customer_id, customer_name, total_apertura, total_pagado, saldo_pendiente, esta_pagado, last_payment_date)
VALUES
    (1, 'Juan Pérez', 100.00, 100.00, 650.00, FALSE, CURRENT_TIMESTAMP);
