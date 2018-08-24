
CREATE DATABASE moviedb;
USE moviedb;
CREATE TABLE movies 
(
	id varchar(10) PRIMARY KEY,
    title varchar(100) NOT NULL,
    year INT NOT NULL,
    director varchar(100) NOT NULL
);

CREATE TABLE stars
(
	id varchar(10) PRIMARY KEY,
    name varchar(100) NOT NULL,
    birthYear INT
);

CREATE TABLE stars_in_movies
(
	starId varchar(10) NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN  KEY (starId) REFERENCES stars(id)
		ON DELETE CASCADE,
	FOREIGN KEY (movieId) REFERENCES movies(id)
		ON DELETE CASCADE
);

CREATE TABLE genres
(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE genres_in_movies
(
	genreId INT NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(id)
		ON DELETE CASCADE,
	FOREIGN KEY (movieId) REFERENCES movies(id)
		ON DELETE CASCADE
);

CREATE TABLE creditcards
(
	id varchar(20) PRIMARY KEY,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    expiration DATE NOT NULL
);
CREATE TABLE customers 
(
	id INT PRIMARY KEY AUTO_INCREMENT,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    ccId varchar(20) NOT NULL,
    address varchar(200) NOT NULL,
    email varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    
    FOREIGN KEY (ccid) REFERENCES creditcards(id)
);
CREATE TABLE sales
(
	id INT PRIMARY KEY auto_increment,
    customerId INT NOT NULL,
    movieId varchar(10) NOT NULL,
    saleDate date NOT NULL,
    
    FOREIGN KEY(customerId) REFERENCES customers(id),
    FOREIGN KEY (movieId) REFERENCES movies(id)
    ON DELETE CASCADE
);

CREATE TABLE ratings
(
	movieId varchar(10) NOT NULL,
    rating float NOT NULL,
    numVotes INT NOT NULL,
    
    FOREIGN KEY(movieId) REFERENCES movies(id)
		ON DELETE CASCADE
    
)