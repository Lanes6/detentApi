SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;

BEGIN;

--Insert some test data
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (10001, 'Alice', 'alice@mail.net', 'hash_mdp', 'secret_token');
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (10002, 'Bob', 'bob@mail.net', 'hash_mdp', 'secret_token');
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (10003, 'Eve', 'eve@mail.net', 'hash_mdp', 'secret_token');

INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (100001, 10001, 'banc_suggestion', 'Cet endroit me paraît bien pour un banc', '01010000206A0800004318EACD3A61FE3FDA3EEE8154F54740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (100002, 10001, 'tree_suggestion', 'Cet endroit me paraît bien pour un arbre', '01010000206A080000DA1E56EB44E3FE3FC439C079AAE94740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (100003, 10002, 'pav_verre_suggestion', 'Cet endroit me paraît bien pour un dépôt de verre', '01010000206A080000DD946867838DFE3F1EF2FEEE30F44740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (100004, 10002, 'trash_suggestion', 'Cet endroit me paraît bien pour une corbeille', '01010000206A080000E633824EE4B1FE3FB050606E52F44740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (100005, 10003, 'toilet_suggestion', 'Cet endroit me paraît bien pour des toilettes', '01010000206A080000F6302528F447FE3F79A6B7AC91F34740');

INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10001, 10002, 100001, 1);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10002, 10003, 100001, 3);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10003, 10003, 100002, 3);
    INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10004, 10002, 100002, 1);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10005, 10001, 100003, 5);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10006, 10003, 100003, 3);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10007, 10001, 100004, 5);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10008, 10003, 100004, 3);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10009, 10002, 100005, 1);
INSERT INTO data.note(
        id_note, id_user, id_object, note)
    VALUES (10010, 10001, 100005, 5);

INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (10001, 10, 'Cet objet nécessite une réparation');
UPDATE public.object 
    SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,'tree','tree_report'),'banc','banc_report'),'trash','trash_report'),'pav_verre','pav_verre_report'),'toilet','toilet_report') 
    WHERE id_object = 10;
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (10001, 50, 'Cet objet nécessite une réparation');
UPDATE public.object 
    SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,'tree','tree_report'),'banc','banc_report'),'trash','trash_report'),'pav_verre','pav_verre_report'),'toilet','toilet_report') 
    WHERE id_object = 50;
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (10002, 1800, 'Cet objet nécessite une réparation');
UPDATE public.object 
    SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,'tree','tree_report'),'banc','banc_report'),'trash','trash_report'),'pav_verre','pav_verre_report'),'toilet','toilet_report') 
    WHERE id_object = 1800;
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (10002, 2200, 'Cet objet nécessite une réparation');
UPDATE public.object 
    SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,'tree','tree_report'),'banc','banc_report'),'trash','trash_report'),'pav_verre','pav_verre_report'),'toilet','toilet_report') 
    WHERE id_object = 2200;
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (10003, 4000, 'Cet objet nécessite une réparation');
UPDATE public.object 
    SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,'tree','tree_report'),'banc','banc_report'),'trash','trash_report'),'pav_verre','pav_verre_report'),'toilet','toilet_report') 
    WHERE id_object = 4000;


--ALTER SEQUENCE public.object_id_object_seq RESTART WITH 10;
--ALTER SEQUENCE data.note_id_note_seq RESTART WITH 10;
--ALTER SEQUENCE data.user_id_user_seq RESTART WITH 10;
--post fillTestData
/*DELETE FROM public.toilet_temp
  WHERE gid>3;
INSERT INTO public.toilet_temp(
  id, san_gest, san_horaire, san_loc, san_sect, san_remarq, san_handi, san_num, codcomm, geom)
  VALUES (300, '1', '1', '1', '1', '1', '1', 1, '1', '01010000206A080000087FE466C58DFE3F8E71034E0FF34741');

UPDATE public.toilet_temp
  SET san_horaire='truc';
*/

COMMIT;