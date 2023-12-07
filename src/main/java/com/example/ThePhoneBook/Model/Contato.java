package com.example.ThePhoneBook.Model;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.Date;

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
    private Date dataNascimento;
    @Column(name = "observ")
    private String observacao;

    public Contato() {

    }

    public Contato(BigInteger idContato, String descricao, Date dataNascimento, String observacao) {
        this.idContato = idContato;
        this.descricao = descricao;
        this.dataNascimento = dataNascimento;
        this.observacao = observacao;
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "idContato=" + idContato +
                ", descricao='" + descricao + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", observacao='" + observacao + '\'' +
                '}';
    }
}
