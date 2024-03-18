INSERT INTO users (name, surname, password, email, role) VALUES ('Marek', 'Kowalski', 'admin','admin@localhost', 'ADMIN');
INSERT INTO users (name, surname, password, email, role) VALUES ('Adam', 'Nowak', 'adamn','adamn@gmail.com','USER');
INSERT INTO users (name, surname, password, email, role) VALUES ('Jan', 'Kowalczyk', 'jank','jank@gmail.com','USER');
INSERT INTO users (name, surname, password, email, role) VALUES ('Anna', 'Kwiatkowska', 'annak','annak@gmail.com','USER');

INSERT INTO categories (name) VALUES ('Sport');
INSERT INTO categories (name) VALUES ('IT');
INSERT INTO categories (name) VALUES ('Music');
INSERT INTO categories (name) VALUES ('Movies');
INSERT INTO categories (name) VALUES ('Books');
INSERT INTO categories (name) VALUES ('Travel');
INSERT INTO categories (name) VALUES ('Food');
INSERT INTO categories (name) VALUES ('Health');
INSERT INTO categories (name) VALUES ('Fashion');
INSERT INTO categories (name) VALUES ('Cars');
INSERT INTO categories (name) VALUES ('Animals');
INSERT INTO categories (name) VALUES ('Nature');
INSERT INTO categories (name) VALUES ('Science');
INSERT INTO categories (name) VALUES ('History');
INSERT INTO categories (name) VALUES ('Art');
INSERT INTO categories (name) VALUES ('Culture');
INSERT INTO categories (name) VALUES ('Religion');
INSERT INTO categories (name) VALUES ('Politics');
INSERT INTO categories (name) VALUES ('Economy');
INSERT INTO categories (name) VALUES ('Society');
INSERT INTO categories (name) VALUES ('Education');
INSERT INTO categories (name) VALUES ('Technology');

INSERT INTO posts (title, user_id, category_id, creation_date, modification_date, content)
VALUES
    ('Post 1',
     2,
     5,
     '2024-01-01 12:00:00',
     '2024-01-01 12:00:00',
     'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. In nulla posuere sollicitudin aliquam ultrices. Tempor id eu nisl nunc mi. Venenatis a condimentum vitae sapien pellentesque. Lacus sed viverra tellus in hac habitasse platea dictumst vestibulum. Faucibus a pellentesque sit amet porttitor. Nec feugiat nisl pretium fusce id velit. Magna fringilla urna porttitor rhoncus dolor purus non. Ultricies mi quis hendrerit dolor magna eget est lorem ipsum. Mauris pharetra et ultrices neque ornare aenean euismod elementum.'
    );

INSERT INTO posts (title, user_id, category_id, creation_date, modification_date, content)
VALUES
    ('Post 2',
     3,
     2,
     '2024-01-01 12:00:00',
     '2024-01-01 12:00:00',
     'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. In nulla posuere sollicitudin aliquam ultrices. Tempor id eu nisl nunc mi. Venenatis a condimentum vitae sapien pellentesque. Lacus sed viverra tellus in hac habitasse platea dictumst vestibulum. Faucibus a pellentesque sit amet porttitor. Nec feugiat nisl pretium fusce id velit. Magna fringilla urna porttitor rhoncus dolor purus non. Ultricies mi quis hendrerit dolor magna eget est lorem ipsum. Mauris pharetra et ultrices neque ornare aenean euismod elementum.'
    );

INSERT INTO posts (title, user_id, category_id, creation_date, modification_date, content)
VALUES
    ('Post 3',
     4,
     8,
     '2024-01-01 12:00:00',
     '2024-01-01 12:00:00',
     'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. In nulla posuere sollicitudin aliquam ultrices. Tempor id eu nisl nunc mi. Venenatis a condimentum vitae sapien pellentesque. Lacus sed viverra tellus in hac habitasse platea dictumst vestibulum. Faucibus a pellentesque sit amet porttitor. Nec feugiat nisl pretium fusce id velit. Magna fringilla urna porttitor rhoncus dolor purus non. Ultricies mi quis hendrerit dolor magna eget est lorem ipsum. Mauris pharetra et ultrices neque ornare aenean euismod elementum.'
    );