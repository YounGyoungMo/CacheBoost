package com.example.CacheBoost.domain.address.repository;

import com.example.CacheBoost.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
