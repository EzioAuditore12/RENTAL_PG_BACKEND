package com.rental_pg_backend.common.utils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtils {

    private MapUtils() {
    }

    public static <T, R> Set<R> extractKeysFromSet(Set<T> set, Function<T, R> keyExtractor) {
        if (Objects.isNull(set))
            return Collections.emptySet();
        return set.stream()
                .map(keyExtractor)
                .collect(Collectors.toSet());
    }
}
