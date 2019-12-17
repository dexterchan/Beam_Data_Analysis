package io.beam.exp.blockchaininfo.service;

import io.beam.exp.blockchaininfo.model.BitcoinState;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class BlockchainInfoBitconStatusService implements BitcoinStatusInterface{

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    HttpClient httpClient = HttpClient.newBuilder()
                .executor(executorService)
                .version(HttpClient.Version.HTTP_2)
                .build();
    //HttpClient httpClient = HttpClient.newHttpClient();
    private  BitcoinState bitcoinState=null;
    private  Map<String, Consumer<String> > bitCoinStateUpdateMap = new HashMap<>();
    public BlockchainInfoBitconStatusService() {
        bitcoinState= new BitcoinState();
        bitCoinStateUpdateMap.put("getdifficulty", (d)-> bitcoinState.setDifficulty(Double.parseDouble(d)));
        bitCoinStateUpdateMap.put("getblockcount", (b)->bitcoinState.setBlockcount(Long.parseLong(b)));
        //bitCoinStateUpdateMap.put("bcperblock", (h)->bitcoinState.setBcperblock(Long.parseLong(h)));
        bitCoinStateUpdateMap.put("totalbc", (t)->bitcoinState.setTotalbc(Long.parseLong(t)));
        bitCoinStateUpdateMap.put("probability", p->bitcoinState.setProbability(Double.parseDouble(p)));
        bitCoinStateUpdateMap.put("hashestowin", h-> bitcoinState.setHashestowin(Long.parseLong(h)));
        bitCoinStateUpdateMap.put("nextretarget", n->bitcoinState.setNextretarget(Long.parseLong(n)));
        bitCoinStateUpdateMap.put("avgtxsize", a-> bitcoinState.setAvgtxsize(Double.parseDouble(a)));
        bitCoinStateUpdateMap.put("avgtxvalue", a-> bitcoinState.setAvgtxvalue(Double.parseDouble(a)));
        bitCoinStateUpdateMap.put("interval", i->bitcoinState.setInterval(Double.parseDouble(i)));
        bitCoinStateUpdateMap.put("eta", e->bitcoinState.setEta(Math.round(Double.parseDouble(e))));
        bitCoinStateUpdateMap.put("avgtxnumber", t -> bitcoinState.setAvgtxnumber(Double.parseDouble(t)));
    }
    final static private String URL="https://blockchain.info/q/%s";
    @Override
    public BitcoinState get()  {
        BitcoinState latestBitcoinState=bitcoinState;
        try {
            List<CompletableFuture<String>> result = bitCoinStateUpdateMap.keySet().stream()
                    .map(svc->{
                        Consumer<String> dataConsumer = bitCoinStateUpdateMap.get(svc);
                        return httpClient.sendAsync(
                                HttpRequest.newBuilder(URI.create(String.format(URL, svc)))
                                        .GET()
                                        .setHeader("User-Agent", "HttpClient Bot")
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()).thenApply(
                                response -> {
                                    if(response.statusCode() == 200) {
                                        dataConsumer.accept(response.body());
                                        return String.format("%s OK", svc);
                                    }else{
                                        return String.format("%s error: %s", svc, response.body());
                                    }
                                }
                        );
                    }
                    ).collect(Collectors.toList());

            for (CompletableFuture<String> future : result) {
                log.info(future.get());
            }
            latestBitcoinState = (BitcoinState) bitcoinState.clone();
            latestBitcoinState.setTimestamp(new Date());
        }catch(Exception ioe){
            throw new IllegalStateException(ioe.getMessage());
        }
        return latestBitcoinState;
    }

    private List<Runnable> getTasks(int waitSeconds, Consumer<BitcoinState> consumer){
        BlockingQueue<BitcoinState> blockingQueue = new LinkedBlockingQueue<BitcoinState>();
        List<Runnable> tasks = new LinkedList<Runnable>();
        tasks.add( ()->{
            while(true) {
                BitcoinState bitcoinState = this.get();
                try {
                    blockingQueue.put(bitcoinState);
                    Thread.sleep(waitSeconds * 1000);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        } );
        tasks.add( ()->{
                    try{
                        while(true){
                            BitcoinState bitcoinState = blockingQueue.take();
                            consumer.accept(bitcoinState);
                            log.info(bitcoinState.toString());
                        }
                    }catch(Exception e){log.error(e.getMessage());}
                }
        );
        return tasks;
    }

    public void runPeriodic(int seconds, Consumer<BitcoinState> consumer){


        List<Runnable> tasks = getTasks(seconds, consumer);
        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executorService))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

    }

}
