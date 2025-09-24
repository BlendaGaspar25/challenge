package com.desafio.challenge.service;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CacheService {

    private static final String CACHE_NAME = "saldo";

    @Cacheable(value = CACHE_NAME, key = "#userId")
    public BigDecimal obterSaldoCache(Long userId) {
        // Retorna null se não estiver em cache, forçando busca no banco
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "#userId")
    public BigDecimal atualizarCacheSaldo(Long userId, BigDecimal saldo) {
        return saldo;
    }
}