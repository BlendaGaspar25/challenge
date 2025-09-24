package com.desafio.challenge.controller;



import com.desafio.challenge.dto.SaldoResponse;
import com.desafio.challenge.dto.TransactionDTO;
import com.desafio.challenge.model.Transaction;
import com.desafio.challenge.model.User;
import com.desafio.challenge.service.TransactionService;
import com.desafio.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacoes")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.buscarPorLogin(username).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestParam BigDecimal valor) {
        try {
            User user = getAuthenticatedUser();
            Transaction transaction = transactionService.depositar(user, valor);
            return ResponseEntity.ok("Depósito realizado com sucesso. Novo saldo: " + user.getSaldo());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    @PostMapping("/pagar")
    public ResponseEntity<?> pagarConta(@RequestParam BigDecimal valor) {
        try {
            User user = getAuthenticatedUser();
            Transaction transaction = transactionService.pagarConta(user, valor);
            return ResponseEntity.ok("Pagamento realizado com sucesso. Novo saldo: " + user.getSaldo());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar pagamento: " + e.getMessage());
        }
    }

    @GetMapping("/saldo")
    public ResponseEntity<SaldoResponse> consultarSaldo() {
        User user = getAuthenticatedUser();
        BigDecimal saldo = transactionService.consultarSaldo(user);
        List<Transaction> historico = transactionService.obterHistorico(user);

        SaldoResponse response = new SaldoResponse();
        response.setSaldoTotal(saldo.toString());
        response.setHistorico(historico.stream().map(t -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setType(t.getType().name().toLowerCase());
            dto.setValor(t.getValor());
            dto.setData(t.getData().toString());
            dto.setSaldoAposTransacao(t.getSaldoAposTransacao());
            return dto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}
