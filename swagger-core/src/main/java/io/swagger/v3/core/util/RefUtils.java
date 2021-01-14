package io.swagger.v3.core.util;


import org.rainday.swagger.tuple.ImmutablePair;
import org.rainday.swagger.tuple.Pair;
import org.rainday.swagger.utils.StringUtils;

public class RefUtils {

    public static String constructRef(String simpleRef) {
        return "#/components/schemas/" + simpleRef;
    }

    public static String constructRef(String simpleRef, String prefix) {
        return prefix + simpleRef;
    }

    public static Pair extractSimpleName(String ref) {
        int idx = ref.lastIndexOf('/');
        if (idx > 0) {
            String simple = ref.substring(idx + 1);
            if (!StringUtils.isBlank(simple)) {
                return new ImmutablePair(simple, ref.substring(0, idx + 1));
            }
        }
        return new ImmutablePair<>(ref, null);

    }
}
