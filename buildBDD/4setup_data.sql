BEGIN;

DELETE FROM public.object;

INSERT INTO public.object (type, description, geom)
SELECT 'banc', banc_detail, geom FROM public.banc_temp;

INSERT INTO public.object (type, description, geom)
SELECT 'pav_verre', domanialit, geom FROM public.pav_verre_temp;

INSERT INTO public.object (type, description, geom)
SELECT 'toilet', concat('Horaires: ',san_horaire, '. Equipement adapté aux situations de handicap: ', san_handi), geom FROM public.toilet_temp;

INSERT INTO public.object (type, description, geom)
SELECT 'trash', concat('Type de poubelle: ',corbeille_t, '. Ramassage ', corbeille_f), geom FROM public.trash_temp;

INSERT INTO public.object (type, description, geom)
SELECT 'tree', concat(espece,' ',variete, '', type), geom FROM public.tree_temp;

CREATE TABLE public.temp_outline AS SELECT contours_communes."code_dept", ST_Union(contours_communes.geom) :: geometry(Polygon,2154) AS geom 
	FROM contours_communes AS contours_communes 
	GROUP BY contours_communes."code_dept";

INSERT INTO public.outline (type, description, geom)
SELECT 'contour_agglo', 'Contour de l aglomération d Orleans', geom FROM public.temp_outline;

COMMIT;

BEGIN;

DROP TABLE IF EXISTS public.banc_temp CASCADE;
DROP TABLE IF EXISTS public.pav_verre_temp CASCADE;
DROP TABLE IF EXISTS public.toilet_temp CASCADE;
DROP TABLE IF EXISTS public.trash_temp CASCADE;
DROP TABLE IF EXISTS public.tree_temp CASCADE;

DROP TABLE IF EXISTS public.contours_communes CASCADE;
DROP TABLE IF EXISTS public.temp_outline CASCADE;

COMMIT;
BEGIN;

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

CREATE OR REPLACE VIEW public.banc_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object 
WHERE type='banc';

ALTER TABLE public.banc_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.toilet_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object 
WHERE type='toilet';

ALTER TABLE public.toilet_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.pav_verre_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object 
WHERE type='pav_verre';

ALTER TABLE public.pav_verre_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.trash_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object 
WHERE type='trash';

ALTER TABLE public.trash_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.tree_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, type, description, geom
FROM public.object 
WHERE type='tree';

ALTER TABLE public.tree_view
    OWNER TO postgres;

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

CREATE OR REPLACE VIEW public.banc_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='banc_reported';

ALTER TABLE public.banc_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.toilet_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='toilet_reported';

ALTER TABLE public.toilet_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.pav_verre_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='pav_verre_reported';

ALTER TABLE public.pav_verre_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.trash_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='trash_reported';

ALTER TABLE public.trash_reported_view
    OWNER TO postgres;

CREATE OR REPLACE VIEW public.tree_reported_view
WITH (
  check_option=cascaded
) AS
SELECT id_object, id_user, type, description, geom
FROM public.object 
WHERE type='tree_reported';

ALTER TABLE public.tree_view
    OWNER TO postgres;

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


