# NHPlus 

### Informationen zur Lernsituation
Sie sind Mitarbeiter der HiTec GmbH, die seit über 15 Jahren IT-Dienstleister und seit einigen Jahren ISO/IEC 27001 zertifiziert ist. Die HiTec GmbH ist ein mittelgroßes IT-Systemhaus und ist auf dem IT-Markt mit folgenden Dienstleistungen und Produkten vetreten:

### Entwicklung: Erstellung eigener Softwareprodukte

Consulting: Anwenderberatung und Schulungen zu neuen IT- und Kommunikationstechnologien , Applikationen und IT-Sicherheit

IT-Systembereich: Lieferung und Verkauf einzelner IT-Komponenten bis zur Planung und Installation komplexer Netzwerke und Dienste

Support und Wartung: Betreuung von einfachen und vernetzten IT-Systemen (Hard- und Software)

Für jede Dienstleistung gibt es Abteilungen mit spezialisierten Mitarbeitern. Jede Abteilung hat einen Abteilungs- bzw. Projektleiter, der wiederum eng mit den anderen Abteilungsleitern zusammenarbeitet.



### Projektumfeld und Projektdefinition

Sie arbeiten als Softwareentwickler in der Entwicklungsabteilung. Aktuell sind sie dem Team zugeordnet, das das Projekt "NHPlus" betreut. Dessen Auftraggeber - das Betreuungs- und Pflegeheim "Curanum Schwachhausen" - ist ein Pflegeheim im Bremer Stadteil Schwachhausen - bietet seinen in eigenen Zimmern untergebrachten Bewohnern umfangreiche Therapie- und Serviceleistungen an, damit diese so lange wie möglich selbstbestimmt und unabhängig im Pflegeheim wohnen können. Curanum Schwachhausen hat bei der HiTec GmbH eine Individualsoftware zur Verwaltung der Patienten und den an ihnen durchgeführten Behandlungen in Auftrag gegeben. Aktuell werden die Behandlungen direkt nach ihrer Durchführung durch die entsprechende Pflegekraft handschriftlich auf einem Vordruck erfasst und in einem Monatsordner abgelegt. Diese Vorgehensweise führt dazu, dass Auswertungen wie z.B. welche Behandlungen ein Patient erhalten oder welche Pflegkraft eine bestimmte Behandlung durchgeführt hat, einen hohen Arbeitsaufwand nach sich ziehen. Durch NHPlus soll die Verwaltung der Patienten und ihrer Behandlungen elektronisch abgebildet und auf diese Weise vereinfacht werden.

Bei den bisher stattgefundenen Meetings mit dem Kunden konnten folgende Anforderungen an NHPlus identifiziert werden:

- alle Patienten sollen mit ihrem vollen Namen, Geburtstag, Pflegegrad, dem Raum, in dem sie im Heim untergebracht sind, sowie ihrem Vermögensstand erfasst werden.

- Die Pflegekräfte werden mit ihrem vollen Namen und ihrer Telefonnumer erfasst, um sie auf Station schnell erreichen zu können.

- jede Pflegekraft erfasst eine Behandlung elektronisch, indem sie den Patienten, das Datum, den Beginn, das Ende, die Behandlungsart sowie einen längeren Text zur Behandlung erfasst.

- Die Software muss den Anforderungen des Datenschutzes entsprechen.

- Die Software ist zunächst als Desktopanwendung zu entwickeln, da die Pflegekräfte ihre Behandlungen an einem stationären Rechner in ihrem Aufenthaltsraum erfassen sollen.



Da in der Entwicklungsabteilung der HiTech GmbH agile Vorgehensweisen vorgeschrieben sind, wurde für NHPlus Scum als Vorgehensweise gewählt.



## Testergebnisse

| Test-ID     | Beschreibung                                           | Vorbedingung                              | Testschritte                                                                                          | Erwartetes Ergebnis                                                                                       | Testergebnis | Bemerkung bei Abweichung / Ergänzung                       |
|-------------|--------------------------------------------------------|-------------------------------------------|--------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------|
| TF1_1       | Alle Pflegekräfte anzeigen                             | Angemeldet, "Pflegekräfte" geöffnet       | –                                                                                                     | Tabelle mit Vor-/Nachnamen & Telefonnummer aller Pflegekräfte                                            |              |                                                             |
| TF1_2       | Neue Pflegekraft hinzufügen                            | Angemeldet, "Pflegekräfte" geöffnet       | Felder ausfüllen, „Hinzufügen“ klicken                                                                | Pflegekraft wird mit ID hinzugefügt & in DB gespeichert                                                   |              |                                                             |
| TF1_3       | Pflegekraft-Daten ändern                               | "Pflegekräfte" geöffnet                   | Doppelklick, Wert ändern, Enter                                                                       | Geänderter Wert wird angezeigt & gespeichert                                                               |              |                                                             |
| TF1_4       | Pflegekraft löschen/sperren                            | "Pflegekräfte" geöffnet                   | Person(en) auswählen, „Löschen“ klicken                                                               | Person verschwindet aus Tabelle, bleibt aber in DB (Speicherfrist)                                       |              |                                                             |
| TF2_1       | Patient archivieren                                    | Patient ausgewählt                         | Archivieren-Button drücken                                                                            | Patient wird nicht mehr angezeigt                                                                          |              |                                                             |
| TF2_2       | Datenbank enthält archivierten Datensatz               | DB-Zugriff vorhanden                       | Archivierten Datensatz suchen                                                                         | Datensatz vorhanden mit “gelöscht”-Flag oder Timestamp                                                    |              |                                                             |
| TF2_3       | Automatische Löschung nach Frist                       | DB-Zugriff vorhanden                       | Timestamp setzen, 5 Min warten, erneut suchen                                                         | Datensatz ist gelöscht                                                                                     |              |                                                             |
| TF3_1       | Login mit falschen Benutzerdaten                       | Programm gestartet                         | Mit falschen Daten einloggen                                                                          | Fehlermeldung, kein Zugang                                                                                 |              |                                                             |
| TF3_2       | Login mit gültigen Benutzerdaten                       | Programm gestartet                         | Mit gültigen Daten einloggen                                                                          | Erfolgreiche Anmeldung                                                                                      |              |                                                             |
| TF3_3       | Benutzerrollen-Test                                    | Benutzer ist eingeloggt                   | Tabellen sichten                                                                                      | Nur relevante Daten sichtbar                                                                               |              |                                                             |
| TF3_4       | Admin löscht/bearbeitet Daten                          | Als Admin eingeloggt                      | Daten löschen & bearbeiten                                                                            | Daten erfolgreich gelöscht/bearbeitet                                                                      |              |                                                             |
| TF4_1       | Alle Views aufrufen                                    | User angemeldet                            | Alle Views nacheinander aufrufen                                                                      | Kein Feld „Vermögensstand“ sichtbar                                                                       |              |                                                             |
| TF4_2       | Datenbank überprüfen auf „Vermögensstand“              | Als Admin angemeldet                       | DB-Tabelle auf das Feld prüfen                                                                        | Kein Feld „Vermögensstand“ vorhanden                                                                      |              |                                                             |


## Zusatzfeatures:

### Logindaten 
admin: 
reguläre Pflegekraft: