-- Creates database moviedb if it doesn't exist
CREATE DATABASE IF NOT EXISTS moviedb;

USE moviedb;

-- Creates movies table
CREATE TABLE movies (
id VARCHAR(10) NOT NULL,
title VARCHAR(100) NOT NULL,
year INTEGER NOT NULL,
director VARCHAR(100) NOT NULL,
PRIMARY KEY (id)
);

-- Creates starss table
CREATE TABLE stars (
id VARCHAR(10) NOT NULL,
name VARCHAR(100) NOT NULL,
birthYear INTEGER,
PRIMARY KEY (id)
);

-- Creates stars_in_movies table
CREATE TABLE stars_in_movies (
starId VARCHAR(10),
movieId VARCHAR(10),
PRIMARY KEY (starId, movieId),
FOREIGN KEY (starId) REFERENCES stars (id),
FOREIGN KEY (movieId) REFERENCES movies (id)
);

-- Creates genres table
CREATE TABLE genres (
id INTEGER NOT NULL AUTO_INCREMENT,
name VARCHAR(32) NOT NULL,
PRIMARY KEY (id)
);

-- Creates genres_in_movies table
CREATE TABLE genres_in_movies (
genreId INTEGER NOT NULL,
movieId VARCHAR(10) NOT NULL,
PRIMARY KEY (genreId, movieId),
FOREIGN KEY (genreId) REFERENCES genres (id),
FOREIGN KEY (movieId) REFERENCES movies (id)
);

-- Creates creditcards table
CREATE TABLE creditcards (
id VARCHAR(20) NOT NULL,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
expiration DATE NOT NULL,
PRIMARY KEY (id)
);

-- Creates customers table
CREATE TABLE customers (
id INTEGER NOT NULL AUTO_INCREMENT,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
ccId VARCHAR(20) NOT NULL,
address VARCHAR(200) NOT NULL,
email VARCHAR(50) NOT NULL,
password VARCHAR(20) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (ccId) REFERENCES creditcards (id) ON DELETE CASCADE
);

-- Creates sales table
CREATE TABLE sales (
id INTEGER NOT NULL AUTO_INCREMENT,
customerId INTEGER NOT NULL,
movieId VARCHAR(10) NOT NULL,
saleDate DATE NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (customerId) REFERENCES customers (id),
FOREIGN KEY (movieId) REFERENCES movies (id)
);

-- Creates ratings table
CREATE TABLE ratings (
movieId VARCHAR(10),
rating FLOAT,
numVotes INTEGER,
PRIMARY KEY (movieId),
FOREIGN KEY (movieId) REFERENCES movies (id)
);