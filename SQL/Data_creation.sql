INSERT INTO "user" (id, firstName, lastName, email, nick_name, birth_date, salt, password)
VALUES ('5243fwersdgerv', 'Hans', 'Peter', 'peter@gmail.com', 'Milfhunter69', '2004-04-04', 'gerwgerg', 'fgwefwef');

INSERT INTO userlocation (user_id, latitude, longitude, created_on)
VALUES ('5243fwersdgerv', 0.69, -1.87, current_timestamp);

INSERT INTO event (id, name, description, start, "end", max_people)
VALUES ('abc', 'Fette party', 'SAUFEEEEEn', '2025-04-04', '2025-04-05', 187);

INSERT INTO eventlocation (event_id, latitude, longitude, created_on)
VALUES ('abc', 0.69, -1.87, current_timestamp);

INSERT INTO pictures (event_id, url)
VALUES ('abc', 'someurl.com/penis');

INSERT INTO ticket (id, event_id, user_id)
VALUES ('peter', 'abc', '5243fwersdgerv');

