package com.mobilgetstudy.repository;

import com.mobilgetstudy.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
