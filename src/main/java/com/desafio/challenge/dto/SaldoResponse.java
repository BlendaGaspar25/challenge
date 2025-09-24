package com.desafio.challenge.dto;

import java.util.List;

public class SaldoResponse {
    private String saldoTotal;
    private List<TransactionDTO> historico;

    // Getters e Setters
    public String getSaldoTotal() { return saldoTotal; }
    public void setSaldoTotal(String saldoTotal) { this.saldoTotal = saldoTotal; }

    public List<TransactionDTO> getHistorico() { return historico; }
    public void setHistorico(List<TransactionDTO> historico) { this.historico = historico; }
}
