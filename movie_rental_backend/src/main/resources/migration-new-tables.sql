-- Migration script for movie-rent: Cart, Order, and OrderItem tables
-- Run this against the sakila database once.

CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id SMALLINT UNSIGNED NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cart_item (
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    film_id SMALLINT UNSIGNED NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES film(film_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS rental_order (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id SMALLINT UNSIGNED NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_item (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    film_id SMALLINT UNSIGNED NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(5,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES rental_order(order_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES film(film_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET @exists = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'sakila' AND TABLE_NAME = 'rental' AND COLUMN_NAME = 'order_id');
SET @sql = IF(@exists = 0,
    'ALTER TABLE rental ADD COLUMN order_id BIGINT NULL',
    'SELECT ''column order_id already exists'' AS status');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = 'sakila' AND TABLE_NAME = 'rental' AND CONSTRAINT_NAME = 'fk_rental_order');
SET @fk_sql = IF(@fk_exists = 0,
    'ALTER TABLE rental ADD CONSTRAINT fk_rental_order FOREIGN KEY (order_id) REFERENCES rental_order(order_id)',
    'SELECT ''fk_rental_order already exists'' AS status');
PREPARE fk_stmt FROM @fk_sql;
EXECUTE fk_stmt;
DEALLOCATE PREPARE fk_stmt;

-- Unique constraint to prevent duplicate film entries in cart
SET @uq_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = 'sakila' AND TABLE_NAME = 'cart_item' AND CONSTRAINT_NAME = 'uq_cart_item_film');
SET @uq_sql = IF(@uq_exists = 0,
    'ALTER TABLE cart_item ADD CONSTRAINT uq_cart_item_film UNIQUE (cart_id, film_id)',
    'SELECT ''uq_cart_item_film already exists'' AS status');
PREPARE uq_stmt FROM @uq_sql;
EXECUTE uq_stmt;
DEALLOCATE PREPARE uq_stmt;
