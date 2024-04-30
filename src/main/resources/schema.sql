CREATE TABLE memes
(
  data text NOT NULL,
  datastorage_id integer NOT NULL,
  canvas_width integer NOT NULL,
  canvas_height integer NOT NULL,
  created timestamp with time zone NOT NULL,
  hash character varying(7) NOT NULL,
  id bigint NOT NULL,
  meme_id integer NOT NULL,
  CONSTRAINT meme_pkey PRIMARY KEY (id)
)
