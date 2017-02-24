Shadow Services - Document Generator
====================================

Summary
-------
This microservice provides the functionality to generate documents driven by the configured templates
in the required format for a given journey.

Dependencies
------------

* Java 8 (Mandatory)

Maven Build
------------

```bash
    git clone https://github.com/connected-world-services/shadow-docgen.git
    mvn clean install
```

Maven Run
----------------

```bash
    mvn spring-boot:run
```

Command Line Run
----------------

```bash
	cd target/
    java -jar docgen-service-X.X-XXX-SNAPSHOT.jar
```

Test
-------------------

```bash
    curl http://localhost:8585/document/01-001
   

```

Endpoints (based on default settings)
----------------------------------
* Reference Data by Locale - <http://localhost:8585/document/{cwsRef}>
* Swagger - <http://localhost:8585/swagger-ui.html>

Configuration
----------------------------------

|                Property                 |                 Description                 |                 Default Value                  |  
| --------------------------------------- | ------------------------------------------- | ---------------------------------------------- |  
| server.port | The port for the microservice to listen on | 8585 |  
| docgen.endpoints.appengine | The url for appengine hal api | should be configured |
| docgen.template.html.location | Path to location of the html template file. | classpath:html/template.html |
| docgen.html.images.folder     | Path to the location of the folder that contains the images referenced in html template | classpath:html/images |
| docgen.http.max.connections | Defaults to 100  | DOCGEN |
| docgen.http.max.connections.per.route |  Defaults to 20 | DOCGEN |
| docgen.http.connection.timetolive.millis | Defaults to 5 seconds i.e. 5000ms | DOCGEN |
| docgen.http.request.connection.timeout.millis | Used when requesting a connection from the connection manager, Defaults to 1000  | DOCGEN |
| docgen.http.connect.timeout.millis | Timeout to deterimine how long to wait until a connection is established, Defaults to 1000 | DOCGEN |
| logging.file | full file name, excluding rollingpattern.log  | {environment temp location}/docgen-audit and is suffixed with rolling pattern (%d{yyyy-MM-dd}.log) |
| logging.path | path where log file will be created. If set, it replaces path part of logging.file property defined above  | {environment temp location} or '/tmp' if not set |
| ssl.port | port for HTTPS connector | 8443 |
| ssl.key-store | path to keystore (jks) file | classpath:keystore.jks |
| ssl.key-password | manager key password | password |
| ssl.key-store-password | keystore file password | secret |



Overriding Default Configuration
----------------------------------
The application should startup as is provided that all the mandatory dependencies are installed correctly
on the same host as the reference data service is being to deployed to.

In case any of the properties above need overriding, create a yaml config file somewhere in the file system i.e: `config.yml` with the relevant
overrides and make sure you start the service from the command line with the `--spring.config.location` parameter pointing to the config path i.e: file:///opt/refdata/config.yml.

For more information please refer to: <https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html>
