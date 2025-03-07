
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
INSERT INTO products (id,name, category_id, purchase_price, sale_price, stock, min_stock, created_at, updated_at)
VALUES
    ('P000000001','Smartphone', 1, 500.00, 700.00, 50, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('P000000002','Laptop', 1, 800.00, 1200.00, 30, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('P000000003','Camiseta', 2, 10.00, 20.00, 100, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('P000000004','Sofá', 3, 300.00, 500.00, 10, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('P000000005','Pelota de fútbol', 5, 15.00, 30.00, 50, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. Clientes
INSERT INTO customers (name, phone, email, created_at)
VALUES
    ('Juan Pérez', '123456789', 'juan@example.com', CURRENT_TIMESTAMP),
    ('María Gómez', '987654321', 'maria@example.com', CURRENT_TIMESTAMP);

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
INSERT INTO orders (customer_id, campaign_id, live_session_id, apertura, total_amount, status, acumulando, payment_due_date, created_at, updated_at)
VALUES
    (1, 1, 1, 100.00, 750.00, 'NO_PAGADO', TRUE, '2025-03-05 23:59:59', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 9. Items de Pedido (Order Items)
-- Suponiendo que el pedido insertado tenga id=1 y se venden dos productos: Smartphone y Pelota de fútbol.
INSERT INTO order_items (order_id, product_id, product_name, price, quantity, discount)
VALUES
    (1, 'P000000001', 'Smartphone', 700.00, 1, 0.00),
    (1, 'P000000001', 'Pelota de fútbol', 30.00, 1, 10.00);

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
