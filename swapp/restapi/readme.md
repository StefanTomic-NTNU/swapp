# RestAPI

restapi-mappen inneholder klassene som håndterer forespørslene som sendes til restapi'et, samt en hjelpe-klasse for lagring.

*SwappModelService* håndterer forespørsler som sendes til */swapp/* og sender videre forespørsler som sendes til **/swapp/{email}** (der email er brukernavnet/e-posten til brukere). *SwappListResource* håndterer forespørsler som sendes til **/swapp/{email}** og sender videre forespørsler som sendes til **/swapp/{email}/{annonsetittel}** til *swappItemResource*.

| URI                            | Metode    | Beskrivelse                                           
| ------------------------------ | --------- | ----------------------------------------------------- 
| /swapp/                        | GET       | Returnerer hele hash-mapet med alle annonser          
| /swapp/                        | PUT       | Tar inn en liste med annonser og oppretter en ny nøkkel med denne listen som verdi
| /swapp/{email}/                | GET       | Returnerer en liste med alle annonser som tilhører {email}
| /swapp/{email}/                | POST      | Tar inn en annonse som tilhører {email} og legger den til
| /swapp/{email}/                | PUT       | Tar inn en liste med annonser som tilhører {email} og setter denne listen til å tilhøre {email}
| /swapp/{email}/{annonsetittel}/ | GET       | Returnerer en annonse som tilhører {email} og har tittel {annonsetittel}
| /swapp/{email}/{annonsetittel}/ | PUT       | Tar inn en annonse og oppdaterer en annonse som tilhører {email} og har tittel {annonsetittel}
| /swapp/{email}/{annonsetittel}/ | DELETE    | Sletter en annonse som tilhører {email} og har tittel {annonsetittel}

Hjelpeklassen *SaveHelper* har funksjoner for å lese og skrive til fil. Skriving til fil gjøres ved hver PUT-, POST- og DELETE-forespørsel til APIet.
