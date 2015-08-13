package org.g6.laas.server.database.service;

import org.g6.laas.server.database.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepo;
}
