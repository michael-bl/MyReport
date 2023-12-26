package com.ciagrolasbrisas.myreport.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.checkerframework.checker.units.qual.K;

import java.util.concurrent.TimeUnit;

public class CacheManager {

        public CacheManager(){

        }

        private static final Cache<Object, Object> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(14, TimeUnit.HOURS) // Configura la expiración de los datos en minutos
                .maximumSize(100) // Configura el tamaño máximo de la caché
                .build();

        public static <K, V> void saveToCache(K key, V value) {
                cache.put(key, value);
        }

        public static <K, V> V getFromCache(K key) {
                return (V) cache.getIfPresent(key);
        }

        public boolean getIfExist(String key) {
                return cache.asMap().containsKey(key);
        }

        public static <K, V> void editCachedData(K key, V value) {
                if (cache.getIfPresent(key) != null) {
                        cache.put(key, value);
                }
        }
}
