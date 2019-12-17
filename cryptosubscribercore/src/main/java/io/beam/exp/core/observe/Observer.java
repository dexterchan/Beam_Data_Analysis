package io.beam.exp.core.observe;




public interface Observer <T> {

    void update(T msg);
    void throwError(Throwable ex);
    String getDescription();
}