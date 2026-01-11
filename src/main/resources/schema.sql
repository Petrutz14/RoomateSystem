DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS apartment_members;
DROP TABLE IF EXISTS apartments;
DROP TABLE IF EXISTS users;


-- -----------------------------
-- Table: users
-- -----------------------------
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------
-- Table: apartments
-- -----------------------------
CREATE TABLE apartments (
                            id SERIAL PRIMARY KEY,
                            address VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------
-- Join Table: apartment_members (Links multiple users to one apartment)
-- -----------------------------
CREATE TABLE apartment_members (
                                   apartment_id INT REFERENCES apartments(id) ON DELETE CASCADE,
                                   user_id INT REFERENCES users(id) ON DELETE CASCADE,
                                   PRIMARY KEY (apartment_id, user_id)
);

-- -----------------------------
-- Table: expenses (Linked to Apartment instead of Category)
-- -----------------------------
CREATE TABLE expenses (
                          id SERIAL PRIMARY KEY,
                          user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          apartment_id INT NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
                          title VARCHAR(100) NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
                          expense_date DATE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
                          id SERIAL PRIMARY KEY,
                          expense_id INTEGER REFERENCES expenses(id) ON DELETE CASCADE,
                          user_id INTEGER REFERENCES users(id),
                          amount DECIMAL(19, 2) NOT NULL,
                          payment_date DATE DEFAULT CURRENT_DATE
);