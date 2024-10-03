# Elerna - E-Learning Backend Server

This is a backend server for an online learning application called Elerna.

## Description

Elerna is an online learning application. As the name suggests, this app provides students with a wide range of high-quality lessons covering various topics and fields, along with a system of assignments and contests to evaluate learners' abilities.

Accordingly, users can join different groups, enroll in courses, and freely access the available resources of those courses. The course content is typically in the form of text or PDFs to leverage the reader's comprehension skills. There will be both free and paid courses, and users will need to deposit money in order to register for courses that require payment.

Additionally, if permitted, users can also take on the role of a teacher, uploading self-edited courses to share knowledge with others, including lessons, assignments, and contests. Admins can access most of the system's resources to manage them closely.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK)**:
  - Version: JDK 8 or higher (JDK 17 is recommended)
  - You can download and install the JDK from [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.java.net/).

2. **Maven**
  - Version: 3.8 or higher version
  - You can donwload from [Maven](https://maven.apache.org/download.cgi)
3. **Docker (Optional)**
  - Version: Latest version is recommend
  - You can download from [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Installing

* Install suitable IDE for Java Spring Boot. E.g: VS Code, IntelliJ, Eclipse, v.v
* In case you want to deploy by docker, please install docker in advance.
* Clone this github by running `git clone https://github.com/trunghieu1109/Elerna.git`

## Executing program

### Run application locally

**Config Parameters:**

You need to access to `.env` file to config your DB port, username, password, v.v and application parameter.

**Build Application:**

Access to root folder of application (default is Elerna) and run the following command to build applicaiton. The result is `.jar` file in target folder.
```
mvn clean package -P dev
```

**Run application:**

You have two options to run application. First, you can run application locally by command:

```
java -jar ./target/*.jar
```

Otherwise, if you want to deploy application, you can use docker by next command:

```
docker-compose up --build -d
```

Application runs on port `80` by default, so you can config to run in other port.

## Author

Nguyen Trung Hieu 

Software Engineer Intern From Zen8Labs, iSE Lab and University of Engineering and Technology

## Contact

If you has any questions or problems, please contact with me by the following methods:

- `Email: ` ngtrunghieu1109@gmail.com
- `Linkedin: ` www.linkedin.com/in/trunghieu1109

