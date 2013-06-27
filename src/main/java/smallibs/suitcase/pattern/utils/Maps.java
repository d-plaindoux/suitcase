package smallibs.suitcase.pattern.utils;

import smallibs.suitcase.pattern.Cases;
import smallibs.suitcase.pattern.core.Case;
import smallibs.suitcase.utils.Option;

import java.util.List;
import java.util.Map;

public class Maps {
    public static <T1, T2> Case<Map<T1, T2>> Entry(T1 o1, Object o2) {
        return new Entry<>(o1, o2);
    }

    static class Entry<T1, T2> implements Case<Map<T1, T2>> {

        private final T1 key;
        private final Case<T2> valCase;

        Entry(T1 key, Object o2) {
            this.key = key;
            this.valCase = Cases.fromObject(o2);
        }

        @Override
        public Option<List<Object>> unapply(Map<T1, T2> map) {
            if (map.containsKey(this.key)) {
                return this.valCase.unapply(map.get(this.key));
            } else {
                return new Option.None<>();
            }
        }
    }

}
