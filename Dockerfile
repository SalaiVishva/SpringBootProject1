FROM maven:3.8.4-openjdk-21 AS build

    #set the working directory
    WORKDIR /app

    #coopy pom and install dependency
    COPY pom.xml .
    RUN mvn dependency:go-offline

    #copy source code and the build the application
    COPY src ./src
    RUN mvn clean package -DskipTests

    #use an official openjdk image to run the application
    FROM openjdk:21-jdk-slim

    #set the working directory
    WORKDIR /app

    #copy the built JAR file from the built stage
    COPY --from=build /app/target/Hotelproject-0.0.1-SNAPSHOT.jar .

    #EXPOSE PORT 8080
    EXPOSE 8080

#SPECIFIC THE COMMENT TO RUN APPLICATION
ENTRYPOINT ["java","-jar","/app/Hotelproject-0.0.1-SNAPSHOT.jar"]