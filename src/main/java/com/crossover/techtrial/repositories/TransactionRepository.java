/**
 *
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author crossover
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Optional<Transaction> findByBook_IdAndDateOfReturnNull(Long bookId);

    List<Transaction> findAllByDateOfReturnBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Transaction> findAllByMember_IdAndAndDateOfReturnNull(Long memberId);
}
