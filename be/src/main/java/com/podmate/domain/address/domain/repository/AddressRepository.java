package com.podmate.domain.address.domain.repository;

import com.podmate.domain.address.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
