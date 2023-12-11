package com.example.ThePhoneBook.Model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "contel")
public class TelefoneContato {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idTelefoneContato;
    @Column(name = "numero", length = 10)
    private String telefone;

    @Column(name = "dddnum", length = 2)
    private String ddd;

    @Column(name = "emails", length = 250)
    private String email;

    @ManyToOne
    @JoinColumn(name = "idcontato")
    private Contato contato;

    public TelefoneContato() {
    }

    public TelefoneContato(Long idTelefoneContato, String telefone, String ddd, String email, Contato contato) {
        this.idTelefoneContato = idTelefoneContato;
        this.telefone = telefone;
        this.ddd = ddd;
        this.email = email;
        this.contato = contato;
    }

    public Long getIdTelefoneContato() {
        return idTelefoneContato;
    }

    public void setIdTelefoneContato(Long idTelefoneContato) {
        this.idTelefoneContato = idTelefoneContato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @Override
    public String toString() {
        return "TelefoneContato{" +
                "idTelefoneContato=" + idTelefoneContato +
                ", telefone='" + telefone + '\'' +
                ", ddd='" + ddd + '\'' +
                ", email='" + email + '\'' +
                ", contato=" + contato +
                '}';
    }
}
