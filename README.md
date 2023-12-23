# Accenture Tech Hub - Java Back End Developer Challenge
This project is a challenge carried out by [Nuwe](https://nuwe.io/) in collaboration with [Accenture](https://www.accenture.com/). The main goal is to renovate the appointment system of a hospital.

My task is to complete the implementation and development of the backend's appointment management system.
The following needs have been developed:

1. Implement the creation of appointments through the API in the `AppointmentController.java` file. 

2. Perform tests for entities in the `EntityUnitTest.java` and  `EntityControllerUnitTest.java` files.

3. Deploy the API in a scalable way, with separate Dockerfiles for the MySQL database (`Dockerfile.mysql`) and the microservice (`Dockerfile.maven`).

4. Additionally, a `docker-compose.yml` file has been generated to simplify the deployment process of the API.
---

## Instructions for running the project with Docker
1. Install [Docker](https://www.docker.com/) on your system if you haven't already. --> [Download Link](https://www.docker.com/products/docker-desktop/).

2. Clone the project repository from the provided source.

3. Navigate to the project directory using the command line or terminal.
4. Now decide how to bring the API to life: "_Easy Way_" or "_Hard Way_ (Customizable)".

- _Easy Way_

  Using **Docker Compose** you will build/run the Docker images and the Containers for the MySQL database and the microservice with a single command.


- _Hard Way_

  You will follow a step-by-step process to create each necessary image and container.

Make sure Docker is running and then proceed with the following steps.


| **Easy Way**                                           | **Hard Way**                                                                                                                |
|--------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------|
| 1. Execute the following command.`docker compose up  ` | 1. Build the MySQL container image.`docker build -t mysql-image -f Dockerfile.mysql . `                                     |
|                                                        | 2. Build the microservice container image.`docker build -t microservice-image -f Dockerfile.maven . `                       |
|                                                        | 3. Create a Docker network.`docker network create my-net`                                                                   |
|                                                        | 4. Run the MySQL container.`docker run -d --name mysql-container -p 3306:3306 --network=my-net -e MYSQL_ROOT_PASSWORD=pass -d mysql-image`|
|                                                        | 5. Run the microservice container.`docker run -d --name microservice-container -p 8080:8080 --network=my-net -d microservice-image`|


Confirm that both the MySQL container and the microservice container are up and running with the following command: `docker ps`.

The application should now be running on port 8080 on your local machine (http://localhost:8080/).

**Note:** Make sure that the required ports are available and that there are no conflicts with other active services. Otherwise, you can change the ports when you execute steps 4 and 5 (_Hard Way_).