
## Design Document
Divides into modules:
1) realtime cryptosubscriber <br>
A executable connecting to Crypto exchange via module cryptorealtime.
Now, it temporally subscribe to "hitbtc" via API 
[XChange-stream Java API](https://github.com/bitrich-info/xchange-stream)

    The module supports Restful API to control the subscription.




## Boilerplate

Build
````
./gradlew clean build
````
Checking
````
java -classpath boilerplate/build/libs/boilerplate-1.0-SNAPSHOT.jar  io.beam.exp.boilerplate.Main
````

## Docker 
build
````
docker build -t gcr.io/peer2peer/cryptosubscribercore:v0.1 .

docker run -it --rm -v /home/humble_pig_2019_mar/.ssh:/tmp -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp.serviceacct.peer2peer-67bc368759d4.json gcr.io/peer2peer/cryptosubscribercore:alpine

docker build -t gcr.io/peer2peer/cryptosubscribervertx:v0.2 .

docker build -t gcr.io/peer2peer/blockchaininfo:v0.2 .

docker run -p 80:8888 --restart unless-stopped -it -v /home/humble_pig_2019_mar:/tmp -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp.serviceacct.peer2peer-67bc368759d4.json gcr.io/peer2peer/cryptosubscribervertx:v0.1

docker run -p 80:8888 -it --rm -v ~/.ssh/pigpig/:/tmp -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp.serviceacct.peer2peer-67bc368759d4.json  gcr.io/peer2peer/cryptosubscribercore:v0.1

docker run -p 80:8888 -it --rm -v ~/.ssh/pigpig/:/tmp -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp.serviceacct.peer2peer-67bc368759d4.json  gcr.io/peer2peer/cryptosubscribervertx:v0.2

docker run -p 80:8888 -it --rm -v ~/.ssh/pigpig/:/tmp -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp.serviceacct.peer2peer-67bc368759d4.json  gcr.io/peer2peer/blockchaininfo:alpine

````