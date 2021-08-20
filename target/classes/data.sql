CREATE TABLE USERS (
id INT AUTO_INCREMENT PRIMARY KEY ,
user_name varchar(200) NOT NULL ,
created_date timestamp NOT NULL,
created_by varchar(200) NOT NULL,
modified_by varchar(200) NULL,
modified_date timestamp NULL
);

CREATE TABLE CARD_DETAILS (
id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT NOT NULL,
card_number varchar(max) NOT NULL,
card_pin INT NOT NULL,
card_expiry_date date NOT NULL,
card_balance BIGINT DEFAULT (0) NOT NULL,
FOREIGN KEY (user_id) REFERENCES USERS(id)
);

INSERT INTO USERS(user_name, created_date, created_by) VALUES
('John Doe', CURRENT_TIMESTAMP(), 'Admin'),
('Stan Loly', CURRENT_TIMESTAMP(), 'Admin');

INSERT INTO CARD_DETAILS(user_id, card_number, card_pin, card_expiry_date, card_balance) VALUES
(1, '456789012341', '3421', '2031-08-17', 100000),
(2, '456789012781', '3421', '2031-08-17', 1000000);