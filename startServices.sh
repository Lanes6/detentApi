 #!/bin/sh
echo "START START SERVICE" &&
mapfile < /var/serverScript/config/config.txt configs &&
passwordSudoConf="$(echo -e "${configs[4]}" | tr -d '[:space:]')" &&
echo "START GEOSERVER" &&
sh /usr/local/apache-tomcat9/bin/startup.sh && &&
echo "START API" &&
echo ${passwordSudoConf} | sudo -S sh /etc/init.d/apache2 start &&
echo "START ROUTINE UPDATE DB"  &&
#(crontab -l; echo " * * 1 * * sh /var/serverScript/updateDB.sh") | crontab - &&
echo "START ROUTINE SHARE IP" &&
(crontab -l; echo " * * * * * sh /var/serverScript/routineSendIp.sh") | crontab - &&
echo "END START SERVICE"