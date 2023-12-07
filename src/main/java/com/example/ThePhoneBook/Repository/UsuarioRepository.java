package com.example.ThePhoneBook.Repository;

import com.example.ThePhoneBook.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        Usuario findByEmailAndSenha(String email, String senha);

        Usuario findByEmail(String email);
}
