FROM gradle:7.1.0-jdk11

USER root

RUN useradd -ms /bin/bash wasadm

RUN rm -rf /app
RUN mkdir -p /app

RUN chown -R wasadm:wasadm /app

RUN git clone https://github.com/lee-code712/agent.git /app/agent
RUN git clone https://github.com/lee-code712/Commerce-Common.git /app/Commerce-Common
COPY . /app/Commerce-Order

WORKDIR /app/Commerce-Order

RUN cd /app/Commerce-Order
RUN gradle build

CMD ["java", "-Djennifer.config=/app/agent/conf/order.conf","-javaagent:/app/agent/jennifer.jar", "-jar", "/app/Commerce-Order/build/libs/Commerce-Order-0.0.1-SNAPSHOT.jar"]