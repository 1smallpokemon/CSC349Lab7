# Lab 7: Database Connectivity with JDBC

## Lab Members
- Alex Hatch
- Ishan Meher
- Haris Khan
- Joseph Dewar

##### Database Username
```imeher```

## Compilation/Runtime Instructions
#### 1) Load Environment Variables

##### 1a) Create a file named ```auth.jdbc.TEMPLATE```
```
  // source.auth.jdbc.TEMPLATE
  export HP_JDBC_URL=jdbc:mysql://db.labthreesixfive.com/imeher?autoReconnect=true\&useSSL=false
  export HP_JDBC_USER= <Calpoly SLO username>
  export HP_JDBC_PW= <database lab password>
```
##### 3) Execute ```auth.jdbc.TEMPLATE``` file with command below

```source auth.jdbc.TEMPLATE```
##### 4) Compile ```InnReservations.java``` file with command below
```javac InnReservations```
##### 5) Run InnReservations
``` java -cp \path_of_mysql_connector:. InnReservations```

Example where mysql_connnector in same directory level as InnReservations.java:

```java -cp mysql-connector-java-8.0.16.jar:. InnReservations```

# Work Division
## See worklog.txt for further information
- NFR3: DBMS Access. (Ishan Meher)
- Command Line Main Menu (Alex Hatch)
- FR1 Haris Khan
- FR2 Joseph Dewar
- FR3 Ishan Meher
- FR4 Alex Hatch
- FR5 Alex Hatch
- FR6 Ishan Meher
