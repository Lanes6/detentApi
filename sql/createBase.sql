SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;

BEGIN;

--setup schemas
CREATE SCHEMA IF NOT EXISTS data
    AUTHORIZATION postgres;
CREATE SCHEMA IF NOT EXISTS public
    AUTHORIZATION postgres;

--Setup tables
DROP TABLE IF EXISTS data.picture CASCADE;
DROP TABLE IF EXISTS data.note CASCADE;
DROP TABLE IF EXISTS data.user CASCADE;
DROP TABLE IF EXISTS data.report CASCADE;
DROP TABLE IF EXISTS public.object CASCADE;
DROP TABLE IF EXISTS public.outline CASCADE;
DROP TABLE IF EXISTS public.banc_temp CASCADE;
DROP TABLE IF EXISTS public.pav_verre_temp CASCADE;
DROP TABLE IF EXISTS public.toilet_temp CASCADE;
DROP TABLE IF EXISTS public.trash_temp CASCADE;
DROP TABLE IF EXISTS public.tree_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp2 CASCADE;

DROP VIEW IF EXISTS public.banc_view;
DROP VIEW IF EXISTS public.toilet_view;
DROP VIEW IF EXISTS public.pav_verre_view;
DROP VIEW IF EXISTS public.trash_view;
DROP VIEW IF EXISTS public.tree_view;

DROP VIEW IF EXISTS public.banc_suggestion_view;
DROP VIEW IF EXISTS public.toilet_suggestion_view;
DROP VIEW IF EXISTS public.pav_verre_suggestion_view;
DROP VIEW IF EXISTS public.trash_suggestion_view;
DROP VIEW IF EXISTS public.tree_suggestion_view;

DROP VIEW IF EXISTS public.banc_reported_view;
DROP VIEW IF EXISTS public.toilet_reported_view;
DROP VIEW IF EXISTS public.pav_verre_reported_view;
DROP VIEW IF EXISTS public.trash_reported_view;
DROP VIEW IF EXISTS public.tree_reported_view;

DROP VIEW IF EXISTS public.outline_aglo_view;

CREATE TABLE data.picture
(
    id_picture serial NOT NULL,
    id_user bigint NOT NULL,
    id_object bigint NOT NULL,
    saison text,
    file bit[] NOT NULL,
    PRIMARY KEY (id_picture)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE data.picture
    OWNER to postgres;

CREATE TABLE data.report
(
    id_report serial NOT NULL,
    id_user bigint NOT NULL,
    id_object bigint NOT NULL,
    description text,
    PRIMARY KEY (id_report)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE data.report
    OWNER to postgres;

CREATE TABLE data.user
(
    id_user serial NOT NULL,
    login text NOT NULL,
    mail text NOT NULL,
    hash_mdp text NOT NULL,
    secret_token text,
    PRIMARY KEY (id_user)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE data.user
    OWNER to postgres;

CREATE TABLE data.note
(
    id_note serial NOT NULL,
    id_user bigint NOT NULL,
    id_object bigint NOT NULL,
    note int,
    PRIMARY KEY (id_note)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE data.note
    OWNER to postgres;

CREATE TABLE public.object
(
    id_object serial NOT NULL,
    id_user bigint,
    type text NOT NULL,
    description text,
    PRIMARY KEY (id_object)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE public.object
    OWNER to postgres;
CREATE TABLE public.outline
(
    id_outline serial NOT NULL,
    type text NOT NULL,
    description text,
    PRIMARY KEY (id_outline)
)
WITH (
    OIDS = FALSE
);
ALTER TABLE public.outline
    OWNER to postgres;
SELECT AddGeometryColumn('','object','geom','2154','POINT',2);
SELECT AddGeometryColumn('','outline','geom','2154','POLYGON',2);




--Setup foreign key
ALTER TABLE data.note DROP CONSTRAINT IF EXISTS "noteFKid_user";
ALTER TABLE data.note DROP CONSTRAINT IF EXISTS "noteFKid_object";
ALTER TABLE data.report DROP CONSTRAINT IF EXISTS "reportFKid_user";
ALTER TABLE data.report DROP CONSTRAINT IF EXISTS "reportFKid_object";
ALTER TABLE data.picture DROP CONSTRAINT IF EXISTS "pictureFKid_user";
ALTER TABLE data.picture DROP CONSTRAINT IF EXISTS "pictureFKid_object";
ALTER TABLE public.object DROP CONSTRAINT IF EXISTS "objectFKid_user";

ALTER TABLE data.note
    ADD CONSTRAINT "noteFKid_user" FOREIGN KEY (id_user)
    REFERENCES data."user" (id_user) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;

ALTER TABLE data.note
    ADD CONSTRAINT "noteFKid_object" FOREIGN KEY (id_object)
    REFERENCES public.object (id_object) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;

ALTER TABLE data.report
    ADD CONSTRAINT "reportFKid_user" FOREIGN KEY (id_user)
    REFERENCES data."user" (id_user) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;

ALTER TABLE data.report
    ADD CONSTRAINT "reportFKid_object" FOREIGN KEY (id_object)
    REFERENCES public.object (id_object) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;

ALTER TABLE data.picture
    ADD CONSTRAINT "pictureFKid_user" FOREIGN KEY (id_user)
    REFERENCES data."user" (id_user) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION
    NOT VALID;

ALTER TABLE data.picture
    ADD CONSTRAINT "pictureFKid_object" FOREIGN KEY (id_object)
    REFERENCES public.object (id_object) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;

ALTER TABLE public.object
    ADD CONSTRAINT "objectFKid_user" FOREIGN KEY (id_user)
    REFERENCES data."user" (id_user) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION
    NOT VALID;






--Views objects open data Orleans without reports
CREATE OR REPLACE VIEW public.banc_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object o
WHERE type='banc'
AND NOT EXISTS ( SELECT id_object FROM data.report r WHERE o.id_object = r.id_object);

ALTER TABLE public.banc_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.toilet_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object o
WHERE type='toilet'
AND NOT EXISTS ( SELECT id_object FROM data.report r WHERE o.id_object = r.id_object);

ALTER TABLE public.toilet_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.pav_verre_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object o
WHERE type='pav_verre'
AND NOT EXISTS ( SELECT id_object FROM data.report r WHERE o.id_object = r.id_object);

ALTER TABLE public.pav_verre_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.trash_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object o
WHERE type='trash'
AND NOT EXISTS ( SELECT id_object FROM data.report r WHERE o.id_object = r.id_object);

ALTER TABLE public.trash_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.tree_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object o
WHERE type='tree'
AND NOT EXISTS ( SELECT id_object FROM data.report r WHERE o.id_object = r.id_object);

ALTER TABLE public.tree_view
    OWNER TO postgres;

--Views objects suggestions
CREATE OR REPLACE VIEW public.banc_suggestion_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='banc_suggestion';

ALTER TABLE public.banc_suggestion_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.toilet_suggestion_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='toilet_suggestion';

ALTER TABLE public.toilet_suggestion_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.pav_verre_suggestion_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom 
FROM public.object 
WHERE type='pav_verre_suggestion';

ALTER TABLE public.pav_verre_suggestion_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.trash_suggestion_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='trash_suggestion';

ALTER TABLE public.trash_suggestion_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.tree_suggestion_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='tree_suggestion';

ALTER TABLE public.tree_suggestion_view
    OWNER TO postgres;

--Views objects with reports
CREATE OR REPLACE VIEW public.banc_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object
WHERE type='banc_report';

ALTER TABLE public.banc_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.toilet_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object
WHERE type='toilet_report';

ALTER TABLE public.toilet_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.pav_verre_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object
WHERE type='pav_verre_report';

ALTER TABLE public.pav_verre_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.trash_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object
WHERE type='trash_report';

ALTER TABLE public.trash_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.tree_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object
WHERE type='tree_report';

ALTER TABLE public.tree_reported_view
    OWNER TO postgres;

--View orleans agglo outline
CREATE OR REPLACE VIEW public.outline_aglo_view
WITH (
  check_option=cascaded
) AS
SELECT id_outline, type, description, geom
FROM public.outline 
WHERE type='contour_agglo';

ALTER TABLE public.outline_aglo_view
    OWNER TO postgres;
    
COMMIT;



