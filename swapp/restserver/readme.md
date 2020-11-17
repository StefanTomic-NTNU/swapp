# Restserver

Denne mappen inneholder filer for oppsett av restserveren og tester av endepunktene til api'et. Når serverren startes opp med *SwappConfig*-klassen leses det fra filen som lagres til av restapi'et. Her benyttes deserializerne i core-mappen for å gjøre om json-filen til en SwappModule med usernames og SwappLists med SwappItems. I testene av servern sendes det forespørsler til endepunktene til api'et, og det blir sjekket at disse behandles korrekt og gir den forventede responsen. 
