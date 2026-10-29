package com.uniessa.agendamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "profissional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String especialidade;

    private String telefone;

    // Galera... O mappedBy="profissional" diz ao JPA: "A chave estrangeira já tá configurada
    // lá na classe Servico no atributo 'profissional'. Não precisa criar tabela extra. Relação bidirecional"
    @OneToMany(mappedBy = "profissional")
    private List<Servico> servicos;
}
