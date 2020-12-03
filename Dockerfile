FROM openjdk:alpine
ARG JAR_FILE=target/*.jar
ENV EXPECTANCY_S3_BUCKET=
ENV EXPECTANCY_S3_KEY= 
ENV ATHENA_S3_LOCATION=s3://
ENV CACHE_ALGO_INPUT_FEATURES=true   
ENV CACHE_ALGO_OUTPUT_FEATURES=true
ENV INPUT_FEATURES=
ENV MILEAGE_THRESHOLD=300
ENV spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
ENV spring.datasource.password=
ENV spring.datasource.url=jdbc:mariadb://127.0.0.1:147/db
ENV spring.datasource.username=root
ENV spring.jpa.database-platform=spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]