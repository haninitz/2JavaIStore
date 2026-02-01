-- iStore database schema (MySQL)
CREATE DATABASE IF NOT EXISTS istore_db;
USE istore_db;

DROP TABLE IF EXISTS store_employees;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS stores;
DROP TABLE IF EXISTS email_whitelist;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(120) NOT NULL UNIQUE,
  pseudo VARCHAR(80),
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE'
);

CREATE TABLE email_whitelist (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE stores (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  price DECIMAL(10,2) NOT NULL CHECK (price >= 0)
);

-- Stock per store per item (cannot be < 0)
CREATE TABLE inventory (
  store_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
  PRIMARY KEY (store_id, item_id),
  FOREIGN KEY (store_id) REFERENCES stores(id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

-- Which employees can access which stores
CREATE TABLE store_employees (
  store_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (store_id, user_id),
  FOREIGN KEY (store_id) REFERENCES stores(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Example whitelist (for quick tests)
INSERT INTO email_whitelist(email) VALUES ('admin@istore.com'), ('emp@istore.com');
