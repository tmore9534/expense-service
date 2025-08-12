package pocketmoney.expenseservice.deserializer;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import pocketmoney.expenseservice.dto.ExpenseDto;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {

    @Override
    public void close() {
    }

    @Override
    public void configure(java.util.Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public ExpenseDto deserialize(String arg0, byte[] args1) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseDto expenseDto = null;
        try {
            expenseDto = objectMapper.readValue(args1, ExpenseDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expenseDto;
    }
}
