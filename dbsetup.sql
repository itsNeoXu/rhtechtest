CREATE DATABASE rhtechtest;
USE rhtechtest;

CREATE TABLE properties
(
	id				INT unsigned NOT NULL AUTO_INCREMENT,
    name 			VARCHAR(150) NOT NULL,
    location		VARCHAR(150) NOT NULL,
    type			VARCHAR(150) NOT NULL,
    PRIMARY KEY		(id)
);

CREATE TABLE bookings
(
	id				INT unsigned NOT NULL AUTO_INCREMENT,
    property_id		INT unsigned NOT NULL,
    check_in		DATE NOT NULL,
    check_out		DATE NOT NULL,
    PRIMARY KEY		(id),
    FOREIGN KEY		(property_id) REFERENCES properties(id)
);

INSERT INTO properties (id, name, location, type) VALUES
(1, 'The Greenhouse Loft', 'London', 'Apartment'),
(2, 'CityView Studios', 'Manchester', 'Studio'),
(3, 'Riverside House', 'Birmingham', 'House'),
(4, 'The Docklands Retreat', 'London', 'Apartment'),
(5, 'York Street Flats', 'Leeds', 'Apartment');

INSERT INTO bookings (id, property_id, check_in, check_out) VALUES
(1, 1, '2025-03-18', '2025-03-21'),
(2, 3, '2025-03-20', '2025-03-23');