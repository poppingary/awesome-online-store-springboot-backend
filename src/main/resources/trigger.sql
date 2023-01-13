CREATE TRIGGER customer_after_insert AFTER INSERT ON fa22_sg_customer FOR EACH ROW
BEGIN
    INSERT INTO customer_history (`action`, customer_id, email, password, first_name, last_name, segment, security_answer, last_modified, security_question_id)
    SELECT 'INSERT', c.customer_id , c.email, c.password, c.first_name, c.last_name, c.segment, c.security_answer, CURRENT_TIMESTAMP, c.security_question_id
    FROM fa22_sg_customer c WHERE c.customer_id = NEW.customer_id;
END;

CREATE TRIGGER customer_after_update AFTER UPDATE ON fa22_sg_customer FOR EACH ROW
BEGIN
    INSERT INTO customer_history (`action`, customer_id, email, password, first_name, last_name, segment, security_answer, last_modified, security_question_id)
    SELECT 'UPDATE', c.customer_id , c.email, c.password, c.first_name, c.last_name, c.segment, c.security_answer, CURRENT_TIMESTAMP, c.security_question_id
    FROM fa22_sg_customer c WHERE c.customer_id = NEW.customer_id;
END;

CREATE TRIGGER customer_before_delete BEFORE DELETE ON fa22_sg_customer FOR EACH ROW
BEGIN
    INSERT INTO customer_history (`action`, customer_id, email, password, first_name, last_name, segment, security_answer, last_modified, security_question_id)
    SELECT 'DELETE', c.customer_id , c.email, c.password, c.first_name, c.last_name, c.segment, c.security_answer, CURRENT_TIMESTAMP, c.security_question_id
    FROM fa22_sg_customer c WHERE c.customer_id = OLD.customer_id;
END;



CREATE TRIGGER order_after_insert AFTER INSERT ON fa22_sg_order FOR EACH ROW
BEGIN
    INSERT INTO order_history (`action`, order_id, order_date, ship_mode, postal_code, city, state, country, region, market, order_priority, is_returned, credit_card_holder, credit_card_number, credit_card_expired_date, credit_card_cvv, last_modified, customer_id)
    SELECT 'INSERT', o.order_id, o.order_date, o.ship_mode, o.postal_code, o.city, o.state, o.country, o.region, o.market, o.order_priority, o.is_returned, o.credit_card_holder, o.credit_card_number, o.credit_card_expired_date, o.credit_card_cvv, CURRENT_TIMESTAMP, o.customer_id
    FROM fa22_sg_order o WHERE o.order_id = NEW.order_id;
END;

CREATE TRIGGER order_after_update AFTER UPDATE ON fa22_sg_order FOR EACH ROW
BEGIN
    INSERT INTO order_history (`action`, order_id, order_date, ship_mode, postal_code, city, state, country, region, market, order_priority, is_returned, credit_card_holder, credit_card_number, credit_card_expired_date, credit_card_cvv, last_modified, customer_id)
    SELECT 'UPDATE', o.order_id, o.order_date, o.ship_mode, o.postal_code, o.city, o.state, o.country, o.region, o.market, o.order_priority, o.is_returned, o.credit_card_holder, o.credit_card_number, o.credit_card_expired_date, o.credit_card_cvv, CURRENT_TIMESTAMP, o.customer_id
    FROM fa22_sg_order o WHERE o.order_id = NEW.order_id;
END;

CREATE TRIGGER order_before_delete BEFORE DELETE ON fa22_sg_order FOR EACH ROW
BEGIN
    INSERT INTO order_history (`action`, order_id, order_date, ship_mode, postal_code, city, state, country, region, market, order_priority, is_returned, credit_card_holder, credit_card_number, credit_card_expired_date, credit_card_cvv, last_modified, customer_id)
    SELECT 'DELETE', o.order_id, o.order_date, o.ship_mode, o.postal_code, o.city, o.state, o.country, o.region, o.market, o.order_priority, o.is_returned, o.credit_card_holder, o.credit_card_number, o.credit_card_expired_date, o.credit_card_cvv, CURRENT_TIMESTAMP, o.customer_id
    FROM fa22_sg_order o WHERE o.order_id = OLD.order_id;
END;



CREATE TRIGGER ship_address_after_insert AFTER INSERT ON fa22_sg_ship_address FOR EACH ROW
BEGIN
    INSERT INTO ship_address_history (`action`, ship_address_id, postal_code, city, state, country, region, market, last_modified, customer_id, is_primary)
    SELECT 'INSERT', sa.ship_address_id, sa.postal_code, sa.city, sa.state, sa.country, sa.region, sa.market, CURRENT_TIMESTAMP, sa.customer_id, sa.is_primary
    FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = NEW.ship_address_id;
END;

CREATE TRIGGER ship_address_after_update AFTER UPDATE ON fa22_sg_ship_address FOR EACH ROW
BEGIN
    INSERT INTO ship_address_history (`action`, ship_address_id, postal_code, city, state, country, region, market, last_modified, customer_id, is_primary)
    SELECT 'UPDATE', sa.ship_address_id, sa.postal_code, sa.city, sa.state, sa.country, sa.region, sa.market, CURRENT_TIMESTAMP, sa.customer_id, sa.is_primary
    FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = NEW.ship_address_id;
END;

CREATE TRIGGER ship_address_before_delete BEFORE DELETE ON fa22_sg_ship_address FOR EACH ROW
BEGIN
    INSERT INTO ship_address_history (`action`, ship_address_id, postal_code, city, state, country, region, market, last_modified, customer_id, is_primary)
    SELECT 'DELETE', sa.ship_address_id, sa.postal_code, sa.city, sa.state, sa.country, sa.region, sa.market, CURRENT_TIMESTAMP, sa.customer_id, sa.is_primary
    FROM fa22_sg_ship_address sa WHERE sa.ship_address_id = OLD.ship_address_id;
END;



CREATE DEFINER=`root`@`localhost` TRIGGER `product_after_insert` AFTER INSERT ON `fa22_sg_product` FOR EACH ROW
BEGIN
    INSERT INTO product_history (`action`, product_id, product_name, unit_price, discount, quantity, last_modified, category_id)
    SELECT 'INSERT', p.product_id, p.product_name, p.unit_price, p.discount, p.quantity, CURRENT_TIMESTAMP, p.category_id
    FROM fa22_sg_product p WHERE p.product_id = NEW.product_id;
END

CREATE TRIGGER product_after_update AFTER UPDATE ON fa22_sg_product FOR EACH ROW
BEGIN
INSERT INTO product_history (`action`, product_id, product_name, unit_price, discount, quantity, last_modified, category_id)
SELECT 'UPDATE', p.product_id, p.product_name, p.unit_price, p.discount, p.quantity, CURRENT_TIMESTAMP, p.category_id
FROM fa22_sg_product p WHERE p.product_id = NEW.product_id;
END;

CREATE TRIGGER product_before_delete BEFORE DELETE ON fa22_sg_product FOR EACH ROW
BEGIN
    INSERT INTO product_history (`action`, product_id, product_name, unit_price, discount, quantity, last_modified, category_id)
    SELECT 'DELETE', p.product_id, p.product_name, p.unit_price, p.discount, p.quantity, CURRENT_TIMESTAMP, p.category_id
    FROM fa22_sg_product p WHERE p.product_id = OLD.product_id;
END;