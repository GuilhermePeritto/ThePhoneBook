package com.example.ThePhoneBook.Repository;

import com.example.ThePhoneBook.Model.TelefoneContato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneContatoRepository extends JpaRepository<TelefoneContato, Long> {
}
