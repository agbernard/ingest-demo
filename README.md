ingest-demo
===========

Requirements:  
1. Java 1.5/1.7  
2. Maven  
   * Consider setting the following property in your .m2/settings.xml file to get rid of all those maven warnings:

    ```xml
    <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>	
    </properties>
    ```  

3. RabbitMQ (http://www.rabbitmq.com/download.html)  
4. After cloning the project, navigate to ingest-demo/common and run `mvn install`. The other projects will get built during the startup steps below.  

Startup:  
Shell 1 (RabbitMQ server):   

    /usr/local/sbin/rabbitmq-server  

Shell 2 (Web service to accept ingestion tasks):  

    cd <project root>/ingest-demo/producer  
    mvn -Djetty.port=8080 jetty:run   

Shell 3 (Workers to process ingestion tasks):  

    export JAVA_HOME=/path/to/java7/home
    cd <project root>/ingest-demo/workers  
    mvn compile exec:java -Dexec.mainClass="com.agbdev.ingestdemo.worker.IngestionWorker"

Shell 4 (Web service to send content to workers):  

    cd <project root>/ingest-demo/supplier  
    mvn -Djetty.port=8081 jetty:run   

Shell 5 (Use to send tasks):  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "http://localhost:8081/content/movies", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

