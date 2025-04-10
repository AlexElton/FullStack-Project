-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    profile_pic_url VARCHAR(255),
    bio TEXT,
    role VARCHAR(255) NOT NULL,
    account_status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    last_login_at TIMESTAMP
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_category_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);

-- Create items table
CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    brief_description TEXT NOT NULL,
    full_description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    condition VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    allow_offers BOOLEAN NOT NULL,
    accept_vipps BOOLEAN NOT NULL,
    status VARCHAR(255) NOT NULL,
    views_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Create item_images table
CREATE TABLE IF NOT EXISTS item_images (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN NOT NULL,
    display_order INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items(item_id)
); 