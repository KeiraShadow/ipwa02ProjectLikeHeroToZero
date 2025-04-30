Dies ist ein Projekt was die Fallstudie für IPWA02-01 Aufgabenstellung 1:Like Hero To Zero darstellt.
Die Dateien sind zu finden in src/main/.

1. Voraussetzungen
Folgende Software muss auf dem System installiert sein:
Eclipse IDE 2021-12 R (Enterprise Edition)
XAMPP Version 8.2.4-0 (enthält Apache und MySQL)
Git (für das Klonen des Repositories)
Java Development Kit (JDK)

2. Projekt aus GitHub klonen
Öffnen Sie die Kommandozeile (Terminal/CMD)
Navigieren Sie zum gewünschten Verzeichnis
Führen Sie folgenden Befehl aus:
bash
git clone https://github.com/KeiraShadow/ipwa02ProjectLikeHeroToZero.git

3. Projekt in Eclipse importieren
Öffnen Sie Eclipse IDE
Wählen Sie File > Import
Wählen Sie Maven > Existing Maven Projects
Navigieren Sie zum geklonten Projektverzeichnis
Wählen Sie pom.xml aus und klicken Sie auf Finish
Warten Sie, bis Maven alle Abhängigkeiten heruntergeladen hat

4. Datenbank einrichten
Starten Sie XAMPP Control Panel
Starten Sie die Apache- und MySQL-Dienste
Öffnen Sie phpMyAdmin im Browser (http://localhost/phpmyadmin)
Erstellen Sie eine neue Datenbank mit dem Namen likeherotozero
Importieren Sie die SQL-Dump-Datei:
Wählen Sie die erstellte Datenbank aus
Klicken Sie auf "Importieren"
Wählen Sie die SQL-Dump-Datei aus dem Projektverzeichnis
Klicken Sie auf "OK" um den Import zu starten

5. Projekt konfigurieren
Überprüfen Sie die Datenbankverbindung in src/main/resources/META-INF/persistence.xml:

<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/likeherotozero" />
<property name="jakarta.persistence.jdbc.user" value="root" />
<property name="jakarta.persistence.jdbc.password" value="" />

6. Projekt deployen und starten
Rechtsklick auf das Projekt in Eclipse
Wählen Sie auf der Datei index.html Run As > Run on Server
Wählen Sie den Apache Tomcat v10.0 Server
Klicken Sie auf Finish

8. Anwendung aufrufen
Öffnen Sie einen Webbrowser
Navigieren Sie zu: http://localhost:8080/LikeHeroToZero/index.html

9. Zugangsdaten für Test-Benutzer
Wissenschaftler-Zugang:
Benutzername: scientist1
Passwort: password
Administrator-Zugang:
Benutzername: 1admin
Passwort: password

10. Fehlerbehebung
Bei häufig auftretenden Problemen:
Stellen Sie sicher, dass Apache und MySQL in XAMPP laufen
Überprüfen Sie die Datenbankverbindung in der persistence.xml
Prüfen Sie, ob alle Maven-Abhängigkeiten korrekt heruntergeladen wurden
Stellen Sie sicher, dass der Port 8080 nicht von anderen Anwendungen belegt ist
