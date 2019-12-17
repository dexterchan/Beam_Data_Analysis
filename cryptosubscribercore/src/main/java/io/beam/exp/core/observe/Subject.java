package io.beam.exp.core.observe;

import java.util.Map;

public interface Subject <T> {
    void registerObserver( Observer<T> o);
    void removeObserver( Observer<T> o);
    void notifyOservers(T msg);
    void notifyObservers(Throwable ex);
    void setActive(boolean isActive);
    boolean isActive();

    Map<String, String> getDescription();
}
