SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;

BEGIN;
--Merge of orleans neighborhoods
CREATE TABLE public.outline_aglo_temp2 AS SELECT outline_aglo_temp."code_dept", ST_Union(outline_aglo_temp.geom) :: geometry(Polygon,2154) AS geom 
  FROM outline_aglo_temp AS outline_aglo_temp 
  GROUP BY outline_aglo_temp."code_dept";
--Insert outline of agglo Orleans
INSERT INTO public.outline (type, description, geom)
SELECT 'contour_agglo', 'Contour de l aglomération d Orleans', geom FROM public.outline_aglo_temp2;

--Insert toilet
INSERT INTO public.object (type, description, geom) 
SELECT 'toilet', concat('Horaires: ',san_horaire, '.\nEquipement adapté aux situations de handicap: ', san_handi), geom FROM public.toilet_temp;
--Insert banc
INSERT INTO public.object (type, description, geom)
SELECT 'banc', banc_detail, geom FROM public.banc_temp;
--Insert pav_verre
INSERT INTO public.object (type, description, geom)
SELECT 'pav_verre', domanialit, geom FROM public.pav_verre_temp;
--Insert trash
INSERT INTO public.object (type, description, geom)
SELECT 'trash', concat('Type de poubelle: ',corbeille_t, '.\nRamassage: ', corbeille_f), geom FROM public.trash_temp;
--Insert tree
INSERT INTO public.object (type, description, geom)
SELECT 'tree', concat(espece,' ',variete, ' ', type), geom FROM public.tree_temp;

--Drop temp tables
DROP TABLE IF EXISTS public.banc_temp CASCADE;
DROP TABLE IF EXISTS public.pav_verre_temp CASCADE;
DROP TABLE IF EXISTS public.toilet_temp CASCADE;
DROP TABLE IF EXISTS public.trash_temp CASCADE;
DROP TABLE IF EXISTS public.tree_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp2 CASCADE;

--Delete object not in agglo Orleans
DELETE FROM public.object obj WHERE 
obj.id_object NOT IN (
    SELECT obj.id_object FROM
      public.object obj, public.outline outline
      WHERE ST_Contains(outline.geom, obj.geom)
    AND outline.type='contour_agglo'
 );

COMMIT;