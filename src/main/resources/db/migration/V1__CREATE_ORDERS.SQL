CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    order_number INTEGER,
    status_id INTEGER,
    customer_id UUID,
    original_price DECIMAL(8, 2) NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER
);


CREATE TABLE IF NOT EXISTS order_number_sequence (
    id BIGSERIAL PRIMARY KEY,
    current_value INTEGER NOT NULL
);

CREATE INDEX "idx_order_id" ON order_products (order_id);
CREATE INDEX "idx_order_product_id" ON order_products (product_id);