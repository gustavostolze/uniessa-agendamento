package com.uniessa.agendamento.model;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Profissional {
    private int id;
    private String name;
    private String especialidade;
    private String telefone;

    public void cadastrar(Service service){

    }
    public void criarAgendamento(Cliente cliente, LocalDate date, Time hora, Service service){}

    public List listarAgendamentos(LocalDate data){return new ArrayList<>();}
    public void editarAgendamento(){};
    public void excluirAgendamento(){};


}
