package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.event.OrderEvent;
import com.mobilgetstudy.model.Invoice;
import com.mobilgetstudy.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "invoice-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderEvent(OrderEvent event) {
        Invoice invoice = new Invoice();
        invoice.setOrderId(event.getOrderId());
        invoice.setTotalAmount(event.getTotalAmount());
        invoice.setInvoiceDate(event.getOrderDate());

        invoiceRepository.save(invoice);
        System.out.println("Invoice created for order: " + event.getOrderId());
    }
}
