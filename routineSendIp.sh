 #!/bin/sh
mapfile < /var/serverScript/config/shareConfig/ip.txt mapIp
ip1="$(echo -e "${mapIp[0]}" | tr -d '[:space:]')"
ip2=$(curl ifconfig.me)

if [ "$ip1" != "$ip2" ]
then
	echo "$ip2" > /var/serverScript/config/shareConfig/ip.txt
	( cd /var/serverScript/config/shareConfig && git init )
	( cd /var/serverScript/config/shareConfig && git add . )
	( cd /var/serverScript/config/shareConfig && git commit -m "update" )
	( cd /var/serverScript/config/shareConfig && git push origin master )
fi