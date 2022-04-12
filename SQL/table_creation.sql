DROP TABLE IF EXISTS ticket CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "event" CASCADE;
DROP TABLE IF EXISTS userlocation CASCADE;
DROP TABLE IF EXISTS eventlocation CASCADE;
DROP TABLE IF EXISTS pictures CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS requests CASCADE;


-- Create tables
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
    password    varchar(256) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE event
(
    id          varchar(128)  NOT NULL,
    user_id     varchar(128)  NOT NULL,
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
    created_on timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);
CREATE TABLE eventlocation
(
    event_id   varchar(128)     NOT NULL,
    latitude   double precision NOT NULL,
    longitude  double precision NOT NULL,
    created_on timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
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


CREATE TABLE requests
(
    user_id    varchar(128) NOT NULL,
    event_id   varchar(128) NOT NULL,
    created_on timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, created_on)
);


-- Create relations
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
ALTER TABLE requests
    ADD CONSTRAINT FKRequests34535 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;
ALTER TABLE requests
    ADD CONSTRAINT FKRequests45367 FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE;
ALTER TABLE event
    ADD CONSTRAINT FKEvent89432 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;


-- Check constraints
ALTER TABLE "user"
    ADD CONSTRAINT Check_age_over_18 CHECK ( date_part('days', (CURRENT_TIMESTAMP - birth_date::timestamp)) >=
                                             18 * 365 );

ALTER TABLE event
    ADD CONSTRAINT event_start_in_future CHECK ( "start" > CURRENT_TIMESTAMP );


-- Custom function to get all friends from the database
CREATE OR REPLACE function getFriendInfos(user_id varchar)
    RETURNS TABLE
            (
                friend_id varchar,
                nick_name varchar
            )
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

CREATE OR REPLACE function getNearbyEvents(user_lat double precision, user_lon double precision,
                                           radiusInKm integer)
    RETURNS TABLE
            (
                id          varchar(128),
                user_id     varchar(128),
                name        varchar(80),
                description varchar(2048),
                "start"     timestamp,
                "end"       timestamp,
                max_people  integer,
                latitude    double precision,
                longitude   double precision
            )
    language plpgsql
as
$$
Declare

Begin

    return QUERY SELECT event.id, event.user_id, event.name, event.description, event.start, event."end", event.max_people, e.latitude, e.longitude
    FROM event
             inner join eventlocation e on event.id = e.event_id
    WHERE (sqrt((111.3 * cos((e.latitude + user_lat) / 2 * 0.01745) * (e.longitude - user_lon)) *
                (111.3 * cos((e.latitude + user_lat) / 2 * 0.01745) * (e.longitude - user_lon)) +
                (111.3 * (e.latitude - user_lat)) * (111.3 * (e.latitude - user_lat)))) <= radiusInKm;
End;
$$;

-- SELECT * FROM getNearbyEvents(0.69, -1.87, 50);

