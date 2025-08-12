package pocketmoney.expenseservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocketmoney.expenseservice.dto.ExpenseDto;
import pocketmoney.expenseservice.service.ExpenseService;

@RequiredArgsConstructor
@Service
public class ExpenseConsumer {

    private ExpenseService expenseService;

    @Autowired
    ExpenseConsumer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ExpenseDto expenseDto) {
        try {
            expenseService.createExpense(expenseDto);
        } catch (Exception e) {
            System.err.println("Error processing expense: " + e.getMessage());
            e.printStackTrace();
        }
    }

}