#from openjdk:14-slim
from openjdk:14-alpine

ENV LIBNAME cryptosubscribervertx-1.0.0-SNAPSHOT-fat.jar
RUN addgroup -S app && adduser -S -G app app


RUN mkdir -p /opt/app
RUN chown app:app /opt/app
USER app
WORKDIR /opt/app


COPY build/libs/${LIBNAME} .
EXPOSE 8888
CMD ["sh","-c","java -jar ${LIBNAME}"]
