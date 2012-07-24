wlan1_if={5BC31F68-B88E-40FF-984D-A7BD92CC2CAA}
wlan2_if={C62D48C4-9A4B-4712-9DC8-B1444316E122}

#adb shell cat /proc/net/dev

#adb shell /data/data/net.bloody.stun/bin/stunclient 112.220.75.19 --mode full --protocol udp

echo "----- UDP Test (1) Random Port using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol udp --localaddr $wlan1_if
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- UDP Test (2) Port = 2000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol udp --localaddr $wlan1_if --localport 2000 
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- UDP Test (3) Port = 40000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol udp --localaddr $wlan1_if --localport 40000
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- UDP Test (4) Port = 50000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol udp --localaddr $wlan1_if --localport 50000 
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- UDP Test (5) Port = 50000 using wlan2 -----"
./stunclient 112.220.75.19 --mode full --protocol udp --localaddr $wlan2_if --localport 50000 
echo " "
ping 112.220.75.19 -n 1
echo " "


echo "----- TCP Test (1) Port = 2000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol tcp --localaddr $wlan1_if --localport 2000 
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- TCP Test (2) Port = 40000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol tcp --localaddr $wlan1_if --localport 40000
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- TCP Test (3) Port = 50000 using wlan1 -----"
./stunclient 112.220.75.19 --mode full --protocol tcp --localaddr $wlan1_if --localport 50000 
echo " "
ping 112.220.75.19 -n 1
echo " "

echo "----- TCP Test (4) Port = 50000 using wlan2 -----"
./stunclient 112.220.75.19 --mode full --protocol tcp --localaddr $wlan2_if --localport 50000 
echo " "
ping 112.220.75.19 -n 1
echo " "


