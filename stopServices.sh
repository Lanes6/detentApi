 #!/bin/sh
echo "START STOP SERVICE" &&
mapfile < /var/serverScript/config/config.txt configs &&
passwordSudoConf="$(echo -e "${configs[4]}" | tr -d '[:space:]')" &&
echo "STOP GEOSERVER" &&
sh /usr/local/apache-tomcat9/bin/shutdown.sh &&
echo "STOP API" &&
echo passwordSudoConf | sudo -S sh /etc/init.d/apache2 stop &&
echo "STOP ROUTINES"
crontab -r
echo "END STOP SERVICE"