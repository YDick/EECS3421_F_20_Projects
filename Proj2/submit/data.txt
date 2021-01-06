--Judith Dick
--jdick 215045081 
--EECS3421B Project 2 for Wenxiao Fu
--Oct 30 2020
--contact tracing

-- Add to your database at least:

-- 5 people(persons)
-- 20 recons
-- 6 tests
-- 3 test centres
-- 3 actions
-- 3 test types

--5 people
INSERT INTO person (sin, name, address, phone) VALUES
    (111111111, 'Harry Potter', 'Underneath the stairway, 4 Privet Drive', '416 111 11111'),
    (221221221, 'Yehudis Dick', '173 Glen Rush Ave., Toronto, ON, Canada, M2N 2B2', '416 781 1111'),
    (331331331, 'Katniss Everdeen', '12th District, Panem', '647 781 2345'),
    (441441441, 'Greg Overlander', 'The Overland Apartment', '416 111 2222'), 
    (551551551, 'Bilbo Baggins', 'Bags-End, Underhill, The Shire', '(000) 000 000');



INSERT INTO place (name, gps, description, address) VALUES
    ('TPL', point(2, -12), 'Barbara Frum Library', '3 Covington Dr.'),
    ('York South', point(-112, 234), 'Warmest place in canads', '432 Florida Dr.'),
    ('York North', point(111, -123), 'Best of the toronto north', '123 Polar Dr.'),
    ('York East', point(113, -73.2), 'The closest place to the middle east', '123 Duvdevani Dr, Har Nof, Jerusalem Shel Zahav');

--3 test centres
INSERT INTO test_centre (name) VALUES
    ('York South'),
    ('York North'),
    ('York East');

--3 actions
INSERT INTO action (action) VALUES
    ('death'),
    ('post-birth abortion'),
    ('cemented door');

--3 test types
INSERT INTO test_type (testtype) VALUES
    ('brain swab'),
    ('nose swab'), 
    ('rapid test');

INSERT INTO method (method) VALUES
    ('registry sign in'),
    ('registry sign out'),
    ('surveillance camera'),
    ('contact-tracing phone app');

INSERT INTO offer (testtype, testcentre) VALUES
    ('brain swab', 'York South'),
    ('nose swab', 'York North'),
    ('rapid test', 'York East');

INSERT INTO time_slot (time) VALUES
    ('2020-01-01 0:00'),
    ('2020-01-01 0:15'),
    ('2020-01-01 0:30'),
    ('2020-01-01 01:15'),
    ('2020-01-01 01:30'),

    ('2020-01-02 00:45'),
    ('2020-01-02 01:00'),
    ('2020-01-02 01:15'),
    ('2020-01-02 01:30'),
    ('2020-01-02 01:45');


--20 recon
INSERT INTO recon (method, sin, time, placename) VALUES
    ('surveillance camera', 111111111, '2020-01-02 1:15:00', 'TPL'),
    ('registry sign in', 111111111, '2020-01-01 0:00:00', 'York South'),
    ('registry sign out', 111111111, '2020-01-01 0:30', 'York South'),

    ('registry sign in', 221221221, '2020-01-01 0:00', 'York North'),
    ('registry sign out', 221221221, '2020-01-01 0:15', 'York North'),
    ('contact-tracing phone app', 221221221, '2020-01-01 0:00', 'York North'),
    ('contact-tracing phone app', 221221221, '2020-01-01 0:15', 'York North'),
    ('surveillance camera', 221221221, '2020-01-01 0:00', 'York North'),

    ('registry sign in', 221221221, '2020-01-02 0:45', 'York North'),
    ('registry sign out', 221221221, '2020-01-02 0:45', 'York North'),
    ('contact-tracing phone app', 221221221, '2020-01-02 0:45', 'York North'),
    ('surveillance camera', 221221221, '2020-01-02 0:45', 'York North'),
    
    ('registry sign in', 331331331, '2020-01-02 0:45', 'York East'),
    ('registry sign out', 331331331, '2020-01-02 1:15', 'York East'),
    ('surveillance camera', 331331331, '2020-01-02 0:45', 'York East'),
    ('contact-tracing phone app', 331331331, '2020-01-02 0:45', 'York East'),
    ('contact-tracing phone app', 331331331, '2020-01-02 1:00', 'York East'),
    ('contact-tracing phone app', 331331331, '2020-01-02 1:15', 'York East'),
    ('contact-tracing phone app', 331331331, '2020-01-02 1:30', 'TPL'),
    
    ('registry sign in', 441441441, '2020-01-02 0:45', 'York East'),
    ('registry sign out', 441441441, '2020-01-02 1:00', 'York East'),

    ('registry sign in', 551551551, '2020-01-02 1:00', 'York East'),
    ('registry sign out', 551551551, '2020-01-02 1:15', 'York East');


--6 test
INSERT INTO test (sin, time, testtype, result, testcentre) VALUES
     (111111111, '2020-01-01 00:15', 'brain swab', null, 'York South' ),

     (221221221, '2020-01-01 01:15', 'nose swab', 'cemented door', 'York North'),
     (221221221, '2020-01-02 0:45', 'nose swab', 'death', 'York North'),

     (331331331, '2020-01-02 0:45', 'rapid test', 'death', 'York East'),
     
     (441441441, '2020-01-02 0:45', 'rapid test', null, 'York East'),

     (551551551, '2020-01-02 1:00', 'rapid test', 'cemented door', 'York East');

INSERT INTO bubble(p1, p2) VALUES
    (111111111, 221221221);
