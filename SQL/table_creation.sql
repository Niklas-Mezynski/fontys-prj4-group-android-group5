DROP TABLE IF EXISTS ticket CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "event" CASCADE;
DROP TABLE IF EXISTS userlocation CASCADE;
DROP TABLE IF EXISTS eventlocation CASCADE;
DROP TABLE IF EXISTS pictures CASCADE;
DROP TABLE IF EXISTS friends CASCADE;


CREATE TABLE ticket
(
    id       varchar(256) NOT NULL,
    event_id varchar(128) NOT NULL,
    user_id  varchar(128) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE "user"
(
    id          varchar(128) NOT NULL,
    firstName   varchar(40)  NOT NULL,
    lastName    varchar(40)  NOT NULL,
    email       varchar(320) NOT NULL UNIQUE,
    nick_name   varchar(40)  NOT NULL UNIQUE,
    birth_date  date         NOT NULL,
    profile_pic varchar(1024),
    about_me    varchar(2048),
    salt        varchar(256) NOT NULL,
    password    varchar(256) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE event
(
    id          varchar(128)  NOT NULL,
    name        varchar(80)   NOT NULL,
    description varchar(2048) NOT NULL,
--     pictures    integer,
    "start"     timestamp     NOT NULL,
    "end"       timestamp,
    max_people  integer       NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE userlocation
(
    user_id    varchar(128)     NOT NULL,
    latitude   double precision NOT NULL,
    longitude  double precision NOT NULL,
    created_on timestamp        NOT NULL,
    PRIMARY KEY (user_id)
);
CREATE TABLE eventlocation
(
    event_id   varchar(128)     NOT NULL,
    latitude   double precision NOT NULL,
    longitude  double precision NOT NULL,
    created_on timestamp        NOT NULL,
    PRIMARY KEY (event_id)
);
CREATE TABLE pictures
(
    event_id varchar(128)  NOT NULL,
    url      varchar(1024) NOT NULL,
    PRIMARY KEY (event_id, url)
);
CREATE TABLE friends
(
    userA varchar(128) NOT NULL,
    userB varchar(128) NOT NULL,
    PRIMARY KEY (userA, userB)
);
ALTER TABLE pictures
    ADD CONSTRAINT FKPictures14710 FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE;
ALTER TABLE userlocation
    ADD CONSTRAINT FKUserLocation586969 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;
ALTER TABLE eventlocation
    ADD CONSTRAINT FKEventLocation894025 FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE;
ALTER TABLE ticket
    ADD CONSTRAINT FKTicket579021 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;
ALTER TABLE ticket
    ADD CONSTRAINT FKTicket634115 FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE;
ALTER TABLE friends
    ADD CONSTRAINT FKFriends788075 FOREIGN KEY (userA) REFERENCES "user" (id) ON DELETE CASCADE;
ALTER TABLE friends
    ADD CONSTRAINT FKFriends788076 FOREIGN KEY (userB) REFERENCES "user" (id) ON DELETE CASCADE;


-- Check constraints
ALTER TABLE "user"
    ADD CONSTRAINT Check_age_over_18 CHECK ( date_part('days', (CURRENT_TIMESTAMP - birth_date::timestamp)) >=
                                             18 * 365 );

ALTER TABLE event
    ADD CONSTRAINT event_start_in_future CHECK ( "start" > CURRENT_TIMESTAMP );



-- Custom function to get all friends from the database
CREATE OR REPLACE function getFriendInfos(user_id varchar)
RETURNS TABLE ( friend_id varchar, nick_name varchar)
language plpgsql
as
$$
Declare

Begin
   return QUERY SELECT DISTINCT "user".id as freind_id, "user".nick_name
FROM ((SELECT f.usera as friendId
 FROM friends f
 where (userb = user_id))
UNION
(SELECT f.userb as friendId
 FROM friends f
 where (usera = user_id))) AS friends
INNER JOIN "user" ON "user".id = friends.friendId;
End;
$$;