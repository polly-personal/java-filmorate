create TABLE IF NOT EXISTS PUBLIC."users"(
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	email VARCHAR(255) NOT NULL,
	login VARCHAR(255) NOT NULL,
	name VARCHAR(255),
	birthday DATE NOT NULL
);

create TABLE IF NOT EXISTS PUBLIC."friendships"(
	user_id BIGINT NOT NULL REFERENCES PUBLIC."users"(id),
	friend_id BIGINT NOT NULL REFERENCES PUBLIC."users"(id),
	is_approved BOOLEAN,
	PRIMARY KEY (user_id, friend_id)
);


create TABLE IF NOT EXISTS PUBLIC."mpa" (
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          VARCHAR(10) NOT NULL UNIQUE
);


create TABLE IF NOT EXISTS PUBLIC."films"(
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(200),
	release_date DATE NOT NULL,
	duration INTEGER NOT NULL,
	rate BIGINT,
	likes BIGINT,
	mpa_id INTEGER REFERENCES PUBLIC."mpa"(id)
);

create TABLE IF NOT EXISTS PUBLIC."likes"(
	film_id BIGINT NOT NULL REFERENCES PUBLIC."films"(id),
	user_id BIGINT NOT NULL REFERENCES PUBLIC."users"(id),
    PRIMARY KEY (film_id, user_id)
);


create TABLE IF NOT EXISTS PUBLIC."genres"(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL UNIQUE
);

create TABLE IF NOT EXISTS PUBLIC."film_genres"(
	film_id BIGINT NOT NULL REFERENCES PUBLIC."films"(id) ON delete CASCADE,
	genre_id INTEGER NOT NULL REFERENCES PUBLIC."genres"(id),
    PRIMARY KEY (film_id, genre_id)
);
