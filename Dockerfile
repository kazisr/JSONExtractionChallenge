FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y tesseract-ocr tesseract-ocr-eng libtesseract-dev libleptonica-dev && \
    apt-get clean

WORKDIR /app

COPY target/kazi-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
