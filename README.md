<div id="top"></div>


<!-- PROJECT SHIELDS -->
[![GitHub repo size][reposize-shield]](#)
[![GitHub language count][languagescount-shield]](#)
[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Gmail][gmail-shield]][gmail-url]

<h3 align="center">Final Project Developing Open Source Based Server-Side Applications: "User-Blog-Joke"</h3>

  <p align="center">
       A web application project that was developed using Spring Boot framework, and supplies various RESTful API services to users, using various libraries such as Jackson, Spring HATEOAS, Swagger UI, Data REST. 
    <br />
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#libraries-used-during-development">Libraries Used During Development</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
    </li>
    <li><a href="#contributors">Contributors</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project
This project was developed fully in Java language, using Spring Boot framework. <br>
In this project we implemented a system that manages users and blogs offering a wide variety of functionalities. The user is offered not only the basic CRUD functionality, but rather get data about users and blogs using complex queries and conditions.<br>

RESTful representations were also implemented using HATEOAS library,
Async programming was also used in this project by using CompletableFuture class,
and data was fetched API and mapped into POJOs using both RestTemplate.

In this project, one can create, get, edit and delete user(s), blog(s) and joke(s) and perform manipulations with the data stored in the database.

You may find several subjects such as: <br>
1. Java 11 Overview & Functional Programming. <br>
2. Multithreading and Java Streams. <br>
3. Concurrent Design Patterns and algorithms. <br>
4. Web Servers and Java Socket Programming. <br>
5. Spring Framework/Java EE overview: IoC and Dependency Injection. <br>
6. Introduction to Spring Boot and Web API development, Custom Exceptions and Handlers. <br>
7. Spring MVC: Beans, JPA, Hibernate, Lombok, openapi/Swagger and MVC Controllers. <br>
8. Spring Boot and RESTful representations using HATEOAS library Async programming using CompletableFuture\<V>. <br>
9. RESTful API development and advanced HTTP response manipulation and Spring Data REST. <br>
10. Advanced Hibernate relations and RESTful representations assemblers. <br>
11. DAO/DTO using Jackson and JSON properties. <br>
12. Spring @Service and async tasks. <br>

<p align="right">(<a href="#top">back to top</a>)</p>

## Libraries Used During Development

1. Swagger UI – A library that allows us to visualize and interact with the APIs resources, expanding about each and every endpoint available.
4. Lombok – A library that helps us to prevent boilerplate code in projects by using its annotations.
5. jackson-databind – A library that offers the general-purpose data-binding functionality. In this project, we used Jackson ObjectMapper.
6. H2 database  - A library that allows us to use the H2 database functionality.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

Follow these simple steps:

1. Clone the repo
   ```sh
   git clone https://github.com/AssafGolani/ServerSideSpring
   ```
2. Run the program
   ```sh
   press shift+F10 or press the "Run WebappApplication" button
   ```
   ![image](https://user-images.githubusercontent.com/62435713/180663523-ff46d032-416e-48c3-b20e-943338671ea9.png) 
3. Import requests to Postman using this link:<br>
[https://www.getpostman.com/collections/3ae56741681d12e2a393](https://www.getpostman.com/collections/573b5aa3403af19411e4)
https://www.getpostman.com/collections/88c894d72a4e5f55caaf
https://www.getpostman.com/collections/05f42928f2a4fd2d79cb

4. You may enter to H2 DB UI using this link:<br>
http://localhost:8080/h2

5. You may enter to Swagger-UI using this link:<br>
http://localhost:8080/swagger-ui/index.html


<p align="right">(<a href="#top">back to top</a>)</p>



## Contributors

We thank the following people who contributed to this project:
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/assafgolani">
        <sub>
          <b>Assaf Golani</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/YarinShlomo">
        <sub>
          <b>Yarin Shlomo</b>
        </sub>
      </a>
    </td>
        <td align="center">
      <a href="https://github.com/aviv555">
        <sub>
          <b>Aviv Raviv</b>
        </sub>
      </a>
    </td>
  </tr>
</table>



<!-- PROJECT SHIELDS -->
[![GitHub repo size][reposize-shield]](#)
[![GitHub language count][languagescount-shield]](#)
[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Gmail][gmail-shield]][gmail-url]


<!-- CONTACT -->
## Contact
[linkedin-url]: https://linkedin.com/in/assaf-golani
[gmail-shield]: https://img.shields.io/badge/1214assaf@gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white
[gmail-url]: mailto:1214assaf@gmail.com
