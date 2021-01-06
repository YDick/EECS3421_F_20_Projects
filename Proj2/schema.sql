--Judith Dick
--jdick 215045081 
--EECS3421B Project 2 for Wenxiao Fu
--Oct 30 2020
--contact tracing


CREATE table Person (
    "sin"   NUMERIC(9),
    name    varchar(20),
    address varchar(50),
    phone   varchar(20) NOT NULL,
    CONSTRAINT sin_pk
        PRIMARY KEY ("sin")
);

CREATE table bubble (
    p1 NUMERIC(9),
    p2 NUMERIC(9),
    CONSTRAINT p1_p2_pk
        PRIMARY KEY (p1, p2),
    CONSTRAINT p1_fk
        FOREIGN KEY (p1) REFERENCES Person,
    CONSTRAINT p2_fk
        FOREIGN KEY (p2) REFERENCES Person
);


CREATE TABLE Time_slot (
    time  TIMESTAMP,
    CONSTRAINT time_pk
        PRIMARY KEY (time)
    --CONSTRAINT time_interval 
);


CREATE TABLE Test_type (
    testtype  varchar(50),
    CONSTRAINT test_type_pk
        PRIMARY KEY (testtype)
);

CREATE TABLE Action (
    action  varchar(50),
    CONSTRAINT action_pk
        PRIMARY KEY (action)
);


CREATE TABLE Method (
    method  varchar(50),
    CONSTRAINT method_pk
        PRIMARY KEY (method)
);


CREATE TABLE Place (
    name            varchar(20),
    gps             point,
    description     varchar(255),
    address         varchar(255) NOT NULL,
    CONSTRAINT place_pk
        PRIMARY KEY (name)
);

CREATE TABLE Test_centre (
    --isa Place
    name   varchar(20),

    CONSTRAINT test_centre_pk
        PRIMARY KEY (name),
    CONSTRAINT test_center_fk
        FOREIGN KEY (name) REFERENCES place (name)
);

CREATE TABLE offer (
    --many-many relation table
    testtype    VARCHAR(50),
    testcentre  VARCHAR(20),
    CONSTRAINT offer_pk
        PRIMARY KEY (testtype, testcentre),
    CONSTRAINT test_type_fk
        FOREIGN KEY (testtype) REFERENCES test_type,
    CONSTRAINT test_centre_fk
        FOREIGN KEY (testcentre) REFERENCES Test_centre
);


CREATE TABLE Test (
    sin   NUMERIC(9) NOT NULL, --weak on person
    time    TIMESTAMP NOT NULL, --week on time slot
    testtype      varchar(50) NOT NULL,
    result  varchar(50),
    testcentre      varchar(20),
    CONSTRAINT test_pk
        PRIMARY KEY (sin, time),
    CONSTRAINT takes_fk
        FOREIGN KEY (sin) REFERENCES Person,
    CONSTRAINT upon_fk
        FOREIGN KEY (time) REFERENCES Time_slot,
    CONSTRAINT of_type_fk
        FOREIGN KEY (testtype) REFERENCES Test_type,
    CONSTRAINT result_fk
        FOREIGN KEY (result) REFERENCES Action,
    CONSTRAINT in_fk
        FOREIGN KEY (testcentre) REFERENCES Test_centre
);


CREATE TABLE recon (
    method     VARCHAR(50),
    placename      VARCHAR(20),
    time  TIMESTAMP,
    sin     NUMERIC(9),
    CONSTRAINT recon_pk
        PRIMARY KEY (method, sin, placename, time),
    CONSTRAINT how_fk
        FOREIGN KEY (method) REFERENCES method,
    CONSTRAINT at_fk
        FOREIGN KEY (placename) REFERENCES Place,
    CONSTRAINT when_fk
        FOREIGN KEY (time) REFERENCES Time_slot,
    CONSTRAINT who_fk
        FOREIGN KEY (sin) REFERENCES Person
);
