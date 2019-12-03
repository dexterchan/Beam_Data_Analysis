package io.beam.exp.core.observe;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public abstract class AsyncObserver<T> implements Observer<T> {
    BlockingQueue asyncQueue = new LinkedBlockingQueue();
    ExecutorService executor = Executors.newCachedThreadPool();

    public void update(T msg){
        try {
            asyncQueue.put(msg);
            executor.execute(()->{
                try {
                    T rmsg = (T) asyncQueue.take();
                    this.asyncUpdate(rmsg);
                }catch(Exception ex){
                    log.error(ex.getMessage());
                }
            });
        }catch(InterruptedException ex){
            log.error(ex.getMessage());
        }
    }
    public abstract void asyncUpdate(T msg);
}
