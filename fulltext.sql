DROP TABLE IF EXISTS ft_movies;
DROP TABLE IF EXISTS ft_stars;

CREATE TABLE ft_movies (
    entryId VARCHAR(10),
    entry VARCHAR(100),
    PRIMARY KEY (entryId),
    FULLTEXT (entry));

INSERT INTO ft_movies(entryId, entry)
SELECT movies.id as entryId, movies.title as entry
FROM movies;

CREATE TABLE ft_stars (
    entryId VARCHAR(10),
    entry VARCHAR(100),
    PRIMARY KEY (entryId),
    FULLTEXT (entry));

INSERT INTO ft_stars(entryId, entry)
SELECT stars.id as entryId, stars.name as entry
FROM stars;