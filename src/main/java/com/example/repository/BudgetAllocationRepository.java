package com.example.repository;

import com.example.domain.BudgetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> {
}
