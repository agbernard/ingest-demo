Content Ingestion Demo
===========

###Use Case
As a content supplier, I want to call a service that will trigger the ingestion of new or updated content from a server of my choosing.

###Design
1. The content supplier will trigger the ingestion of their content via a REST service. 
2. This REST service will create ingestion tasks for each piece of content passed from the supplier. 
3. Each task will be added to a queue which will be monitored by a pool of workers.
4. Each worker will pick up a task from the queue in the order they were received. The specified content will be downloaded from the supplier's server with the appropriate data ingested into the database so the content can be retrieved.

###Implementation
The supplier will setup a service to deliver the content to ingest. This will be modeled on `http://localhost:8081/content/...`. The supplier will POST a JSON payload to a service provided by the repository, modeled by `http://localhost:8080/ingestion/tasks`. That JSON will be decomposed into individual tasks and added to a queue which will be implemented using RabbitMQ. There will be another server with a pool of workers listening on the queue. When a task is taken from from the queue the worker will be responsible for pulling down the data from the supplier, storing the content, and ingesting the appropriate data into the database. The DB used for the demo is HSQLDb, but it is accessed via JPA with Hibernate as the provider, so any of Hibernate's supported DB's would work.

###Requirements  
1. Java 1.5/1.7  
2. RabbitMQ (http://www.rabbitmq.com/download.html)  
3. Maven - consider setting the following property in your .m2/settings.xml file to get rid of all those maven warnings:

    ```xml
    <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>	
    </properties>
    ```  

###Setup  
After cloning the project, navigate to ingest-demo/common and run `mvn install`. The other projects will get built during the startup steps below.  

###Quick Start  
####Server 1 
RabbitMQ server - you only need to manually start this if the server is not running as a daemon:  

Mac: `/usr/local/sbin/rabbitmq-server`  
Linux: `invoke-rc.d rabbitmq-server start`  

####Server 2 
HSqlDb:  

    mvn exec:java

####Server 3 
Startup the web service to accept ingestion tasks:  

    cd <project root>/ingest-demo/producer  
    mvn -Djetty.port=8080 jetty:run   

####Server 4 
Workers to process ingestion tasks:  

    export JAVA_HOME=/path/to/java7/home (if Java 7 is not the default version)
    cd <project root>/ingest-demo/workers  
    mvn compile exec:java

Note: this is the only piece that actually requires Java 7, but there is not technical reason for it. It was used just to [experiment with some new features](http://www.theserverside.com/tutorial/Use-try-with-resources-Language-Enhancements-for-the-Java-7-OCPJP-Exam)

####Server 5 
Web service to send content to workers:  

    cd <project root>/ingest-demo/supplier  
    mvn -Djetty.port=8081 jetty:run   

Once everything is running, you can use the following command to see it in action:  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "http://localhost:8081/content/movies", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

Feel free to add as many contentId's as you want.

###Evaluation
TODO
