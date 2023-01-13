create table if not exists customer_history
(
    action varchar(8) default 'insert' not null,
    revision int auto_increment,
    customer_id varchar(50) not null,
    email varchar(50) not null comment 'Email address of the customer, used in registration and logging in.',
    password varchar(100) null,
    first_name varchar(20) not null comment 'First Name of the customer.',
    last_name varchar(20) not null comment 'Last Name of the customer.',
    segment varchar(20) not null comment 'Group of customers is divided based on the customers’ characteristics.',
    security_answer varchar(100) not null comment 'The answer of security question.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    security_question_id varchar(50) not null,
    index (security_question_id),
    primary key (customer_id, revision, action)
)
    engine=MyISAM;

create table if not exists fa22_sg_category
(
    category_id varchar(50) not null
        primary key,
    category varchar(20) not null comment 'Category name.',
    subcategory varchar(20) not null comment 'Sub-category name.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table if not exists fa22_sg_product
(
    product_id varchar(50) not null
        primary key,
    product_name varchar(200) not null comment 'Name of the product.',
    unit_price decimal(7,2) not null comment 'The price of a product.',
    discount decimal(2,2) not null comment 'Percentage of the discount, already applied to the product.',
    quantity int not null comment 'Quantity of the product that is in stock.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    category_id varchar(50) not null,
    constraint product_category_fk
        foreign key (category_id) references fa22_sg_category (category_id)
);

-- create definer = root@localhost trigger product_after_insert
-- 	after insert
-- 	on fa22_sg_product
-- 	for each row
-- BEGIN
--     INSERT INTO product_history (`action`, product_id, product_name, unit_price, discount, quantity, last_modified, category_id)
--     SELECT 'INSERT', p.product_id, p.product_name, p.unit_price, p.discount, p.quantity, CURRENT_TIMESTAMP, p.category_id
--     FROM fa22_sg_product p WHERE p.product_id = NEW.product_id;
-- END;
--
-- create definer = root@localhost trigger product_after_update
-- 	after update
--                      on fa22_sg_product
--                      for each row
-- BEGIN
-- INSERT INTO product_history
-- SELECT 'UPDATE', NULL, p.*
-- FROM fa22_sg_product p WHERE p.product_id = NEW.product_id;
-- UPDATE product_history
-- SET last_modified = current_timestamp()
-- WHERE product_id = NEW.product_id;
-- END;
--
-- create definer = root@localhost trigger product_before_delete
-- 	before delete
-- on fa22_sg_product
-- 	for each row
-- BEGIN
-- INSERT INTO product_history
-- SELECT 'DELETE', NULL, p.*
-- FROM fa22_sg_product p WHERE p.product_id = OLD.product_id;
-- UPDATE product_history
-- SET last_modified = current_timestamp()
-- WHERE product_id = OLD.product_id;
-- END;

create table if not exists fa22_sg_role
(
    role_id varchar(50) not null
        primary key,
    name varchar(30) not null
);

create table if not exists fa22_sg_security_question
(
    security_question_id varchar(50) not null
        primary key,
    security_question varchar(100) not null comment 'Security question.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table if not exists fa22_sg_customer
(
    customer_id varchar(50) not null
        primary key,
    email varchar(50) not null comment 'Email address of the customer, used in registration and logging in.',
    password varchar(100) null,
    first_name varchar(20) not null comment 'First Name of the customer.',
    last_name varchar(20) not null comment 'Last Name of the customer.',
    segment varchar(20) not null comment 'Group of customers is divided based on the customers’ characteristics.',
    security_answer varchar(100) not null comment 'The answer of security question.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    security_question_id varchar(50) not null,
    constraint email
        unique (email),
    constraint customer_security_question_fk
        foreign key (security_question_id) references fa22_sg_security_question (security_question_id)
);

create table if not exists fa22_sg_credit_card
(
    credit_card_id varchar(50) not null
        primary key,
    cardholder_first_name varchar(20) not null comment 'The first name of credit card.',
    cardholder_last_name varchar(20) not null comment 'The last name of credit card.',
    card_number varchar(30) not null comment 'The card number of credit card.',
    expired_date varchar(10) not null comment 'The expired date of credit card.',
    cvv varchar(10) not null comment 'CVV of credit card.',
    customer_id varchar(50) not null,
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint credit_card_customer_fk
        foreign key (customer_id) references fa22_sg_customer (customer_id)
);

-- create definer = root@localhost trigger customer_after_insert
-- 	after insert
-- 	on fa22_sg_customer
-- 	for each row
-- BEGIN
-- INSERT INTO customer_history
-- SELECT 'INSERT', NULL, c.*
-- FROM fa22_sg_customer c WHERE c.customer_id = NEW.customer_id;
-- UPDATE customer_history
-- SET last_modified = current_timestamp()
-- WHERE customer_id = NEW.customer_id;
-- END;
--
-- create definer = root@localhost trigger customer_after_update
-- 	after update
--                      on fa22_sg_customer
--                      for each row
-- BEGIN
-- INSERT INTO customer_history
-- SELECT 'UPDATE', NULL, c.*
-- FROM fa22_sg_customer c WHERE c.customer_id = NEW.customer_id;
-- UPDATE customer_history
-- SET last_modified = current_timestamp()
-- WHERE customer_id = NEW.customer_id;
-- END;
--
-- create definer = root@localhost trigger customer_before_delete
-- 	before delete
-- on fa22_sg_customer
-- 	for each row
-- BEGIN
-- INSERT INTO customer_history (action, customer_id, email, password, first_name, last_name, segment, security_answer, last_modified, security_question_id)
-- SELECT 'DELETE', c.customer_id, c.email, c.password, c.first_name, c.last_name, c.segment, c.security_answer, c.last_modified, c.security_question_id
-- FROM fa22_sg_customer c WHERE c.customer_id = OLD.customer_id;
-- UPDATE customer_history
-- SET last_modified = current_timestamp()
-- WHERE customer_id = OLD.customer_id;
-- END;

create table if not exists fa22_sg_customer_product
(
    quantity int not null comment 'Quantity of the product that is sold in one order.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    product_id varchar(50) not null,
    customer_id varchar(50) not null,
    primary key (product_id, customer_id),
    constraint customer_product_customer_fk
        foreign key (customer_id) references fa22_sg_customer (customer_id),
    constraint customer_product_product_fk
        foreign key (product_id) references fa22_sg_product (product_id)
);

create table if not exists fa22_sg_order
(
    order_id varchar(50) not null
        primary key,
    order_date datetime not null comment 'The date the order is placed.',
    ship_mode varchar(30) not null comment 'Ship days. Same day: 0 day, First class: 2 days, Second class: 4 days, Standard class: 6 days',
    postal_code varchar(10) null comment 'Postal Code in the shipping address, optional outside US.',
    city varchar(50) null,
    state varchar(50) null,
    country varchar(50) null,
    region varchar(20) not null comment 'Region of the shipping address.',
    market varchar(20) not null comment 'Where the market is in, eg. Europe.',
    order_priority varchar(30) not null comment 'Type of the priority of an order.',
    is_returned varchar(1) not null comment 'Y means that the order is returned, N means the order is not returned.',
    credit_card_holder varchar(50) not null,
    credit_card_number varchar(30) not null,
    credit_card_expired_date varchar(10) not null,
    credit_card_cvv varchar(100) not null,
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    customer_id varchar(50) not null,
    constraint order_customer_fk
        foreign key (customer_id) references fa22_sg_customer (customer_id)
);

-- create definer = root@localhost trigger order_after_insert
-- 	after insert
-- 	on fa22_sg_order
-- 	for each row
-- BEGIN
-- INSERT INTO order_history
-- SELECT 'INSERT', NULL, o.*
-- FROM fa22_sg_order o WHERE o.order_id = NEW.order_id;
-- UPDATE order_history
-- SET last_modified = current_timestamp()
-- WHERE order_id = NEW.order_id;
-- END;
--
-- create definer = root@localhost trigger order_after_update
-- 	after update
--                      on fa22_sg_order
--                      for each row
-- BEGIN
-- INSERT INTO order_history
-- SELECT 'UPDATE', NULL, o.*
-- FROM fa22_sg_order o WHERE o.order_id = NEW.order_id;
-- UPDATE order_history
-- SET last_modified = current_timestamp()
-- WHERE order_id = NEW.order_id;
-- END;
--
-- create definer = root@localhost trigger order_before_delete
-- 	before delete
-- on fa22_sg_order
-- 	for each row
-- BEGIN
-- INSERT INTO order_history
-- SELECT 'DELETE', NULL, o.*
-- FROM fa22_sg_order o WHERE o.order_id = OLD.order_id;
-- UPDATE order_history
-- SET last_modified = current_timestamp()
-- WHERE order_id = OLD.order_id;
-- END;

create table if not exists fa22_sg_order_product
(
    quantity int not null comment 'Quantity of the product that is sold in one order.',
    actual_price decimal(7,2) not null,
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    product_id varchar(50) not null,
    order_id varchar(50) not null,
    primary key (product_id, order_id),
    constraint order_product_order_fk
        foreign key (order_id) references fa22_sg_order (order_id),
    constraint order_product_product_fk
        foreign key (product_id) references fa22_sg_product (product_id)
);

create table if not exists fa22_sg_password_reset_token
(
    token varchar(50) not null
        primary key,
    customer_id varchar(50) not null,
    expiry_date datetime not null,
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    index (customer_id),
    constraint fa22_sg_password_reset_token_ibfk_1
        foreign key (customer_id) references fa22_sg_customer (customer_id)
);

create table if not exists fa22_sg_ship_address
(
    ship_address_id varchar(50) not null
        primary key,
    postal_code varchar(10) null comment 'Postal Code in the shipping address, optional outside US.',
    city varchar(50) null,
    state varchar(50) null,
    country varchar(50) null,
    region varchar(20) not null comment 'Region of the shipping address.',
    market varchar(20) not null comment 'Where the market is in, eg. Europe.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    customer_id varchar(50) not null,
    is_primary varchar(1) not null comment 'Y means that the address is primary address, N means the address is not primary address.',
    constraint ship_address_customer_fk
        foreign key (customer_id) references fa22_sg_customer (customer_id)
);

-- create definer = root@localhost trigger ship_address_after_insert
-- 	after insert
-- 	on fa22_sg_ship_address
-- 	for each row
-- BEGIN
-- INSERT INTO ship_address_history
-- SELECT 'INSERT', NULL, sa.*
-- FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = NEW.ship_address_id;
-- UPDATE ship_address_history
-- SET last_modified = current_timestamp()
-- WHERE ship_address_id = NEW.ship_address_id;
-- END;
--
-- create definer = root@localhost trigger ship_address_after_update
-- 	after update
--                      on fa22_sg_ship_address
--                      for each row
-- BEGIN
-- INSERT INTO ship_address_history
-- SELECT 'UPDATE', NULL, sa.*
-- FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = NEW.ship_address_id;
-- UPDATE ship_address_history
-- SET last_modified = current_timestamp()
-- WHERE ship_address_id = NEW.ship_address_id;
-- END;
--
-- create definer = root@localhost trigger ship_address_before_delete
-- 	before delete
-- on fa22_sg_ship_address
-- 	for each row
-- BEGIN
-- INSERT INTO ship_address_history
-- SELECT 'DELETE', NULL, sa.*
-- FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = OLD.ship_address_id;
-- UPDATE ship_address_history
-- SET last_modified = current_timestamp()
-- WHERE ship_address_id = OLD.ship_address_id;
-- END;

create table if not exists order_history
(
    action varchar(8) default 'insert' not null,
    revision int auto_increment,
    order_id varchar(50) not null,
    order_date datetime not null comment 'The date the order is placed.',
    ship_mode varchar(30) not null comment 'Ship days. Same day: 0 day, First class: 2 days, Second class: 4 days, Standard class: 6 days',
    postal_code varchar(10) null comment 'Postal Code in the shipping address, optional outside US.',
    city varchar(50) null,
    state varchar(50) null,
    country varchar(50) null,
    region varchar(20) not null comment 'Region of the shipping address.',
    market varchar(20) not null comment 'Where the market is in, eg. Europe.',
    order_priority varchar(30) not null comment 'Type of the priority of an order.',
    is_returned varchar(1) not null comment 'Y means that the order is returned, N means the order is not returned.',
    credit_card_holder varchar(50) not null,
    credit_card_number varchar(30) not null,
    credit_card_expired_date varchar(10) not null,
    credit_card_cvv varchar(100) not null,
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    customer_id varchar(50) not null,
    index (customer_id),
    primary key (order_id, revision, action)
)
    engine=MyISAM;

create table if not exists product_history
(
    action varchar(8) default 'insert' not null,
    revision int auto_increment,
    product_id varchar(50) not null,
    product_name varchar(200) not null comment 'Name of the product.',
    unit_price decimal(7,2) not null comment 'The price of a product.',
    discount decimal(2,2) not null comment 'Percentage of the discount, already applied to the product.',
    quantity int not null comment 'Quantity of the product that is in stock.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    category_id varchar(50) not null,
    index (category_id),
    primary key (product_id, revision, action)
)
    engine=MyISAM;

create table if not exists ship_address_history
(
    action varchar(8) default 'insert' not null,
    revision int auto_increment,
    ship_address_id varchar(50) not null,
    postal_code varchar(10) null comment 'Postal Code in the shipping address, optional outside US.',
    city varchar(50) null,
    state varchar(50) null,
    country varchar(50) null,
    region varchar(20) not null comment 'Region of the shipping address.',
    market varchar(20) not null comment 'Where the market is in, eg. Europe.',
    last_modified timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    customer_id varchar(50) not null,
    is_primary varchar(1) not null comment 'Y means that the address is primary address, N means the address is not primary address.',
    index (customer_id),
    primary key (ship_address_id, revision, action)
)
    engine=MyISAM;