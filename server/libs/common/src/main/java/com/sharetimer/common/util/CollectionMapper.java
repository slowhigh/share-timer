package com.sharetimer.common.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class CollectionMapper {

  private CollectionMapper() {}

  public static <T, R> List<R> map(Collection<T> source, Function<T, R> mapper) {
    return source == null ? List.of() : source.stream().map(mapper).toList();
  }

}
