
# Fix-server

It handles fix connectivity.

## SETTING UP DATABASE
1. INSTALL POSTGRES 9+;
   1.1 - Enable MD2
      /etc/postgresql/14/main/pg_hba.conf
2. RUN SCRIPT ON PSQL FOR CREATING THE APPS DATABASE;
   CREATE DATABASE quickfix;
2. UPDATE USER AND PASSWORD.
   2.1 FILE: resources/applications.properties
      datasource.url=jdbc:postgresql://localhost:5432/quickfix?socketTimeout=30
      datasource.username=postgres
      datasource.password=postgres
3. FIX DATABASE INTEGRATION
   3.1. FILE: resources/fix/quickfixj-server-uat.cfg
   JdbcDriver=org.postgresql.Driver
   JdbcURL=jdbc:postgresql://localhost:5432/quickfix?socketTimeout=30
   JdbcUser=postgres
   JdbcPassword=postgres


#### Main configurations


quickfixj-uat-acceptor.cfg
quickfixj-uat-initiator.cfg


