package io.beam.exp.core.observe;

public interface Subject <T> {
    void registerObserver( Observer<T> o);
    void removeObserver( Observer<T> o);
    void notifyOservers(T msg);
    void notifyObservers(Throwable ex);
}
