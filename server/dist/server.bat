@echo off
title Login Server
echo Starting JTS Tanks Server.
echo.
REM Protected build
java -version:1.7 -server -Dfile.encoding=UTF-8 -Djava.library.path=. -Xms32m -Xmx64m -cp config;./* ru.jts.server.Server

pause