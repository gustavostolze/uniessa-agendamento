package com.uniessa.agendamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor// ferramenta do lombook para gerar construtor vazio
@AllArgsConstructor// ferramenta do lombook para gerar construtor carregado
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double preco;

    @Column(nullable = false)
    private int duracaoMinutos;



}
