@echo off

title Game Server

java -version:1.7 -server -Dfile.encoding=UTF-8 -Djava.library.path=. -Xms32m -Xmx64m -cp config;./lib/* ru.jts.gameserver.GameServer

pause