package com.desafio.challenge.service;


import com.desafio.challenge.model.Transaction;
import com.desafio.challenge.model.User;
import com.desafio.challenge.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TransactionService {

    private static final BigDecimal JURO_NEGATIVACAO = new BigDecimal("1.02");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CacheService cacheService;

    @Transactional
    @CacheEvict(value = "saldo", key = "#user.id")
    public Transaction depositar(User user, BigDecimal valor) {
        BigDecimal saldoAnterior = user.getSaldo();
        BigDecimal novoSaldo;

        // Aplicar juros se estiver negativo
        if (saldoAnterior.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal divida = saldoAnterior.abs().multiply(JURO_NEGATIVACAO);
            novoSaldo = valor.subtract(divida);
        } else {
            novoSaldo = saldoAnterior.add(valor);
        }

        user.setSaldo(novoSaldo);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(Transaction.TransactionType.DEPOSITO);
        transaction.setValor(valor);
        transaction.setSaldoAposTransacao(novoSaldo);

        Transaction saved = transactionRepository.save(transaction);

        // Atualizar cache
        cacheService.atualizarCacheSaldo(user.getId(), novoSaldo);

        return saved;
    }

    @Transactional
    @CacheEvict(value = "saldo", key = "#user.id")
    public Transaction pagarConta(User user, BigDecimal valor) {
        BigDecimal saldoAnterior = user.getSaldo();
        BigDecimal novoSaldo = saldoAnterior.subtract(valor);

        user.setSaldo(novoSaldo);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(Transaction.TransactionType.PAGAMENTO);
        transaction.setValor(valor);
        transaction.setSaldoAposTransacao(novoSaldo);

        Transaction saved = transactionRepository.save(transaction);

        // Atualizar cache
        cacheService.atualizarCacheSaldo(user.getId(), novoSaldo);

        return saved;
    }

    public List<Transaction> obterHistorico(User user) {
        return transactionRepository.findByUserOrderByDataDesc(user);
    }

    public BigDecimal consultarSaldo(User user) {
        // Tentar obter do cache primeiro
        BigDecimal saldoCache = cacheService.obterSaldoCache(user.getId());
        if (saldoCache != null) {
            return saldoCache;
        }

        // Se não estiver em cache, buscar do banco e atualizar cache
        BigDecimal saldo = user.getSaldo();
        cacheService.atualizarCacheSaldo(user.getId(), saldo);

        return saldo;
    }
}