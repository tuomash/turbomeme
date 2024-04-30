CREATE TABLE memes
(
  id serial PRIMARY KEY,
  data text NOT NULL,
  datastorage_id integer NOT NULL,
  canvas_width integer NOT NULL,
  canvas_height integer NOT NULL,
  created timestamp with time zone NOT NULL,
  hash character varying(9) NOT NULL,
  meme_id integer NOT NULL
)
