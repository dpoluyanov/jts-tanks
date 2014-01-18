@echo off

title Auth Server

java -version:1.7 -server -Dfile.encoding=UTF-8 -Djava.library.path=. -Xms32m -Xmx64m -cp config;./* ru.jts.authserver.AuthServer

pause