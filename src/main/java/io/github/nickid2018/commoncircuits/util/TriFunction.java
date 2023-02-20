package io.github.nickid2018.commoncircuits.util;

public interface TriFunction<T, U, V, W> {

    W apply(T t, U u, V v);
}
