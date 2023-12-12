package com.example.ThePhoneBook.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.springframework.cglib.core.Local;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contat")
public class Contato {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigInteger idContato;
    @Column(name = "descri")
    private String descricao;
    @Column(name = "datnas")
    private LocalDate dataNascimento;
    @Column(name = "observ")
    private String observacao;

    private String localImagem;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<TelefoneContato> contels;

    public Contato() {

    }

    public Contato(BigInteger idContato, String descricao, LocalDate dataNascimento, String observacao, String localImagem) {
        this.idContato = idContato;
        this.descricao = descricao;
        this.dataNascimento = dataNascimento;
        this.observacao = observacao;
        this.localImagem = localImagem;
    }

    public BigInteger getIdContato() {
        return idContato;
    }

    public void setIdContato(BigInteger idContato) {
        this.idContato = idContato;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getLocalImagem() {
        return localImagem;
    }

    public void setLocalImagem(String localImagem) {
        this.localImagem = localImagem;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
