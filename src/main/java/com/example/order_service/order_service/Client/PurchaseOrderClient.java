package com.example.order_service.order_service.Client;

import com.example.order_service.order_service.DTOs.PoDTO.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PurchaseOrderClient {
    @Autowired
    private RestTemplate restTemplate;

    public PurchaseOrderDTO getPurchaseOrder(Long poId) {
        String url = "http://local-host/api/purchase-orders/" + poId;
        return restTemplate.getForObject(url, PurchaseOrderDTO.class);
    }
}
