package ru.pda.xmlSerializer;

import java.util.Collection;
import java.util.Map;

/**
 * A copy of {@link su.opencode.kefir.util.ObjectUtils}
 * to get rid of using log4j 1.2.
 */
public final class ObjectUtils {
    private ObjectUtils() {
        // private constructor for utils class
    }

    public static boolean empty(final Collection collection) {
        return (collection == null) || ( collection.isEmpty() );
    }
    public static boolean notEmpty(final Collection collection) {
        return (collection != null) && ( !collection.isEmpty() );
    }

    public static boolean empty(final Map map) {
        return (map == null) || ( map.isEmpty() );
    }
}