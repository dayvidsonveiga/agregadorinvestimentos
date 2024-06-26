package br.com.agregadorinvestimentos.repository;

import br.com.agregadorinvestimentos.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
}
