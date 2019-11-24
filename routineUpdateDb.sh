 #!/bin/sh
echo "START UPDATE DATABASE"  &&
mapfile < /var/serverScript/config/config.txt configs &&
ipDbConf="$(echo -e "${configs[0]}" | tr -d '[:space:]')" &&
userDbConf="$(echo -e "${configs[1]}" | tr -d '[:space:]')" &&
passwordDbConf="$(echo -e "${configs[2]}" | tr -d '[:space:]')" &&
nameDbConf="$(echo -e "${configs[3]}" | tr -d '[:space:]')" &&

rm -rf /var/serverScript/toilet  &&
rm -rf /var/serverScript/pav_verre  &&
rm -rf /var/serverScript/tree   &&
rm -rf /var/serverScript/trash   &&
rm -rf /var/serverScript/banc   &&
rm -rf /var/serverScript/outline_aglo &&
mkdir -p /var/serverScript/toilet   &&
mkdir -p /var/serverScript/pav_verre   &&
mkdir -p /var/serverScript/tree   &&
mkdir -p /var/serverScript/trash   &&
mkdir -p /var/serverScript/banc   &&
mkdir -p /var/serverScript/outline_aglo &&
wget -o /dev/null -O /var/serverScript/toilet/toilet -N -q https://data.orleans-metropole.fr/explore/dataset/espace_public_ev_sanitaires/download/?format=shp\&timezone=Europe/Berlin &&
echo "Download toilet OK" &&
wget -o /dev/null -O /var/serverScript/pav_verre/pav_verre -N -q https://data.orleans-metropole.fr/explore/dataset/dechets_pav/download/?format=shp\&refine.type_flux=Verre\&timezone=Europe/Berlin &&
echo "Download pav_verre OK" &&
wget -o /dev/null -O /var/serverScript/tree/tree -N -q https://data.orleans-metropole.fr/explore/dataset/espace_publicev_arbres/download/?format=shp\&timezone=Europe/Berlin &&
echo "Download tree OK" &&
wget -o /dev/null -O /var/serverScript/trash/trash -N -q https://data.orleans-metropole.fr/explore/dataset/espace_public_ev_corbeilles/download/?format=shp\&timezone=Europe/Berlin &&
echo "Download trash OK" &&
wget -o /dev/null -O /var/serverScript/banc/banc -N -q https://data.orleans-metropole.fr/explore/dataset/espace_public_ev_banc/download/?format=shp\&timezone=Europe/Berlin &&
echo "Download banc OK" &&
wget -o /dev/null -O /var/serverScript/outline_aglo/outline_aglo -N -q https://data.orleans-metropole.fr/explore/dataset/contours-iris-2016-avec-epci/download/?format=shp\&timezone=Europe/Berlin &&
echo "Download outline_aglo OK"  &&
unzip /var/serverScript/toilet/toilet -d toilet  &&
unzip /var/serverScript/pav_verre/pav_verre -d pav_verre  &&
unzip /var/serverScript/tree/tree -d tree &&
unzip /var/serverScript/trash/trash -d trash   &&
unzip /var/serverScript/banc/banc -d banc &&
unzip /var/serverScript/outline_aglo/outline_aglo -d outline_aglo  &&
echo "Unzip OK" &&
shp2pgsql -s 2154 /var/serverScript/toilet/espace_public_ev_sanitaires public.toilet_temp > /var/serverScript/toilet/toilet.sql   &&
shp2pgsql -s 2154 /var/serverScript/trash/espace_public_ev_corbeilles public.trash_temp > /var/serverScript/trash/trash.sql &&
shp2pgsql -s 2154 /var/serverScript/banc/espace_public_ev_banc public.banc_temp > /var/serverScript/banc/banc.sql   &&
shp2pgsql -s 2154 /var/serverScript/pav_verre/dechets_pav public.pav_verre_temp > /var/serverScript/pav_verre/pav_verre.sql &&
shp2pgsql -s 2154 /var/serverScript/tree/espace_publicev_arbres public.tree_temp > /var/serverScript/tree/tree.sql &&
shp2pgsql -s 2154 /var/serverScript/outline_aglo/contours-iris-2016-epci public.outline_aglo_temp > /var/serverScript/outline_aglo/outline_aglo.sql &&
echo "Generate .sql OK" &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/sql/preUpdate.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/toilet/toilet.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/banc/banc.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/outline_aglo/outline_aglo.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/pav_verre/pav_verre.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/tree/tree.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/trash/trash.sql &&
PGPASSWORD="${passwordDbConf}" psql -h ${ipDbConf} -U ${userDbConf} -d ${nameDbConf} -f /var/serverScript/sql/postUpdate.sql &&

rm -rf /var/serverScript/toilet  &&
rm -rf /var/serverScript/pav_verre  &&
rm -rf /var/serverScript/tree   &&
rm -rf /var/serverScript/trash   &&
rm -rf /var/serverScript/banc   &&
rm -rf /var/serverScript/outline_aglo &&
echo "END UPDATE DATABASE"