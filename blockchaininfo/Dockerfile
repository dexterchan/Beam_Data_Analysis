from openjdk:14-alpine

ENV LIBNAME blockchaininfo-1.0-SNAPSHOT.jar
RUN addgroup -S app && adduser -S -G app app


RUN mkdir -p /opt/app
RUN chown app:app /opt/app
USER app
WORKDIR /opt/app


COPY build/libs/${LIBNAME} .

CMD ["sh","-c","java -jar  ${LIBNAME}"]