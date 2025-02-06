# Case Study - Flight Search Api 

<p align="center">
    <img src="screenshots/mainimage.png" alt="Main Information" width="800" height="500">
</p>

### 📖 Information

<ul style="list-style-type:disc">
  <li>
    This project provides a Spring Boot API for managing user authentication,
    airport management, flight management, and flight search functionalities. It
    includes advanced logging and security features.
  </li>
  <li>
    User Authentication:
    <ul>
      <li>
        User Registration: Register users with an email and password.
      </li>
      <li>
        User Login: Authenticate users with email and password, generating
        access and refresh tokens.
      </li>
      <li>
        Token Refresh: Refresh expired access tokens using refresh tokens.
      </li>
      <li>User Logout: Invalidate tokens to log users out.</li>
    </ul>
  </li>
  <li>
    Airport Management:
    <ul>
      <li>
        Create Airports: Accessible only to <i>ADMIN</i> users.
      </li>
      <li>
        Retrieve Airports: Paginated airport list retrieval, accessible to both
        <i>ADMIN</i> and <i>USER</i> roles.
      </li>
      <li>
        Retrieve Specific Airport: Search by airport ID, accessible to both
        <i>ADMIN</i> and <i>USER</i> roles.
      </li>
      <li>
        Update Airports: Modify an existing airport by ID, accessible only to
        <i>ADMIN</i> users.
      </li>
      <li>
        Delete Airports: Remove airports by ID, accessible only to <i>ADMIN</i>
        users.
      </li>
    </ul>
  </li>
  <li>
    Flight Management:
    <ul>
      <li>
        Create Flights: Accessible only to <i>ADMIN</i> users.
      </li>
      <li>
        Retrieve Flights: Paginated flight list retrieval, accessible to both
        <i>ADMIN</i> and <i>USER</i> roles.
      </li>
      <li>
        Retrieve Specific Flight: Search by flight ID, accessible to both
        <i>ADMIN</i> and <i>USER</i> roles.
      </li>
      <li>
        Update Flights: Modify an existing flight by ID, accessible only to
        <i>ADMIN</i> users.
      </li>
      <li>
        Delete Flights: Remove flights by ID, accessible only to <i>ADMIN</i>
        users.
      </li>
    </ul>
  </li>
  <li>
    Flight Search:
    <ul>
      <li>
        Search Flights: Search for flights based on departure, arrival airports,
        and departure date. Accessible to both <i>ADMIN</i> and <i>USER</i>
        roles. (One Way - Round-Trip)
      </li>
    </ul>
  </li>
  <li>
    Security:
    <ul>
      <li>
        User Roles: ADMIN and USER roles implemented using Spring Security.
      </li>
      <li>JWT Authentication: Secure endpoints with JSON Web Tokens.</li>
    </ul>
  </li>
  <li>
    Logging:
    <ul>
      <li>
        Custom Logging Aspect: Logs details of REST controller method calls and
        exceptions, including HTTP request and response details.
      </li>
    </ul>
  </li>
</ul>


### Explore Rest APIs

Endpoints Summary
<table style="width:100%;">
    <tr>
        <th>Method</th>
        <th>Url</th>
        <th>Description</th>
        <th>Request Body</th>
        <th>Path Variable</th>
        <th>Response</th>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/authentication/user/register</td>
        <td>Register for Admin or User</td>
        <td>RegisterRequest</td>
        <td></td>
        <td>CustomResponse&lt;Void&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/authentication/user/login</td>
        <td>Login for Admin or User</td>
        <td>LoginRequest</td>
        <td></td>
        <td>CustomResponse&lt;TokenResponse&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/authentication/user/refresh-token</td>
        <td>Refresh Token for Admin or User</td>
        <td>TokenRefreshRequest</td>
        <td></td>
        <td>CustomResponse&lt;TokenResponse&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/authentication/user/logout</td>
        <td>Logout for Admin or User</td>
        <td>TokenInvalidateRequest</td>
        <td></td>
        <td>CustomResponse&lt;Void&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/airports</td>
        <td>Create a new Airport</td>
        <td>CreateAirportRequest</td>
        <td></td>
        <td>CustomResponse&lt;String&gt;</td>
    </tr>
    <tr>
        <td>GET</td>
        <td>/api/v1/airports</td>
        <td>Get all Airports</td>
        <td>AirportPagingRequest</td>
        <td></td>
        <td>CustomResponse&lt;CustomPagingResponse&lt;AirportResponse&gt;&gt;</td>
    </tr>
    <tr>
        <td>GET</td>
        <td>/api/v1/airports/{id}</td>
        <td>Get Airport by ID</td>
        <td></td>
        <td>UUID</td>
        <td>CustomResponse&lt;AirportResponse&gt;</td>
    </tr>
    <tr>
        <td>PUT</td>
        <td>/api/v1/airports/{id}</td>
        <td>Update an Airport</td>
        <td>UpdateAirportRequest</td>
        <td>UUID</td>
        <td>CustomResponse&lt;AirportResponse&gt;</td>
    </tr>
    <tr>
        <td>DELETE</td>
        <td>/api/v1/airports/{id}</td>
        <td>Delete an Airport</td>
        <td></td>
        <td>UUID</td>
        <td>CustomResponse&lt;String&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/flights</td>
        <td>Create a new Flight</td>
        <td>CreateFlightRequest</td>
        <td></td>
        <td>CustomResponse&lt;String&gt;</td>
    </tr>
    <tr>
        <td>GET</td>
        <td>/api/v1/flights</td>
        <td>Get all Flights</td>
        <td>AirportPagingRequest</td>
        <td></td>
        <td>CustomResponse&lt;CustomPagingResponse&lt;FlightResponse&gt;&gt;</td>
    </tr>
    <tr>
        <td>GET</td>
        <td>/api/v1/flights/{id}</td>
        <td>Get Flight by ID</td>
        <td></td>
        <td>UUID</td>
        <td>CustomResponse&lt;FlightResponse&gt;</td>
    </tr>
    <tr>
        <td>PUT</td>
        <td>/api/v1/flights/{id}</td>
        <td>Update a Flight</td>
        <td>UpdateFlightRequest</td>
        <td>UUID</td>
        <td>CustomResponse&lt;FlightResponse&gt;</td>
    </tr>
    <tr>
        <td>DELETE</td>
        <td>/api/v1/flights/{id}</td>
        <td>Delete a Flight</td>
        <td></td>
        <td>UUID</td>
        <td>CustomResponse&lt;String&gt;</td>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/flights/search</td>
        <td>Search for Flights</td>
        <td>SearchFlightRequest</td>
        <td></td>
        <td>CustomResponse&lt;CustomPagingResponse&lt;FlightResponse&gt;&gt;</td>
    </tr>
</table>



### Technologies

---
- Java 21
- Spring Boot 3.0
- Restful API
- Mapstruct
- Open Api (Swagger)
- Maven
- Junit5
- Mockito
- Integration Tests
- Docker
- Docker Compose
- CI/CD (Github Actions - Jenkins)
- Postman
- TestContainer
- Mongo
- Prometheus
- Grafana
- Kubernetes
- JaCoCo (Test Report)

### Postman

```
Import postman collection under postman_collection folder
```


### Prerequisites

#### Define Variable in .env file

```
MONGO_DB_NAME=flightdatabase
MONGO_DB_HOST=localhost
MONGO_DB_PORT=27017
```

### Open Api (Swagger)

```
http://localhost:1133/swagger-ui/index.html
```

---

### JaCoCo (Test Report)

After the command named `mvn clean install` completes, the JaCoCo report will be available at:
```
target/site/jacoco/index.html
```
Navigate to the `target/site/jacoco/` directory.

Open the `index.html` file in your browser to view the detailed coverage report.

---

### Maven, Docker and Kubernetes Running Process


### Maven Run
To build and run the application with `Maven`, please follow the directions shown below;

```sh
$ cd flightsearchapi
$ mvn clean install
$ mvn spring-boot:run
```

---

### Docker Run
The application can be built and run by the `Docker` engine. The `Dockerfile` has multistage build, so you do not need to build and run separately.

Please follow directions shown below in order to build and run the application with Docker Compose file;

```sh
$ cd flightsearchapi
$ docker-compose up -d
```

If you change anything in the project and run it on Docker, you can also use this command shown below

```sh
$ cd flightsearchapi
$ docker-compose up --build
```

To monitor the application, you can use the following tools:

- **Prometheus**:  
  Open in your browser at [http://localhost:9090](http://localhost:9090)  
  Prometheus collects and stores application metrics.

- **Grafana**:  
  Open in your browser at [http://localhost:3000](http://localhost:3000)  
  Grafana provides a dashboard for visualizing the metrics.  
  **Default credentials**:
    - Username: `admin`
    - Password: `admin`

- Define prometheus data source url, use this link shown below

```
http://prometheus:9090
```

---


### Kubernetes Run
To build and run the application with `Maven`, please follow the directions shown below;

- Start Minikube

```sh
$ minikube start
```

- Open Minikube Dashboard

```sh
$ minikube dashboard
```

- To deploy the application on Kubernetes, apply the Kubernetes configuration file underneath k8s folder

```sh
$ kubectl apply -f k8s
```

- To open Prometheus, click tunnel url link provided by the command shown below to reach out Prometheus

```sh
minikube service prometheus-service
```


- To open Grafana, click tunnel url link provided by the command shown below to reach out Prometheus

```sh
minikube service grafana-service
```

- Define prometheus data source url, use this link shown below

```
http://prometheus-service.default.svc.cluster.local:9090
```


---
### Docker Image Location

```
https://hub.docker.com/repository/docker/noyandocker/flightsearchapi/general
https://hub.docker.com/repository/docker/noyandocker/flightsearchapi-jenkins/general
```

### Jenkins


### Screenshots

<details>
<summary>Click here to show the screenshots of project</summary>
    <p> Figure 1 </p>
    <img src ="screenshots/test_coverage.PNG">
</details>


### Contributors

- [Sercan Noyan Germiyanoğlu](https://github.com/Rapter1990)
- [Furkan Tarık Göçmen](https://github.com/furkantarikgocmen)