
# Microservices Showcase


## Table of Contents
  1. [Introduction](#introduction)
  2. [Technologies](#technologies) 
  3. [Features](#features) 
  4. [Getting Started](#getting-started) - [Prerequisites](#prerequisites) - [Running the Project](#running-the-project) 
  5. [ API Documentation](#api-documentation) 
  6. [Project Overview (With Screenshots)](#project-overview-with-screenshots)
## Introduction

Welcome! This project serves as a showcase of a modern microservices architecture. Leveraging an array of technologies and tools, it illustrates a robust system that encapsulates the essential functionalities of a microservice. Designed with cloud-native principles, this ecosystem can be seamlessly deployed across multiple leading Cloud Providers.

This project presents an integrated landscape complete with key features such as monitoring, scaling, resilience, inter-service communication, load-balancing, log aggregation, service registry and discovery, self-healing and more.

This version cover most of the core features of the final intended project, and it's more focused in the infrastructure rather than the business logic, but there are still a few things left to implement, so please keep an eye on the technologies section bellow for reference. 

## Technologies

Here's an overview of the core technologies used:

- [x] **Java 17**
- [x] **Spring Boot**
- [x] **Spring Cloud**: Enabling inter-service communication, centralized configuration, service discovery, API Gateway and more.
- [x] **Docker & Docker Compose**: Containerization and defining multi-container Docker applications.
- [x] **Kubernetes**: An orchestration tool for automating the deployment, scaling, and management of containerized applications.
- [x] **Helm**: The package manager for Kubernetes, simplifying deployments.
- [x] **Eureka Server**: Providing service discovery within the system. (*Used for registering outside a k8s cluster*)
- [x] **Feign**: A declarative web service client, simplifying writing HTTP clients. Also used as a client-side load-balancing. (Spring 5 WebClient would be a more modern approach, but Feign fits perfectly in the project scope)
- [x] **Actuator**: Offering insights into the application's health, metrics, info, dump, env, etc.
- [x] **H2 Database**: An in-memory database for development convenience.
- [x] **Micrometer**: Facilitating application monitoring.
- [x] **Zipkin**: A distributed tracing system for gathering latency data.
- [x] **Resilience4j**: A fault tolerance library used for setting Circuit Breakers, Rate Limiters, Fallbacks, etc.
- [x] **Prometheus**: An open-source monitoring and alerting toolkit.
- [x] **Grafana**: Open-source platform for monitoring and observability with a rich UI.
- [x] **Google Cloud GKE**: Google's Kubernetes Engine, providing a managed environment for deploying, managing, and scaling containerized applications using Google infrastructure. (*Application is tested and ready for a fast deployment, but due to the cloud's costs, even with the free tier, I'm only able to run my cluster on-demand. Feel free to contact me if you wish to see the live project).* 
- [ ] ~~Spring Security and OAuth2~~ (*WIP*) - A robust security framework for authentication and authorization in Spring applications, utilizing the OAuth2 protocol for secure handling of authentication tokens.
- [ ] ~~Swagger~~ (*WIP*) - For API Documentation
- [ ] ~~Angular and RxJs~~ (*WIP*) - Used for the Front-end, consuming endpoints and building dynamic single-page applications.



## Features

- **Microservices Architecture**: Best practices in implementing scalable, maintainable microservices.
- **Robust Monitoring**: Complete observability through Prometheus, Grafana, and more.
- **Resilience Patterns**: Implementing circuit breaking, rate limiting, and more with Resilience4j.
- **Distributed Tracing**: Through Zipkin, enabling detailed insight into the application's behavior.
- **Containerization**: Using Docker for consistent development and deployment.
- **Orchestration**: Leveraging Kubernetes and Helm for scalable orchestration.

## Getting Started

### Prerequisites
The prerequisites for running the project vary depending on your deployment environment. Below are the required tools and technologies:

#### For Running on local Docker Containers (Grafana included)
 
- Docker 
#### For Running on Local Clusters (e.g., Minikube)  
- Chocolatey (Optional) 
- Docker 
- Kubernetes 
- Minikube (for local development)
- Helm 



### Running the Project

Follow these steps to get the project up and running if you with to run with Docker Compose:

1. **Ensure Docker is Installed and running**: If you don't have Docker installed, you can download and install it from [Docker's official website](https://www.docker.com/get-started).

2. **Navigate to the Project Directory**: Open your terminal or command prompt and navigate to the root directory of the project.

3. **Move to the Monitoring Folder**: Enter the following command to navigate to the "docker-compose/monitoring" folder:
 ```cd docker-compose/monitoring```
 
4. **Run Docker Compose**: Now, you can start all the services defined in the "docker-compose.yml" file with the following command:
```docker-compose up```
or for the detached mode without logs:
```docker-compose up -d```

5. **Verify the Services**: Please wait around 5 minutes to make sure all the services have properly started. You can verify that all the services are running by executing:
```docker-compose ps```
6. **Access the Application**: Depending on your configuration, your application and monitoring tools should now be accessible via your browser at the specified URLs.

~~**Follow these steps** to get the project up and running if you with to run with Minikube:~~  

~~Running multiple pods in a local cluster can be a resource intensive task, so please make sure you have at least 7GB of ram available before following this approach:~~

🚧**WIP**🚧
1. To start your Minikube cluster, execute 
``` minikube start --memory 7168 --cpus 4```
2. From the root project directory, in your helm\environments, execute:
``` helm install dev-deployment .\dev-env\```
3. To see if the services have successfully started, execute:
```kubectl get services```
(*Some pods might fail if you haven't allocated enough memory to Minikube*)




## API Documentation

🚧**WIP**🚧
While Swagger is not set, here is a quick temporary guide of how you can access the services:

### Running on Docker:
- Eureka: http://localhost:8070/
- Zipkin: http://localhost:9411/zipkin/
- Grafana: http://localhost:3000/
- Prometheus: http://localhost:9090/
- Spring API Gateway: http://localhost:8072/actuator/gateway/routes
- Spring Cloud Config: http://localhost:8071/accounts/dev - or any other service name such as /cards/prod
- Accounts Service: http://localhost:8080/sayHello - or any other endpoint (WIP Documentation)
- Loads Service: http://localhost:8090/loans/properties - or any other endpoint (WIP Documentation)
- Cards Service: http://localhost:9000/cards/properties - or any other endpoint (WIP Documentation)

### Running on a Kubernetes Cluster (Minikube or Cloud):
- With your minikube cluster running, execute 
- ``` minikube start --memory 8000 --cpus 5```



## Project Overview (With Screenshots)

🚧🚧**WIP**🚧🚧
