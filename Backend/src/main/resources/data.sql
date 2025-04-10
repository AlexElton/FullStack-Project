-- Insert test users
INSERT INTO users (username, email, password_hash, first_name, last_name, phone_number, role, account_status, bio, profile_pic_url, created_at, updated_at, last_login_at)
VALUES 
('admin', 'admin@example.com', '$2a$10$mDQtfH0X6juF3MQTPnvIMO7c0jYKL5PiUQJw3g.LYBFvICmZ2zDUu', 'Admin', 'User', '12345678', 'ADMIN', 'ACTIVE', 'Admin user', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user1', 'user1@example.com', '$2a$10$mDQtfH0X6juF3MQTPnvIMO7c0jYKL5PiUQJw3g.LYBFvICmZ2zDUu', 'John', 'Doe', '87654321', 'USER', 'ACTIVE', 'Regular user', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert categories
INSERT INTO categories (name, description, parent_category_id, display_order, icon_url, is_active, created_at, updated_at)
VALUES 
('Electronics', 'Electronic devices and accessories', NULL, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Books', 'Books and literature', NULL, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Clothing', 'Clothing and fashion', NULL, 3, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Smartphones', 'Mobile phones and accessories', 1, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laptops', 'Laptop computers', 1, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Audio', 'Headphones and speakers', 1, 3, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert items
INSERT INTO items (seller_id, category_id, title, brief_description, full_description, price, currency, condition, quantity, allow_offers, accept_vipps, status, views_count, created_at, updated_at)
VALUES 
-- Smartphones
(2, 4, 'iPhone 13 Pro', 'Like new iPhone 13 Pro', 'iPhone 13 Pro in excellent condition with original box and accessories', 899.99, 'NOK', 'LIKE_NEW', 1, true, true, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, 'Samsung Galaxy S21', 'Samsung flagship phone', 'Samsung Galaxy S21 with 128GB storage, barely used', 799.99, 'NOK', 'LIKE_NEW', 1, true, true, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Laptops
(2, 5, 'MacBook Pro M1', '2020 MacBook Pro', 'MacBook Pro with M1 chip, 16GB RAM, 512GB SSD', 1299.99, 'NOK', 'LIKE_NEW', 1, true, true, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 5, 'Dell XPS 15', 'Dell XPS 15 laptop', 'Dell XPS 15 with 11th gen Intel i7, 16GB RAM, 1TB SSD', 1499.99, 'NOK', 'GOOD', 1, true, true, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Audio
(2, 6, 'Sony WH-1000XM4', 'Noise cancelling headphones', 'Sony WH-1000XM4 wireless noise cancelling headphones, black', 349.99, 'NOK', 'NEW', 2, true, true, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert item images
INSERT INTO item_images (item_id, image_url, is_primary, display_order, created_at)
VALUES 
-- iPhone 13 Pro images
(1, 'https://placehold.co/600x400?text=iPhone+13+Pro', true, 1, CURRENT_TIMESTAMP),
(1, 'https://placehold.co/600x400?text=iPhone+13+Pro+2', false, 2, CURRENT_TIMESTAMP),

-- Samsung Galaxy S21 images
(2, 'https://placehold.co/600x400?text=Samsung+Galaxy+S21', true, 1, CURRENT_TIMESTAMP),
(2, 'https://placehold.co/600x400?text=Samsung+Galaxy+S21+2', false, 2, CURRENT_TIMESTAMP),

-- MacBook Pro images
(3, 'https://placehold.co/600x400?text=MacBook+Pro', true, 1, CURRENT_TIMESTAMP),
(3, 'https://placehold.co/600x400?text=MacBook+Pro+2', false, 2, CURRENT_TIMESTAMP),

-- Dell XPS 15 images
(4, 'https://placehold.co/600x400?text=Dell+XPS+15', true, 1, CURRENT_TIMESTAMP),
(4, 'https://placehold.co/600x400?text=Dell+XPS+15+2', false, 2, CURRENT_TIMESTAMP),

-- Sony WH-1000XM4 images
(5, 'https://placehold.co/600x400?text=Sony+Headphones', true, 1, CURRENT_TIMESTAMP),
(5, 'https://placehold.co/600x400?text=Sony+Headphones+2', false, 2, CURRENT_TIMESTAMP); 