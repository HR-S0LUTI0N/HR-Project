package com.hrmanagement.controller;

import com.hrmanagement.dto.request.PersonelExpenseRequestDto;
import com.hrmanagement.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hrmanagement.constants.ApiUrls.EXPENSE;


@RestController
@RequestMapping(EXPENSE)
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/personnel-make-expense/{token}")
    public ResponseEntity<Boolean> personnelMakeExpense(@PathVariable String token, @RequestBody PersonelExpenseRequestDto dto){
        return ResponseEntity.ok(expenseService.personnelMakeExpense(token,dto));
    }
}
