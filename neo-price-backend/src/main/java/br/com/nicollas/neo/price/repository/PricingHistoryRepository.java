package br.com.nicollas.neo.price.repository;

import br.com.nicollas.neo.price.domain.model.PricingHistory;
import br.com.nicollas.neo.price.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PricingHistoryRepository extends JpaRepository<PricingHistory, Long> {

    List<PricingHistory> findByUser(User user);

    Optional<PricingHistory> findBypricingHistoryIdAndUser(Long id, User loggedUser);

}
