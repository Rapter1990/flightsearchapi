package com.example.demo.common.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Utility class that provides helper methods for working with lists.
 * This class contains static methods to assist with conversions and manipulations of lists.
 */
@UtilityClass
public class ListUtil {

    /**
     * Converts an object to a list of the specified type.
     * This method performs an unchecked cast of the provided object to a list of the specified type.
     *
     * @param object The object to be converted to a list.
     * @param clazz  The class type of the elements in the list.
     * @param <C>    The type of elements in the list.
     * @return A list containing the elements of the specified type.
     * @throws ClassCastException If the provided object cannot be cast to a list of the specified type.
     */
    public <C> List<C> to(Object object, Class<C> clazz) {
        return (List<C>) object;
    }

}
