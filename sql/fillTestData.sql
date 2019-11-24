SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;

BEGIN;

--Insert some test data
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (1, 'test1', 'test1@mail.net', 'hash_mdp', 'secret_token');
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (2, 'test2', 'test2@mail.net', 'hash_mdp', 'secret_token');
INSERT INTO data."user"(
    id_user, login, mail, hash_mdp, secret_token)
    VALUES (3, 'test3', 'test3@mail.net', 'hash_mdp', 'secret_token');

INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (1, 1, 'banc_suggestion', 'Sa peut être bien!', '01010000206A0800004318EACD3A61FE3FDA3EEE8154F54740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (2, 1, 'tree_suggestion', 'Sa peut être bien!', '01010000206A080000DA1E56EB44E3FE3FC439C079AAE94740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (3, 2, 'pav_verre_suggestion', 'Sa peut être bien!', '01010000206A080000DD946867838DFE3F1EF2FEEE30F44740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (4, 2, 'trash_suggestion', 'Sa peut être bien!', '01010000206A080000E633824EE4B1FE3FB050606E52F44740');
INSERT INTO public.object(
    id_object, id_user, type, description, geom)
    VALUES (5, 3, 'toilet_suggestion', 'Sa peut être bien!', '01010000206A080000F6302528F447FE3F79A6B7AC91F34740');

INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (1, 3, 5);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (1, 4, 5);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (1, 5, 5);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (2, 1, 1);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (2, 2, 1);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (2, 5, 1);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (3, 1, 3);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (3, 2, 3);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (3, 3, 3);
INSERT INTO data.note(
        id_user, id_object, note)
    VALUES (3, 4, 3);

ALTER SEQUENCE public.object_id_object_seq RESTART WITH 10;
ALTER SEQUENCE data.note_id_note_seq RESTART WITH 10;
ALTER SEQUENCE data.user_id_user_seq RESTART WITH 10;

/*
--post fillTestData
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (1, 10, 'aie');
INSERT INTO data.report(
  id_user, id_object, description)
  VALUES (1, 15, 'aie');
DELETE FROM public.toilet_temp
  WHERE gid>3;
INSERT INTO public.toilet_temp(
  id, san_gest, san_horaire, san_loc, san_sect, san_remarq, san_handi, san_num, codcomm, geom)
  VALUES (300, '1', '1', '1', '1', '1', '1', 1, '1', '01010000206A080000087FE466C58DFE3F8E71034E0FF34741');

UPDATE public.toilet_temp
  SET san_horaire='truc';
*/

COMMIT;