-- Active: 1699698539334@@127.0.0.1@5433@postgres


DROP TABLE IF  EXISTS user_simple_temp CASCADE;
DROP TABLE IF  EXISTS chatroom_temp CASCADE;
DROP TABLE IF  EXISTS message_simple_temp CASCADE;
DROP SEQUENCE IF EXISTS user_simple_id_seq CASCADE;
DROP SEQUENCE IF EXISTS chatroom_id_seq CASCADE;
DROP SEQUENCE IF EXISTS message_id_seq CASCADE;

/*================================================*/



CREATE TABLE user_simple_temp (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_simple (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


INSERT INTO user_simple_temp SELECT DISTINCT * FROM user_simple;

DROP TABLE IF EXISTS user_simple CASCADE;

ALTER TABLE user_simple_temp RENAME TO user_simple;

CREATE SEQUENCE IF NOT EXISTS user_simple_id_seq;

SELECT setval('user_simple_id_seq', (SELECT max(id) FROM user_simple));

ALTER TABLE user_simple ALTER COLUMN id SET DEFAULT nextval('user_simple_id_seq');




/*================================================*/


CREATE TABLE chatroom_temp (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS chatroom (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO chatroom_temp SELECT * FROM chatroom;

DROP TABLE IF EXISTS chatroom CASCADE;

ALTER TABLE chatroom_temp RENAME TO chatroom;

CREATE SEQUENCE IF NOT EXISTS chatroom_id_seq;
SELECT setval('chatroom_id_seq', (SELECT max(id) FROM chatroom));

ALTER TABLE chatroom ALTER COLUMN id SET DEFAULT nextval('chatroom_id_seq');



/*================================================*/


CREATE TABLE message_simple_temp (
    id SERIAL PRIMARY KEY,
    author INT REFERENCES user_simple (id) NOT NULL,
    chatroom INT REFERENCES chatroom (id) NOT NULL,
    text_message VARCHAR(1000),
    date TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS message_simple (
    id SERIAL PRIMARY KEY,
    author INT REFERENCES user_simple (id) NOT NULL,
    chatroom INT REFERENCES chatroom (id) NOT NULL,
    text_message VARCHAR(1000),
    date TIMESTAMP 
);

INSERT INTO message_simple_temp SELECT * FROM message_simple;

DROP TABLE IF EXISTS message_simple;

ALTER TABLE message_simple_temp RENAME TO message_simple;
CREATE SEQUENCE IF NOT EXISTS message_id_seq;

SELECT setval('message_id_seq', (SELECT max(id) FROM message_simple));

ALTER TABLE message_simple ALTER COLUMN id SET DEFAULT nextval('message_id_seq');

/*================================================*/

INSERT INTO user_simple (name, password)
SELECT 'admin', 'admin'
WHERE NOT EXISTS (
    SELECT 1 FROM user_simple WHERE name = 'admin'
);

INSERT INTO chatroom (name)
SELECT 'test'
WHERE NOT EXISTS (
    SELECT 1 FROM chatroom WHERE name = 'test'
);


-- DROP TABLE IF EXISTS message_simple CASCADE;
-- DROP TABLE IF EXISTS chatroom CASCADE;
-- DROP TABLE IF EXISTS user_simple CASCADE;


-- SELECT * FROM message_simple AS m
-- JOIN user_simple AS u ON m.author = u.id
-- JOIN chatroom AS ch ON m.chatroom = ch.id
-- WHERE chatroom = 2 LIMIT 30;