INSERT INTO BOOK (name, author) VALUES ('Effective Java', 'Joshua Bloch');
INSERT INTO BOOK (name, author) VALUES ('자바 ORM 표준 JPA 프로그래밍', '김영한');
INSERT INTO LIBRARY (name) VALUES ('서울 도서관');
INSERT INTO LIBRARY (name) VALUES ('부산 도서관');
INSERT INTO LIBRARY_BOOK (book_id, library_id) VALUES (1, 1);
INSERT INTO LIBRARY_BOOK (book_id, library_id) VALUES (1, 2);
INSERT INTO LIBRARY_BOOK (book_id, library_id) VALUES (2, 1);
INSERT INTO LIBRARY_BOOK (book_id, library_id) VALUES (2, 2);