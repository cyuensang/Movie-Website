DROP FUNCTION IF EXISTS randomizer;
DROP PROCEDURE IF EXISTS add_movie;

-- change delimiter to $$
DELIMITER $$

CREATE FUNCTION randomizer()
RETURNS VARCHAR(10)
BEGIN
	DECLARE randVal VARCHAR(10);
	DECLARE randVal1 VARCHAR(1);
	DECLARE randCtr INT;

	SET randVal = '';
	SET randCtr = 10;	
	
	WHILE randCtr > 0 DO
		IF randCtr = 9 OR randCtr = 7 OR randCtr = 5 OR randCtr = 3 OR randCtr = 1 THEN
			SET randVal1 = (SELECT LOWER(CONV(FLOOR(RAND()*36), 10, 36)));
		ELSE
			SET randVal1 = (SELECT UPPER(CONV(FLOOR(RAND()*36), 10, 36)));
		END IF;
        
		SET randVal = CONCAT(randVal, randVal1);		
		SET randCtr = randCtr - 1;
        
	END WHILE;
	RETURN randVal;
END
$$

CREATE PROCEDURE add_movie(IN movie_id VARCHAR(10), IN movie_title VARCHAR(100), 
IN movie_year INTEGER, IN movie_director VARCHAR(100), 
IN star_name VARCHAR(100), IN star_birthdate INTEGER, 
IN genre_name VARCHAR(32))

BEGIN
	DECLARE m_add_movie VARCHAR(300) DEFAULT NULL;
	DECLARE m_add_star VARCHAR(300) DEFAULT NULL;
    DECLARE m_add_genre VARCHAR(300) DEFAULT NULL;
    DECLARE m_up VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_title VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_year VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_director VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_star1 VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_star2 VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_genre1 VARCHAR(300) DEFAULT NULL;
    DECLARE m_up_genre2 VARCHAR(300) DEFAULT NULL;
    
    DECLARE varchar_id VARCHAR(10);
    DECLARE hold VARCHAR(300);
	DECLARE int_id INTEGER;
    
	-- Add movie:
	IF(movie_id IS NULL AND star_name IS NULL AND star_birthdate IS NULL AND genre_name IS NULL) THEN
		-- Error to add movie
		IF(movie_title IS NULL) THEN
			SET m_add_movie = CONCAT("Add movie error: movie title field missing.");
		ELSEIF(movie_year IS NULL) THEN
			SET m_add_movie = CONCAT("Add movie error: movie year field missing.");
		ELSEIF(movie_director IS NULL) THEN
			SET m_add_movie = CONCAT("Add movie error: movie director field missing.");
		-- Proceed to add movie if not in DB
		ELSEIF(NOT EXISTS(SELECT * FROM movies WHERE movies.title = movie_title AND movies.year = movie_year AND movies.director = movie_director )) THEN
			SET varchar_id = randomizer();
            INSERT INTO movies VALUES(varchar_id, movie_title, movie_year, movie_director);
            SET m_add_movie = CONCAT("Add movie success(title: ", movie_title, " | year: ", movie_year, " | director: ", movie_director,"): movie added in the database with id: ", varchar_id, ".");
		-- Error movie already in the DB
        ELSE
			SET m_add_movie = CONCAT("Add movie error(title: ", movie_title, " | year: ", movie_year, " | director: ", movie_director,"): movie already in the database.");
		END IF;
        
	-- Add star
    ELSEIF(movie_id IS NULL AND movie_title IS NULL AND movie_year IS NULL AND movie_director IS NULL AND genre_name IS NULL) THEN
		-- Error to add star
		IF(star_name IS NULL) THEN
			SET m_add_star = "Add star error: star name missing.";
		-- Proceed to add star without birthdate
		ELSEIF(NOT EXISTS(SELECT * FROM stars WHERE stars.name = star_name)) THEN
			IF(star_birthdate IS NULL) THEN 
				SET varchar_id = randomizer();
				INSERT INTO stars (id, name) VALUES(varchar_id, star_name);
			ELSE
				SET varchar_id = randomizer();
				INSERT INTO stars (id, name, birthYear) VALUES(varchar_id, star_name, star_birthdate);
			END IF;
            SET m_add_star = CONCAT("Add star success(name: ", star_name, "): star added in the database with id: ", varchar_id, ".");
		-- Proceed to add star with birthdate
		ELSE
			SET m_add_star = CONCAT("Add star error(name: ", star_name, "): star already in the database.");
		END IF;
        
	-- Add genre
    ELSEIF(movie_id IS NULL AND movie_title IS NULL AND movie_year IS NULL AND movie_director IS NULL AND star_name IS NULL AND star_birthdate IS NULL) THEN
		-- Error to add genre
		IF(genre_name IS NULL) THEN
			SET m_add_genre = "Add genre error: genre name missing.";
		-- Proceed to add genre
		ELSEIF(NOT EXISTS(SELECT * FROM genres WHERE genres.name = genre_name)) THEN
			SET int_id = ((SELECT MAX(id) FROM genres)+1);
			INSERT INTO genres VALUES(int_id, genre_name );
            SET m_add_genre = CONCAT("Add genre sucess(name: ", genre_name, "): genre added in the database with id: ", int_id, ".");
		ELSE
			SET m_add_genre = CONCAT("Add genre error(name: ", genre_name, "): genre already in the database.");
		END IF;
        
	-- Update movie
	ELSE
		-- Error to update movie
		IF(movie_id IS NULL) THEN
			SET m_up = "Update movie error: movie id missing.";
		-- Proceed to update movie
        ELSEIF(NOT EXISTS(SELECT * FROM movies WHERE id = movie_id)) THEN
			SET m_up = CONCAT("Update movie error: movie id: ", movie_id, " not found in the database.");
		ELSE
			SET m_up = CONCAT("Update movie: movie id: ", movie_id, " found in the database.");
			-- Update movie's attribute
            IF(movie_title IS NOT NULL) THEN
				SET hold = (SELECT title FROM movies WHERE id = movie_id);
				UPDATE movies SET movies.title = movie_title WHERE movies.id = movie_id;
                SET m_up_title = CONCAT("Update movie(id: ", movie_id, "): movie title: " , hold, " overwritten by ", movie_title, ".");
                END IF;
			IF(movie_year IS NOT NULL) THEN
				SET hold = (SELECT year FROM movies WHERE id = movie_id);
				UPDATE movies SET movies.year = movie_year WHERE movies.id = movie_id;
                SET m_up_year = CONCAT("Update movie(id: ", movie_id, "): movie year: " , hold, " overwritten by ", movie_year, ".");
                END IF;
			IF(movie_director IS NOT NULL) THEN
				SET hold = (SELECT director FROM movies WHERE id = movie_id);
				UPDATE movies SET movies.director = movie_director WHERE movies.id = movie_id;
                SET m_up_director = CONCAT("Update movie(id: ", movie_id, "): movie director: " , hold, " overwritten by ", movie_director, ".");
                END IF;
        
			-- Get star id or create a new one
			IF(star_name IS NOT NULL) THEN
                -- Insert star in database if necessary
                IF(NOT EXISTS(SELECT * FROM stars WHERE stars.name = star_name)) THEN
					SET varchar_id = randomizer();
					INSERT INTO stars (id, name) VALUES(varchar_id, star_name);
                    SET m_up_star1 = CONCAT("Update movie(id: ", movie_id, "): new star: ", star_name, " added in the database with id: ", varchar_id, ".");
				ELSE
					SET varchar_id = (SELECT stars.id FROM stars WHERE stars.name = star_name);
					SET m_up_star1 = CONCAT("Update movie(id: ", movie_id, "): star: ", star_name, " already in the database.");
				END IF;
                
                -- Insert star in movie if necessary
                IF(NOT EXISTS(SELECT * FROM stars_in_movies WHERE starId = varchar_id AND movieId = movie_id)) THEN
					INSERT INTO stars_in_movies VALUES(varchar_id, movie_id);
                    SET m_up_star2 = CONCAT("Update movie(id: ", movie_id, "): star: ", star_name, " added in the selected movie.");
				ELSE
					SET m_up_star2 = CONCAT("Update movie(id: ", movie_id, "): star: ", star_name, " already in the selected movie.");
				END IF;
			END IF;
            
            -- Get genre id or create a new one
			IF(genre_name IS NOT NULL) THEN
                -- Insert genre in database if necessary
                IF(NOT EXISTS(SELECT * FROM genres WHERE genres.name = genre_name)) THEN
					SET int_id = ((SELECT MAX(id) FROM genres)+1);
					INSERT INTO genres VALUES(int_id, genre_name);
                    SET m_up_genre1 = CONCAT("Update movie(id: ", movie_id, "): new genre: ", genre_name, " added in the database with id: ", int_id, ".");
				ELSE
					SET int_id = (SELECT genres.id FROM genres WHERE genres.name = genre_name);
					SET m_up_genre1 = CONCAT("Update movie(id: ", movie_id, "): genre: ", genre_name, " already in the database.");
				END IF;
                
                -- Insert genre in movie if necessary
                IF(NOT EXISTS(SELECT * FROM genres_in_movies WHERE genreId = int_id AND movieId = movie_id)) THEN
					INSERT INTO genres_in_movies VALUES(int_id, movie_id);
                    SET m_up_genre2 = CONCAT("Update movie(id: ", movie_id, "): genre: ", genre_name, " added in the selected movie.");
				ELSE
					SET m_up_genre2 = CONCAT("Update movie(id: ", movie_id, "): genre: ", genre_name, " already in the selected movie.");
				END IF;
			END IF;
		END IF;
        
	END IF;
    SELECT CONCAT_WS(', ', m_add_movie, m_add_star, m_add_genre, m_up, m_up_title, m_up_year, m_up_director, 
    m_up_star1, m_up_star2, m_up_genre1, m_up_genre2) as message;
END
$$
-- change back delimiter to ;
DELIMITER ;