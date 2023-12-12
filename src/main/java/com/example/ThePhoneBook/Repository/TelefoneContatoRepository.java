package com.example.ThePhoneBook.Repository;

import com.example.ThePhoneBook.Model.Contato;
import com.example.ThePhoneBook.Model.TelefoneContato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelefoneContatoRepository extends JpaRepository<TelefoneContato, Long> {
    List<TelefoneContato> findByContatoInOrderByTelefoneAsc(List<Contato> idcontato);
    List<TelefoneContato> findAllByOrderByTelefoneAsc();
    List<TelefoneContato> findByContatoOrderByTelefoneAsc(Contato idcontato);
}
