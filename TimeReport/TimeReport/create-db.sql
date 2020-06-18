
-- Create DB and user

-- sudo mysql -h localhost  -u root -p

show databases;

DROP DATABASE IF EXISTS timereport;
DROP USER IF EXISTS timereport;

CREATE DATABASE timereport;
CREATE USER 'timereport' IDENTIFIED BY 'timereport';
GRANT USAGE ON *.* TO 'timereport'@localhost IDENTIFIED BY 'timereport';
GRANT ALL privileges ON `timereport`.* TO 'timereport'@localhost;
FLUSH PRIVILEGES;


-- Populate the DB

-- mysql -u timereport -p
-- source timereport.sql

