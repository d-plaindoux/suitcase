package org.smallibs.suitcase.utils;

public class Pair<T1, T2> {

    public final T1 _1;
    public final T2 _2;

    public Pair(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair pair = (Pair) o;

        if (_1 != null ? !_1.equals(pair._1) : pair._1 != null) return false;
        if (_2 != null ? !_2.equals(pair._2) : pair._2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }
}
