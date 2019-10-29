
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
