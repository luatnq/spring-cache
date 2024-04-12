# Stage 1: Build the application
FROM maven:3.8.7 AS deps

ARG WORKDIR=/opt/app

WORKDIR ${WORKDIR}
COPY pom.xml .
RUN mvn dependency:go-offline

# Stage 2: Build the application
FROM maven:3.8.7 AS builder

ARG WORKDIR=/opt/app
WORKDIR ${WORKDIR}
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app

COPY ./src ./src
RUN mvn clean package -DskipTests

# Stage 3: Extract file jar for application image
FROM eclipse-temurin:17-jre-alpine AS extractor

ARG WORKDIR=/opt/app
ARG JAR_FILE=${WORKDIR}/target/*.jar

WORKDIR ${WORKDIR}
COPY --from=builder ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract


# Stage 4: Create the final image
FROM eclipse-temurin:17-jre-alpine
ARG WORKDIR=/opt/app
WORKDIR ${WORKDIR}

# Set the timezone.
ENV TZ=Asia/Ho_Chi_Minh
RUN set -x \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

RUN addgroup twoup && adduser -S twoup -G twoup
RUN chown -R twoup:twoup /opt/app
USER twoup

COPY --from=extractor ${WORKDIR}/dependencies/ ./
COPY --from=extractor ${WORKDIR}/spring-boot-loader/ ./
COPY --from=extractor ${WORKDIR}/snapshot-dependencies/ ./
COPY --from=extractor ${WORKDIR}/application/ ./
ENTRYPOINT ["java", "-XX:+UseG1GC", "-Xms512m", "-Xmx768m", "-Xss512k", "-XX:MaxRAM=72m", "org.springframework.boot.loader.launch.JarLauncher"]


