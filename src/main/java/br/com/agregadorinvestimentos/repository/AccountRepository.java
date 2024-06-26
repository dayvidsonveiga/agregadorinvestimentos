package br.com.agregadorinvestimentos.repository;

import br.com.agregadorinvestimentos.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
