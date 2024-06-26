package br.com.agregadorinvestimentos.repository;

import br.com.agregadorinvestimentos.entity.AccountStock;
import br.com.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
