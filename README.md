<h1 align="center">AIllergic OCR REST API</h1>
<br>
<p align="justify">
  This project aims to develop a REST API that uses Optical Character Recognition (OCR) techniques to extract text from food product labels and return information about allergenic ingredients present in them. This information will be used by a mobile application that will consume this REST API to, together with other functionalities being developed outside the scope of this work, identify the presence of allergens and inform the user if the label contains any allergenic substance. This identification is intended to help patients with food allergies make informed decisions about their diet. As a result, the REST API developed in this work, when integrated into the mobile application flow, aims to provide a better quality of life for people with food allergies, by providing a reliable tool for identifying possible allergens present in the food they consume.
</p>

## Table of Contents
- [Goals](#goals)
- [Technologies](#technologies)
- [What you will need](#what-you-will-need)
- [Entities](#entities)
- [Configuring and running the project](#configuring-and-running-the-project)


## Goals
* Extract text from a food product label image
* Create, read and delete a product, as a way to test API efficiency by comparing the similarity of the actual label and the resulting transcript;
* Calculate the similarity of the actual label and the resulting transcript;
* Data must be persisted in a database (MongoDB);
* The API must be built in Java with Spring;
* Coverage must be greater than 50%;


## Technologies
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Tools"/> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java"/> 
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Graddle"/>
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
<img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white" alt="Postman"/>


## What you will need
### (Required)
* <a href="https://www.oracle.com/java/technologies/javase-downloads.html">Java 17+</a> 

* <a href="https://gradle.org/install/">Gradle 8+</a> 

### (Optional)
IDE used in the project:
* <a href="https://spring.io/tools">Spring Tool Suite (STS)</a>


## Entity
![image](https://github.com/meuplow/ocr-rest-api/assets/57534895/fd6635ae-8cd1-473c-850a-b1517228dd98)


## Configuring and running project locally

```shell
# clone the repository and access the directory
$ git clone git@github.com:meuplow/ocr-rest-api.git && cd ocr-rest-api

# running the application
$ gradle bootrun

# running tests 
$ gradle test
```


## OpenAPI REST API documentation presented here (after application start):
You can reach the swagger UI with this URL http://localhost:8080/.
