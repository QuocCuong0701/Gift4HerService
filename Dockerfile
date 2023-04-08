FROM openjdk:11
LABEL owner = "cuongpq@bssd.vn"

EXPOSE 8080
ADD ./target/upload-0.0.1.jar /upload.jar

ENTRYPOINT ["java", "-jar", "upload.jar"]
# docker build -t upload-app:latest -f Dockerfile .