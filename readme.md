[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2065/gr2065) 

# Swap app 
 
Swap er en app der man kan gi bort ting man ikke trenger. Dette gjøres ved at man oppretter en annonse. Deretter kan andre brukere trykke på annonsen og ta kontakt. 
 
## Organisaering av koden 
 
- **src/main/java/swapp** for koden til applikasjonen
- **src/test/java/swapp** for testkoden 
 
## Domenelaget 
Appen samler annonse-data i form av tekst. 

Domenelaget finnes i **src/main/java/swapp/core**
 
## Brukergrensesnittlaget 
Brukergrensesnittet i appen viser liste med annonser, samt en knapp for å legge til ny annonse. 

Brukergrensesnittet er laget med JavaFX og FXML og finnes i **src/main/java/swapp/ui**

## Persistenslaget 
Persistenslaget inneholder alle klasser og logikk for lagring av annonse-data i domenelaget. Vårt persistenslag implementerer fillagring med JSON-syntaks, og finnes i **src/main/java/swapp/json**

## Bygging med maven 
Prosjektet er konfigurert til å bruke byggeverktøyet maven, og har dermed en pom.xml-fil for konfigurasjon. 
