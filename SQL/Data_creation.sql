INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('5243fwersdgerv', 'Hans', 'Peter', 'peter@gmail.com', 'Milfhunter69', '2004-04-04', 'fgwefwef');

INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('12', 'Hans', 'Peter', 'f3@gmail.com', 'eggert', '2004-04-04', 'fgwefwef');

INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('34', 'Hans', 'Peter', 'fw@gmail.com', 'pralle', '2004-04-04', 'fgwefwef'),
       ('db7c3024-d53d-4bcf-85df-718cc5198b90', 'Daniel', 'Weinstein', 'd@w.de', 'MCDanDanHD', '2003-05-15', '$2a$10$Ds37m4r/MnEu0e2Vz7M75OmRd/dsYO//Cn0EcKBnvnWx39sXiPVoO');

INSERT INTO userlocation (user_id, latitude, longitude, created_on)
VALUES ('5243fwersdgerv', 0.69, -1.87, current_timestamp);

INSERT INTO event (id, user_id, name, description, start, "end", max_people)
VALUES ('abc', '12', 'Fette party', 'SAUFEEEEEn', '2025-04-04', '2025-04-05', 187);

INSERT INTO eventlocation (event_id, latitude, longitude, created_on)
VALUES ('abc', 0.69, -1.87, current_timestamp);

INSERT INTO pictures (event_id, url)
VALUES ('abc', 'someurl.com/penis');

INSERT INTO ticket (id, event_id, user_id)
VALUES ('peter', 'abc', '5243fwersdgerv'),
       ('someticketid', 'abc', 'db7c3024-d53d-4bcf-85df-718cc5198b90');

INSERT INTO friends (usera, userb)
VALUES ('34', '5243fwersdgerv'),
       ('12', '34');

INSERT INTO ticketrequest (user_id, event_id)
VALUES ('12', 'abc', current_timestamp);

SELECT *
FROM event inner join eventlocation e on event.id = e.event_id
WHERE latitude > 0 AND latitude < 10 AND longitude > -10 AND longitude < 10;

-- SELECT "user".nick_name
-- FROM ((SELECT f.usera as friendId
--  FROM friends f
--  where (userb = '34'))
-- UNION
-- (SELECT f.userb as friendId
--  FROM friends f
--  where (usera = '34'))) AS friends
-- INNER JOIN "user" ON "user".id = friends.friendId;


-- SELECT * FROM getFriendInfos('34');