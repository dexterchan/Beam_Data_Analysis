package io.beam.exp.core.observe;

public interface Subject <T> {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyOservers(T msg);
}
