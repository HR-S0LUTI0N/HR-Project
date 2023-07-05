package com.hrmanagement.service;

import com.hrmanagement.repository.entity.Debt;
import com.hrmanagement.repository.IDebtRepository;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService extends ServiceManager<Debt, String> {
    private final IDebtRepository debtRepository;
    public DebtService(IDebtRepository debtRepository){
        super(debtRepository);
        this.debtRepository = debtRepository;
    }

    public List<Debt> findAllByUserId(String userId){
        List<Debt> debtList = debtRepository.findAllByUserId(userId);
        return debtList;
    }







}
