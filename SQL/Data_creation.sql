INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('5243fwersdgerv', 'Hans', 'Peter', 'peter@gmail.com', 'Milfhunter69', '2004-04-04', 'fgwefwef')
ON CONFLICT DO NOTHING;

INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('12', 'Hans', 'Peter', 'f3@gmail.com', 'eggert', '2004-04-04', 'fgwefwef')
ON CONFLICT DO NOTHING;

INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('34', 'Hans', 'Peter', 'fw@gmail.com', 'pralle', '2004-04-04', 'fgwefwef')
ON CONFLICT DO NOTHING;
INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, password)
VALUES ('db7c3024-d53d-4bcf-85df-718cc5198b90', 'Daniel', 'Weinstein', 'd@w.de', 'MCDanDanHD', '2003-05-15', '$2a$10$Ds37m4r/MnEu0e2Vz7M75OmRd/dsYO//Cn0EcKBnvnWx39sXiPVoO')
ON CONFLICT DO NOTHING;

INSERT INTO userlocation (user_id, latitude, longitude, created_on)
VALUES ('5243fwersdgerv', 0.69, -1.87, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO event (id, user_id, name, description, start, "end", max_people)
VALUES ('abc', '12', 'Fette party', 'SAUFEEEEEn', '2025-04-04', '2025-04-05', 187)
ON CONFLICT DO NOTHING;

INSERT INTO event (id, user_id, name, description, start, "end", max_people)
VALUES ('def', 'db7c3024-d53d-4bcf-85df-718cc5198b90', 'Andere fette party', 'Suff suff suff', '2025-04-04', '2025-04-05', 420)
ON CONFLICT DO NOTHING;

INSERT INTO eventlocation (event_id, latitude, longitude, created_on)
VALUES ('abc', 0.69, -1.87, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO eventlocation (event_id, latitude, longitude, created_on)
VALUES ('def', 51.27, 6.6188, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO pictures (event_id, url)
VALUES ('abc', 'someurl.com/penis')
ON CONFLICT DO NOTHING;

INSERT INTO ticket (id, event_id, user_id)
VALUES ('peter', 'abc', '5243fwersdgerv')
ON CONFLICT DO NOTHING;
INSERT INTO ticket (id, event_id, user_id)
VALUES ('someticketid', 'abc', 'db7c3024-d53d-4bcf-85df-718cc5198b90')
ON CONFLICT DO NOTHING;

INSERT INTO friends (usera, userb)
VALUES ('34', '5243fwersdgerv')
ON CONFLICT DO NOTHING;
INSERT INTO friends (usera, userb)
VALUES ('12', '34')
ON CONFLICT DO NOTHING;

INSERT INTO ticketrequest (user_id, event_id, created_on)
VALUES ('12', 'abc', current_timestamp)
ON CONFLICT DO NOTHING;
DO $$ BEGIN PERFORM pg_sleep(1); END $$;

INSERT INTO ticketrequest (user_id, event_id, created_on)
VALUES ('12', 'def', current_timestamp)
ON CONFLICT DO NOTHING;
DO $$ BEGIN PERFORM pg_sleep(1); END $$;

INSERT INTO ticketrequest (user_id, event_id, created_on)
VALUES ('34', 'abc', current_timestamp)
ON CONFLICT DO NOTHING;
DO $$ BEGIN PERFORM pg_sleep(1); END $$;

INSERT INTO ticketrequest (user_id, event_id, created_on)
VALUES ('5243fwersdgerv', 'def', current_timestamp)
ON CONFLICT DO NOTHING;

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