USE moviedb;

-- Creates employees table
CREATE TABLE employees (
email varchar(50),
password varchar(20) not null,
fullname varchar(100),
PRIMARY KEY (email)
);

-- Inserts employee
INSERT INTO employees VALUES('classta@email.edu', 'classta', 'TA CS122B');