cd /d %~dp0
cd ..
@REM ren pom.xml pom-back.xml
@REM ren pom-sonatype.xml pom.xml
@REM call mvn -N versions:update-child-modules
call mvn clean deploy source:jar -DskipTests  -Pcentral-publish -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Darguments=“gpg.passphrase=passphrase”
@rem call mvn clean deploy -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Pcentral-publish
@REM ren pom.xml pom-sonatype.xml
@REM ren pom-back.xml pom.xml
cd mak