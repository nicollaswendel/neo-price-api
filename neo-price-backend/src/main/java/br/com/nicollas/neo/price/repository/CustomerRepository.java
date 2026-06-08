package br.com.nicollas.neo.price.repository;

import br.com.nicollas.neo.price.domain.model.Customer;
import br.com.nicollas.neo.price.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    List<Customer> findByUser(User user);

    Optional<Customer> findByCustomerIdAndUser(Long customerId, User user);

}
