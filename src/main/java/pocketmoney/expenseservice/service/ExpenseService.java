package pocketmoney.expenseservice.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import pocketmoney.expenseservice.dto.ExpenseDto;
import pocketmoney.expenseservice.entity.ExpenseEntity;
import pocketmoney.expenseservice.repository.ExpenseRepository;

@Service
@AllArgsConstructor
public class ExpenseService {
    private ExpenseRepository expenseRepository;

    private ObjectMapper objectMapper;

    @Autowired
    ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
        this.objectMapper = new ObjectMapper();
    }

    public boolean createExpense(ExpenseDto expenseDto) {
        try {
            this.setCurrency(expenseDto);
            expenseRepository.save(objectMapper.convertValue(expenseDto, ExpenseEntity.class));
            return true;
        } catch (Exception e) {
            System.out.println("Error creating expense: " + e.getMessage());
            return false;
        }

    }

    // TODO: Add lock (Redis or DB) during the creation and update of expense.
    // fetch from SQL --> Buisness Logic --> Update to SQL
    // To avoid fetching stale data use locks.
    public boolean updateExpense(ExpenseDto expenseDto) {
        setCurrency(expenseDto);
        Optional<ExpenseEntity> expenseFoundOpt = expenseRepository.findByUserIdAndExternalId(expenseDto.getUserId(),
                expenseDto.getExternalId());
        if (expenseFoundOpt.isEmpty()) {
            return false;
        }

        ExpenseEntity expense = expenseFoundOpt.get();
        expense.setAmount(expenseDto.getAmount());
        expense.setMerchant(
                Strings.isNotBlank(expenseDto.getMerchant()) ? expenseDto.getMerchant() : expense.getMerchant());
        expense.setCurrency(
                Strings.isNotBlank(expenseDto.getCurrency()) ? expenseDto.getMerchant() : expense.getCurrency());
        expenseRepository.save(expense);
        return true;
    }

    public List<ExpenseDto> getExpenses(String userId) {
        List<ExpenseEntity> expenseOpt = expenseRepository.findByUserId(userId);
        return objectMapper.convertValue(expenseOpt, new TypeReference<List<ExpenseDto>>() {
        });
    }

    // pass by reference and update.
    public void setCurrency(ExpenseDto expenseDto) {
        if (Objects.isNull(expenseDto.getCurrency())) {
            expenseDto.setCurrency("INR");
        }
    }

}
