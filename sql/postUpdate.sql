SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;

BEGIN;

--Clear outline of agglo Orleans
DELETE FROM public.outline
WHERE type='contour_agglo';
--Merge of orleans neighborhoods
CREATE TABLE public.outline_aglo_temp2 AS SELECT outline_aglo_temp."code_dept", ST_Union(outline_aglo_temp.geom) :: geometry(Polygon,2154) AS geom 
  FROM outline_aglo_temp AS outline_aglo_temp 
  GROUP BY outline_aglo_temp."code_dept";
--Insert outline of agglo Orleans
INSERT INTO public.outline (type, description, geom)
SELECT 'contour_agglo', 'Contour de l aglomération d Orleans', geom FROM public.outline_aglo_temp2;

--add column with boolean
ALTER TABLE public.object
    ADD COLUMN update boolean;
UPDATE public.object
  SET update=false;
--kepp suggestions
UPDATE public.object
  SET update=true
  WHERE type!='banc'
  AND type!='tree'
  AND type!='trash'
  AND type!='pav_verre'
  AND type!='toilet';

--update toilet from open data Orleans
UPDATE public.object as obj
SET description=concat('Horaires: ',toilet_temp.san_horaire, '.\nEquipement adapté aux situations de handicap: ', toilet_temp.san_handi), update=true
FROM toilet_temp
WHERE obj.type='toilet' AND toilet_temp.geom = obj.geom;
--update tree from open data Orleans
UPDATE public.object as obj
SET description=concat(tree_temp.espece,' ',tree_temp.variete, ' ', tree_temp.type), update=true
FROM tree_temp
WHERE obj.type='tree' AND tree_temp.geom = obj.geom;
--update trash from open data Orleans
UPDATE public.object as obj
SET description=concat('Type de poubelle: ',trash_temp.corbeille_t, '.\nRamassage: ',trash_temp.corbeille_f), update=true
FROM trash_temp
WHERE obj.type='trash' AND trash_temp.geom = obj.geom;
--update banc from open data Orleans
UPDATE public.object as obj
SET description=banc_temp.banc_detail, update=true
FROM banc_temp
WHERE obj.type='banc' AND banc_temp.geom = obj.geom;
--update pav_verre from open data Orleans
UPDATE public.object as obj
SET description=pav_verre_temp.domanialit, update=true
FROM pav_verre_temp
WHERE obj.type='pav_verre' AND pav_verre_temp.geom = obj.geom;

--Delete object deleted in open data Orleans
DELETE FROM public.object obj
WHERE update=false;

--Delete temp column
ALTER TABLE public.object DROP COLUMN update;

--Create toilet new in open data Orleans
INSERT INTO public.object (type, description, geom) 
SELECT 'toilet', concat('Horaires: ',san_horaire, '.\nEquipement adapté aux situations de handicap: ', san_handi), geom
FROM public.toilet_temp t
WHERE
    NOT EXISTS (
        SELECT geom FROM public.object o WHERE o.geom = t.geom
    );
--Create tree new in open data Orleans
INSERT INTO public.object (type, description, geom) 
SELECT 'tree', concat(espece,' ',variete, ' ', type), geom
FROM public.tree_temp t
WHERE
    NOT EXISTS (
        SELECT geom FROM public.object o WHERE o.geom = t.geom
    );
--Create trash new in open data Orleans
INSERT INTO public.object (type, description, geom) 
SELECT 'trash', concat('Type de poubelle: ',corbeille_t, '.\nRamassage ', corbeille_f), geom
FROM public.trash_temp t
WHERE
    NOT EXISTS (
        SELECT geom FROM public.object o WHERE o.geom = t.geom
    );
--Create banc new in open data Orleans
INSERT INTO public.object (type, description, geom) 
SELECT 'banc', banc_detail, geom
FROM public.banc_temp t
WHERE
    NOT EXISTS (
        SELECT geom FROM public.object o WHERE o.geom = t.geom
    );
--Create pav_verre new in open data Orleans
INSERT INTO public.object (type, description, geom) 
SELECT 'pav_verre', domanialit, geom
FROM public.pav_verre_temp t
WHERE
    NOT EXISTS (
        SELECT geom FROM public.object o WHERE o.geom = t.geom
    );

--Delete object not in agglo Orleans
DELETE FROM public.object obj WHERE 
obj.id_object NOT IN (
    SELECT obj.id_object FROM
      public.object obj, public.outline outline
      WHERE ST_Contains(outline.geom, obj.geom)
    AND outline.type='contour_agglo'
 );

--Drop temp tables
DROP TABLE IF EXISTS public.banc_temp CASCADE;
DROP TABLE IF EXISTS public.pav_verre_temp CASCADE;
DROP TABLE IF EXISTS public.toilet_temp CASCADE;
DROP TABLE IF EXISTS public.trash_temp CASCADE;
DROP TABLE IF EXISTS public.tree_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp CASCADE;
DROP TABLE IF EXISTS public.outline_aglo_temp2 CASCADE;

COMMIT;