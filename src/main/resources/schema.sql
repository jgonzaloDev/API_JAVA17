-- Desactivar temporalmente la integridad referencial para evitar errores en el orden de eliminación
SET REFERENTIAL_INTEGRITY FALSE;

-- Eliminar tablas existentes en el orden correcto (de tablas hijas a tablas maestras)
DROP TABLE IF EXISTS transaction_summary;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS campaigns;
DROP TABLE IF EXISTS live_sessions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Reactivar la integridad referencial
SET REFERENTIAL_INTEGRITY TRUE;

---------------------------------------------------------
-- Tabla de Roles
---------------------------------------------------------
CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

---------------------------------------------------------
-- Tabla de Usuarios
---------------------------------------------------------
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role_id    BIGINT       NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);



---------------------------------------------------------
-- Tabla de Categorías
---------------------------------------------------------
CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT         NULL
);

---------------------------------------------------------
-- Tabla de Productos
---------------------------------------------------------
CREATE TABLE products
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                   VARCHAR(255)   NOT NULL,
    category_id            BIGINT         NOT NULL,
    purchase_price         DECIMAL(10, 2) NOT NULL,
    sale_price             DECIMAL(10, 2) NOT NULL CHECK (sale_price >= purchase_price),
    stock                  INT            NOT NULL,
    min_stock              INT            NOT NULL,
    description            TEXT           NULL,
    image_url              VARCHAR(255)   NULL,
    brand                  VARCHAR(255)   NULL,
    model                  VARCHAR(255)   NULL,
    status                 VARCHAR(20)    NULL,
    unit                   VARCHAR(20)    NULL,
    barcode                VARCHAR(255)   NULL,
    specification_material VARCHAR(100)   NULL,
    specification_capacity VARCHAR(20)    NULL,
    specification_color    VARCHAR(10)    null,
    sku                    VARCHAR(255)   NULL,
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    discount               DECIMAL(5, 2)  NOT NULL DEFAULT 0.00,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);



---------------------------------------------------------
-- Tabla de Clientes
---------------------------------------------------------
CREATE TABLE customers
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id         VARCHAR(25)                         NULL,
    name                VARCHAR(255)                        NULL,
    phone               VARCHAR(20)                         NULL,
    email               VARCHAR(100)                        NULL,
    created_at          TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nick_tiktok         VARCHAR(255) UNIQUE                 NOT NULL,
    initial_deposit     DECIMAL(10, 2) DEFAULT 10.00        NOT NULL,
    shipping_preference VARCHAR(10)    DEFAULT 'ACCUMULATE' NOT NULL,
    remaining_deposit   DECIMAL(10, 2) DEFAULT 0


);

---------------------------------------------------------
-- Tabla de Sesiones en Vivo
---------------------------------------------------------
CREATE TABLE live_sessions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    platform   VARCHAR(50)  NOT NULL DEFAULT 'TikTok',
    start_time TIMESTAMP    NOT NULL,
    end_time   TIMESTAMP    NOT NULL CHECK (end_time > start_time),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

---------------------------------------------------------
-- Tabla de Campañas
---------------------------------------------------------
CREATE TABLE campaigns
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NULL,
    start_date  TIMESTAMP    NOT NULL,
    end_date    TIMESTAMP    NOT NULL CHECK (end_date > start_date)
);

---------------------------------------------------------
-- Tabla de Pedidos (Orders)
---------------------------------------------------------
CREATE TABLE orders
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id      BIGINT         NOT NULL,
    campaign_id      BIGINT         NULL,
    live_session_id  BIGINT         NULL,

    --  Cambiado: ahora permite nulos (opcional)
    aperture         DECIMAL(10, 2) NULL,

    total_amount     DECIMAL(10, 2) NOT NULL,
    real_amount_to_pay  DECIMAL(10, 2) NOT NULL DEFAULT 0.00, -- 👈 NUEVO CAMPO
    status           VARCHAR(20)    NOT NULL CHECK (status IN ('PAGADO', 'NO_PAGADO')),
    accumulation     BOOLEAN        NOT NULL DEFAULT FALSE,
    payment_due_date TIMESTAMP      NULL,
    dias_sin_pagar   INT            NOT NULL DEFAULT 0,

    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    FOREIGN KEY (campaign_id) REFERENCES campaigns (id) ON DELETE SET NULL,
    FOREIGN KEY (live_session_id) REFERENCES live_sessions (id) ON DELETE SET NULL
);

---------------------------------------------------------
-- Tabla de Items de Pedido (order_items)
---------------------------------------------------------
CREATE TABLE order_items
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id     BIGINT         NOT NULL,
    product_id   BIGINT         NOT NULL,
    product_name VARCHAR(255)   NOT NULL,
    price        DECIMAL(10, 2) NOT NULL,
    quantity     INT            NOT NULL CHECK (quantity > 0),
    discount     DECIMAL(5, 2)  NOT NULL DEFAULT 0.00 CHECK (discount >= 0),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

---------------------------------------------------------
-- Tabla de Pagos
---------------------------------------------------------
CREATE TABLE payments
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT         NOT NULL,
    amount         DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    payment_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50)    NOT NULL DEFAULT 'Efectivo',
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

---------------------------------------------------------
-- Tabla de Resumen de Transacciones (Opcional para reportes)
---------------------------------------------------------
CREATE TABLE transaction_summary
(
    customer_id       BIGINT PRIMARY KEY,
    customer_name     VARCHAR(255)   NOT NULL,
    total_apertura    DECIMAL(10, 2) NOT NULL,
    total_pagado      DECIMAL(10, 2) NOT NULL,
    saldo_pendiente   DECIMAL(10, 2) NOT NULL CHECK (saldo_pendiente >= 0),
    esta_pagado       BOOLEAN        NOT NULL,
    last_payment_date TIMESTAMP      NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE
);

---------------------------------------------------------
-- ÍNDICES PARA MEJORAR CONSULTAS
---------------------------------------------------------
CREATE INDEX idx_orders_customer ON orders (customer_id);
CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_payments_order ON payments (order_id);
CREATE INDEX idx_products_stock ON products (stock);
CREATE INDEX idx_products_category ON products (category_id);
