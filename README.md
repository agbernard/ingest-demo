ingest-demo
===========

Requirements:  
1)Install RabbitMQ (http://www.rabbitmq.com/download.html)  
2)After downloading the project, navigate to ingest-demo/common and run `mvn clean install`. The other projects will get built during the startup steps below.  


Startup:  
Shell 1 (RabbitMQ server):   

    /usr/local/sbin/rabbitmq-server  

Shell 2 (Web service to accept ingestion tasks):  

    cd <project root>/ingest-demo/producer  
    mvn jetty:run   

Shell 3 (Workers to process ingestion tasks):  

    cd <project root>/ingest-demo/workers  
    mvn clean compile assembly:simple  
    java -jar target/ingest-demo-workers.jar  

Shell 4 (Web service to send content to workers):  

    <needs implementing>  

Shell 5 (Use to send tasks):  

    curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "myurl", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks  

