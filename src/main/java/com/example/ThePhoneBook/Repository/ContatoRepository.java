package com.example.ThePhoneBook.Repository;

import com.example.ThePhoneBook.Model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigInteger;
import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, BigInteger> {
    List<Contato> findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(String descricao);
    List<Contato> findAllByOrderByDescricaoAsc();
}
