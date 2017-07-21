# Spring-Cloud-Demo

This Project demonstrate how an `ApplicationClient` service will call `ApplicationServer` -service which has 2 instances
running on different ports and are registered on SpringEureka server (discovery-server).

*Pre-Requisites*

   - IntelliJ IDE
   - maven 3.0
   - java 1.8

In order to make use of this project, Follow the Steps:

1) Clone this project by giving the following command in terminal

       git clone https://github.com/syedjafar01/Spring-Cloud-Demo.git

2) Build the project

       mvn clean install

3) Open discovery-service module and run `DiscoveryServiceApplication` class

4) Open application-server module and run 2 instances of `ServiceApplication` class

   In order to run 2 instances, Follow thw steps:

       * In IntelliJ IDE - go to Edit Configurations...
       * Click on + (Add new Configuration)
       * Select Application
       * Name it as ServiceApplication-instance 1
       * Fill the options as:

                   Main class: com.syed.ServiceApplication
                   Program arguments: --server.port=8081 --service.instance.name=instance1

       * Select application-server as a module for "use classpath of module" option
       
   Click apply and run    
   
   Similarly for 2nd instance, Follow same above steps with few changes 
   
       * Name it as ServiceApplication-instance 2  
       * Program arguments: --server.port=8082 --service.instance.name=instance2  
       
   Click apply and run 
   
5) Open application-client module and run `ClientApplication` class

6) Go to browser and type

        localhost:8761
        
   This will open the `spring Eureka` ui portal where u can see the registered Application services.
   
7) Open another window and type

        localhost:8080  

   When u refresh the page, the output changes depending on which instance of `ServiceApplication` is responding. This all will be managed by spring Eureka server.