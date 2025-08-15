# studentManagement
Student Management Angular Spring Boot application

Student Management application is Angular + Spring Boot microservices application where users can manage students and their enrolled courses. Each student can be enrolled in multiple courses. The app should allow users to create, read, update, and delete both students and courses.

Setup Spring Boot
- Set up database in Postgresql.
  - In Command Line console, type "psql -h localhost -p 5432 -d postgres -U postgres -f springboot/StudentManagement/src/main/resources/sql/init_db.sql"
- Set up Spring Boot microservices
  - Open springboot/StudentManagement/src/main/resources/application.properties
    - Change spring.datasource.username, and spring.datasource.password as your local Postgresql.
  - In Command Line console, go to the "springboot" directory.
  - .\gradlew clean bootJar
  - java -jar ./build/libs/StudentManagement-0.0.1-SNAPSHOT.jar

Setup Angular instruction
- In Command Line console, go to the "angular" directory.
- Type "npm install" to install Angular dependencies.
- Type "ng serve" to start the server

In web browser, open URL "http://localhost:4200/"

