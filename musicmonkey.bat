@ECHO OFF

REM You must either have java in your path, or you must edit this script
REM to use your installed java.

REM HME Factory options
REM If you have more than one network interface on this machine, 
REM use the -i option to specify which interface to bind to

REM OPTIONS="-i 192.168.8.187"

java -cp lib\jid3lib-0.5.jar;lib\hme.jar;lib\log4j.jar;lib\JID3.jar;lib\musicmonkey.jar;. com.tivo.hme.sdk.Factory %OPTIONS% net.whirljack.tivohme.musicmonkey.MusicMonkey

