@echo on
echo ---------------------------------------------------------------
echo Clonando el repositorio...
echo ---------------------------------------------------------------
git pull

echo ---------------------------------------------------------------
echo Compilando el proyecto...
echo ---------------------------------------------------------------
mvn clean package

echo ---------------------------------------------------------------
echo Copiando el archivo JAR a la raiz...
echo ---------------------------------------------------------------
copy /Y target\portaltransaccional-0.0.1-SNAPSHOT.jar .

echo ---------------------------------------------------------------
echo Ejecutando la aplicacion...
echo ---------------------------------------------------------------
java -jar portaltransaccional-0.0.1-SNAPSHOT.jar

pause
