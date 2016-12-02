package com.vdms.api;

import com.vdms.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kenji on 12/2/2016.
 */
@Service
public class ProductService {
    private static  final ConcurrentMap<String, Product> products;

    static {
        products = new ConcurrentHashMap<>();
    }


}
