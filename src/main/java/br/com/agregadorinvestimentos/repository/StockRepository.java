package br.com.agregadorinvestimentos.repository;

import br.com.agregadorinvestimentos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}
