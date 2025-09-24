package com.desafio.challenge.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal valor;

    private LocalDateTime data = LocalDateTime.now();

    private BigDecimal saldoAposTransacao;

    public enum TransactionType {
        DEPOSITO, SAQUE, PAGAMENTO
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public BigDecimal getSaldoAposTransacao() { return saldoAposTransacao; }
    public void setSaldoAposTransacao(BigDecimal saldoAposTransacao) { this.saldoAposTransacao = saldoAposTransacao; }
}