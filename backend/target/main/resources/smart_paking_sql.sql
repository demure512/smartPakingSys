CREATE TABLE roles (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(50) NOT NULL,
                       permissions TEXT
);

CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100),
                       email VARCHAR(100),
                       role_id INT,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE customers (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(100) NOT NULL,
                           contactInfo VARCHAR(255)
);

CREATE TABLE vehicles (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          licensePlate VARCHAR(20) NOT NULL,
                          make VARCHAR(50),
                          model VARCHAR(50),
                          customer_id INT,
                          FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE parking_lots (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              name VARCHAR(100) NOT NULL,
                              address VARCHAR(255),
                              capacity INT
);

CREATE TABLE parking_spaces (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                parkingLot_id INT,
                                spaceNumber VARCHAR(10),
                                status VARCHAR(20),
                                FOREIGN KEY (parkingLot_id) REFERENCES parking_lots(id)
);

CREATE TABLE parking_records (
                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                 vehicle_id INT,
                                 parkingSpace_id INT,
                                 entryTime DATETIME,
                                 exitTime DATETIME,
                                 feePaid DECIMAL(10,2),
                                 FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                                 FOREIGN KEY (parkingSpace_id) REFERENCES parking_spaces(id)
);

CREATE TABLE financial_transactions (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        parkingRecord_id INT,
                                        amount DECIMAL(10,2),
                                        paymentMethod VARCHAR(50),
                                        transactionTime DATETIME,
                                        FOREIGN KEY (parkingRecord_id) REFERENCES parking_records(id)
);

CREATE TABLE partners (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          contactInfo VARCHAR(255),
                          relationshipType VARCHAR(50)
);

CREATE TABLE system_logs (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             user_id INT,
                             actionType VARCHAR(50),
                             details TEXT,
                             timestamp DATETIME,
                             FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE global_configurations (
                                       key VARCHAR(50) PRIMARY KEY,
                                       value VARCHAR(255)
);

CREATE TABLE menus (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(50),
                       url VARCHAR(255),
                       icon VARCHAR(50),
                       parent_id INT,
                       FOREIGN KEY (parent_id) REFERENCES menus(id)
);

CREATE TABLE role_menu (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           role_id INT,
                           menu_id INT,
                           FOREIGN KEY (role_id) REFERENCES roles(id),
                           FOREIGN KEY (menu_id) REFERENCES menus(id)
);

