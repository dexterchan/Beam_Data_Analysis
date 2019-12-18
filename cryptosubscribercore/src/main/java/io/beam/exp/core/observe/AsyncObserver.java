package io.beam.exp.core.observe;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public abstract class AsyncObserver<T> implements Observer<T> {
    BlockingQueue asyncQueue = new LinkedBlockingQueue();
    BlockingDeque asyncErrQueue = new LinkedBlockingDeque();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    public enum Status{
        START, END
    }
    @Getter @Setter
    Status status = Status.START;

    @Override
    public void update(T msg){
        try {
            asyncQueue.put(msg);
            executor.execute(()->{
                while (this.status == Status.START) {
                    try {
                        T rmsg = (T) asyncQueue.take();
                        this.asyncUpdate(rmsg);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throwError(e);
                    }
                }
            });
        }catch(InterruptedException e){
            log.error(e.getMessage());
            throwError(e);
        }
    }

    @Override
    public void throwError(Throwable ex) {


        try {
            this.asyncErrQueue.put(ex);
            executor.execute(()->{
                while (this.status == Status.START) {
                    try {
                        Throwable t = (Throwable) asyncErrQueue.take();
                        this.asyncThrowError(t);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        this.asyncThrowError(e);
                    }
                }
            });
        }catch(InterruptedException e){
            log.error(e.getMessage());
            this.asyncThrowError(e);
        }
    }

    public abstract void asyncUpdate(T msg);

    public abstract void asyncThrowError( Throwable ex);


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
