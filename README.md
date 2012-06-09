ingest-demo
===========

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

###Startup  
Shell 1 (RabbitMQ server, only needed if the server is not running as a daemon):  

Mac: `/usr/local/sbin/rabbitmq-server`  
Linux: `invoke-rc.d rabbitmq-server [start|stop]`  

Shell 2 (HSqlDb):

    mvn exec:java

Shell 3 (Web service to accept ingestion tasks):  

    cd <project root>/ingest-demo/producer  
    mvn -Djetty.port=8080 jetty:run   

Shell 4 (Workers to process ingestion tasks):  

    export JAVA_HOME=/path/to/java7/home (if java 7 is not the default version)
    cd <project root>/ingest-demo/workers  
    mvn compile exec:java

Shell 5 (Web service to send content to workers):  

    cd <project root>/ingest-demo/supplier  
    mvn -Djetty.port=8081 jetty:run   

Shell 6 (Use to send tasks):  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "http://localhost:8081/content/movies", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

