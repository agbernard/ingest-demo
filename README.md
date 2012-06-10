Content Ingestion Demo
===========

###Use Case
As a content supplier, I want to call a service that will trigger the ingestion of new or updated content from a server of my choosing.  

###Design
1. The content supplier will trigger the ingestion of their content via a REST service provided by the content distributor.  
2. This REST service will create ingestion tasks for each piece of content passed from the supplier. 
3. Each task will be added to a queue which will be monitored by a pool of workers.
4. Each worker will pick up a task from the queue in the order they were received. The specified content will be downloaded from the supplier's server with the appropriate data ingested into the database so the content can be distributed as needed.

###Implementation
* The supplier will setup a service to deliver the content to ingest, modeled by `http://localhost:8081/content/...`
* The supplier will POST a JSON payload to a service provided by the distributor, modeled by `http://localhost:8080/ingestion/tasks`  
  * Format of the JSON: `{"supplierUrl": "http://...", "contentIds": ["1", ...]}`
* The JSON posted to the distributor will be decomposed into individual tasks and added to a queue implemented using RabbitMQ. 
* There will be another server with a pool of workers listening on the queue. When a task is taken from the queue the worker will be responsible for pulling down the data from the supplier, storing the content, and ingesting the appropriate data into the database. 
  * The worker will act on each content ID by attaching it to the URL and calling a GET on it. For example, if the supplier URL is `http://localhost:8081/content/movies`, then the worker for ID `1` will do a GET on `http://localhost:8081/content/movies/1` which should return the data for the movie with id `1`. The format of that data isn't important for the purposes of this demo - that would be something agreed upon by the supplier and distributor.
* The DB used for the demo is [HSQLDb](http://hsqldb.org/), but don't worry about downloading it - maven will handle it (see Server 2 below).

*insert diagram*

###Requirements  
1. Java 1.5/[1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u4-downloads-1591156.html)  
2. [RabbitMQ](http://www.rabbitmq.com/download.html) (tested with version 2.8.2)
3. Maven (tested with version 3.0.3)

    ```xml
    <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>	
    </properties>
    ```  

###Setup  
After cloning the project, navigate to `ingest-demo/common` and run `mvn install`. The other projects will get built during the startup steps below. You'll need to run this anytime the common project is modified.

###Execution

####Server 1 
RabbitMQ server - you only need to manually start this if the server is not running as a daemon:  

Mac: `/usr/local/sbin/rabbitmq-server`  
Linux: `invoke-rc.d rabbitmq-server start`  

####Server 2 
HSqlDb:  

    mvn exec:java

####Server 3 
Startup the web service to accept ingestion tasks:  

    cd <project root>/ingest-demo/distributor  
    mvn -Djetty.port=8080 jetty:run   

####Server 4 
Workers to process ingestion tasks:  

    export JAVA_HOME=/path/to/java7/home (if Java 7 is not the default version)
    cd <project root>/ingest-demo/workers  
    mvn compile exec:java

Note: this is the only piece that actually requires Java 7, but there is not technical reason for it. It was used just to [experiment with some new features](http://www.theserverside.com/tutorial/Use-try-with-resources-Language-Enhancements-for-the-Java-7-OCPJP-Exam).

####Server 5 
Web service to send content to workers:  

    cd <project root>/ingest-demo/supplier  
    mvn -Djetty.port=8081 jetty:run   

####Server 6
Just for testing - once everything is running, you can use the following command to see it in action:  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "http://localhost:8081/content/movies", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

Feel free to add as many contentId's as you want.

###Evaluation
TODO
* talk about the cost/benefit of multiple servers  
* talk about RabbitMQ  

**Benefits:**  
* The code is purposely written to take advantage of [JPA](http://docs.oracle.com/javaee/5/tutorial/doc/bnbpz.html) with [Hibernate](http://www.hibernate.org) as the provider, so any of Hibernate's supported DB's would work. Even if the provider is changed, the code would not need to.
