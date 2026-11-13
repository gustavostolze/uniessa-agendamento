package com.uniessa.agendamento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/teste")
    public String testarAPI() {
        return "Opaaa! Retorno de teste, tudo certo por aqui!";
    }

}
