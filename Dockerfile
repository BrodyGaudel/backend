FROM openjdk:21-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Setting environment variables
ENV MYSQL_USER=root
ENV MYSQL_PWD=root
ENV MYSQL_HOST=172.20.0.2
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=products_db

# Copying your jar application into the container
COPY target/backend-0.0.1.jar backend.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","backend.jar"]