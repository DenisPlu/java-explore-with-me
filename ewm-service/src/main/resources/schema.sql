CREATE TABLE IF NOT EXISTS users
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  varchar(120) NOT NULL,
    email varchar(120) NOT NULL,
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(120),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title              varchar(120),
    description        varchar(7000),
    annotation         varchar(2000),
    state              varchar(100),
    category_id        int REFERENCES categories (id),
    created_on         TIMESTAMP,
    event_date         TIMESTAMP,
    published_on       TIMESTAMP,
    confirmed_requests int,
    location_id        bigint,
    initiator_id       bigint REFERENCES users (id),
    paid               boolean,
    participant_limit  int,
    request_moderation boolean
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title  varchar(120),
    pinned varchar(120)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    id             bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    compilation_id bigint REFERENCES compilations (id),
    event_id       bigint REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS participation_request
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP,
    event_id     bigint REFERENCES events (id),
    requester_id bigint REFERENCES users (id),
    status       varchar(100)
);

CREATE TABLE IF NOT EXISTS location
(
    id  bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat float,
    lon float
);

CREATE TABLE IF NOT EXISTS comments
(
    id       bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text     varchar(2000),
    created  TIMESTAMP,
    updated  TIMESTAMP,
    event_id bigint REFERENCES events (id),
    user_id  bigint REFERENCES users (id),
    status   varchar(50)
);