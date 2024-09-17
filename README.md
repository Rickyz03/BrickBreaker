Build infos: [![Java CI with Maven](https://github.com/Rickyz03/BrickBreaker/actions/workflows/build.yml/badge.svg)](https://github.com/Rickyz03/BrickBreaker/actions/workflows/build.yml)
![checkstyle](.github/ReadmeBadges/checkstyle-result.svg)

This branch (Jacoco):
![coverage](.github/ReadmeBadges/jacoco.svg)
![branches_coverage](.github/ReadmeBadges/branches.svg)

Main branch (Coveralls): [![Coverage Status](https://coveralls.io/repos/github/Rickyz03/BrickBreaker/badge.svg?branch=main)](https://coveralls.io/github/Rickyz03/BrickBreaker?branch=main)

Quality gate (SonarCloud): [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Rickyz03_BrickBreaker&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Rickyz03_BrickBreaker)
# BrickBreaker

Repository per il codice sorgente del progetto Brick Breaker.

<hr/>

## Funzionalità
Brick Breaker è un gioco classico in cui l'obiettivo è controllare una barra per far rimbalzare una pallina e rompere tutti i blocchi presenti nella schermata.
<br/>
Le principali caratteristiche del gioco sono:
- La pallina parte da una posizione casuale e con una direzione casuale, dopo essere stata lanciata alla pressione del tasto Enter.
- Il giocatore può muovere la barra con i tasti freccia sinistra e destra, per far rimbalzare la pallina in alto verso i blocchi
- Ogni blocco ha un punteggio casuale predeterminato e un colore che varia in base al punteggio.
- Il gioco include una schermata di vittoria, una schermata di Game Over e, in esse, la possibilità di riavviare la partita alla pressione del tasto Enter.

<hr/>

## Tecnologie usate
* Java
* Swing per l'interfaccia grafica
* Maven per la gestione del progetto
* JUnit e Mockito per i test
* Jacoco e Coveralls per la copertura del codice
* SonarCloud per il controllo qualità

<hr/>

## Dipendenze
* Java Development Kit (JDK) versione 11 o superiore
* Maven

<hr/>

## Istruzioni per la compilazione ed esecuzione su Linux
Assicurati di avere Java e Maven correttamente installati e configurati nel tuo sistema.  

Per compilare BrickBreaker:

```bash
 git clone https://github.com/Rickyz03/BrickBreaker.git
 cd BrickBreaker
 mvn clean install
```
Per eseguire il gioco:

```bash
 java -jar target/BrickBreaker-1.0-SNAPSHOT.jar
```
