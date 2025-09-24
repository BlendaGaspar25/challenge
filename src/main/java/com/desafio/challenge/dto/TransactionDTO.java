package com.desafio.challenge.dto;


import java.math.BigDecimal;


public class TransactionDTO {
    private Long id;
    private String type;
    private BigDecimal valor;
    private String data;
    private BigDecimal saldoAposTransacao;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public BigDecimal getSaldoAposTransacao() { return saldoAposTransacao; }
    public void setSaldoAposTransacao(BigDecimal saldoAposTransacao) { this.saldoAposTransacao = saldoAposTransacao; }
}