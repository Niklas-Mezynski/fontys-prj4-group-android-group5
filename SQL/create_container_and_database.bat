@echo off
docker run --name postgresPrj4 -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=prj4Group5 -e POSTGRES_DB=postgres -d postgres
timeout /t 5 /nobreak
REM ---------------------------------------------------------------------------------------------------------------------------------------------------
docker exec -i postgresPrj4 psql -U postgres -d postgres < C:\Users\%username%\Documents\GitHub\prj4-group-android-group5\SQL\table_creation.sql
timeout /t 3 /nobreak
echo Container und Datenbank erfolgreich erstellt!
pause
exit
