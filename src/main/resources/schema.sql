DROP TABLE IF EXISTS book;
CREATE TABLE book (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  author VARCHAR(255),
  genre VARCHAR(100),
  isbn VARCHAR(20),
  publication_date DATE,
  available BOOLEAN
);
