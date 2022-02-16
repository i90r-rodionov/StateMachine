package org.example.domain.repository.impl;

import org.example.domain.repository.LoanApplicationRepository;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationRepositoryImpl implements LoanApplicationRepository {
    @Override
    public void trace() {
        LoanApplicationRepository.super.trace();
    }
}
