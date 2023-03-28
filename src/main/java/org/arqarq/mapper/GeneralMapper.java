package org.arqarq.mapper;

import org.apache.commons.lang3.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

public interface GeneralMapper<T, U> {
    default T toJson(U entity) {
        throw new NotImplementedException();
    }

    default List<T> toJsonList(List<U> entities) {
        List<T> jsons = new LinkedList<>();

        entities.forEach(el -> jsons.add(toJson(el)));
        return jsons;
    }

    default U toEntity(T json) {
        throw new NotImplementedException();
    }

    default List<U> toEntities(List<T> jsons) {
        List<U> entities = new LinkedList<>();

        jsons.forEach(e -> entities.add(toEntity(e)));
        return entities;
    }
}