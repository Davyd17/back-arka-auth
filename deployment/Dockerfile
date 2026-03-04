# -------- Build Stage --------
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21 AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test --no-daemon


# -------- Production Stage --------
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21 AS production

WORKDIR /app

ENV TZ=America/Bogota

COPY --from=build /app/main-app/boot/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]