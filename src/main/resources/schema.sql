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
