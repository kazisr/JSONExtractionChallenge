FROM openjdk:17-slim


RUN apt-get update && \
    apt-get install -y tesseract-ocr tesseract-ocr-eng libtesseract-dev libleptonica-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV TESSDATA_PREFIX=/src/main/resources/tessdata

WORKDIR /app


COPY target/kazi-0.0.1-SNAPSHOT.jar app.jar




EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
