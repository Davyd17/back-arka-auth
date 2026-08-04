# -------- Build Stage --------
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-jdk AS build

WORKDIR /app

RUN yum install -y findutils

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test --no-daemon


# -------- Production Stage --------
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless AS production

WORKDIR /app

ENV TZ=America/Bogota

COPY --from=build /app/main-app/boot/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
