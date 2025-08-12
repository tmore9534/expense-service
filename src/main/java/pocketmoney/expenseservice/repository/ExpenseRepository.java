package pocketmoney.expenseservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import pocketmoney.expenseservice.entity.ExpenseEntity;

@Repository
public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByUserId(String userId);

    List<ExpenseEntity> findByUserIdAndCreatedAtBetween(
            String userId,
            java.sql.Timestamp startDate,
            java.sql.Timestamp endDate);

    Optional<ExpenseEntity> findByUserIdAndExternalId(String userId, String externalId);

}