ingest-demo
===========

Requirements:  
1. Maven
1. RabbitMQ (http://www.rabbitmq.com/download.html)  
1. After cloning the project, navigate to ingest-demo/common and run `mvn clean install`. The other projects will get built during the startup steps below.  


Startup:  
Shell 1 (RabbitMQ server):   

    /usr/local/sbin/rabbitmq-server  

Shell 2 (Web service to accept ingestion tasks):  

    cd <project root>/ingest-demo/producer  
    mvn -Djetty.port=8080 jetty:run   

Shell 3 (Workers to process ingestion tasks):  

    cd <project root>/ingest-demo/workers  
    mvn clean compile assembly:simple  
    java -jar target/ingest-demo-workers.jar  

Shell 4 (Web service to send content to workers):  

    cd <project root>/ingest-demo/supplier  
    mvn -Djetty.port=8081 jetty:run   

Shell 5 (Use to send tasks):  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "http://localhost:8081/content/movies", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

