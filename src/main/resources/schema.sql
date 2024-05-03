CREATE TABLE IF NOT EXISTS product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(150),
    is_active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    price NUMERIC NOT NULL,
    quantity INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES product_category (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    dni VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    disabled_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_product (
    id SERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);
