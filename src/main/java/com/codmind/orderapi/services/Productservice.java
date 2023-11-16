package com.codmind.orderapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codmind.orderapi.entity.Product;
import com.codmind.orderapi.execptions.GeneralServiceException;
import com.codmind.orderapi.execptions.NoDataFoundException;
import com.codmind.orderapi.execptions.ValidateServiceException;
import com.codmind.orderapi.repository.ProductRepository;
import com.codmind.orderapi.validators.ProductValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Productservice {

	@Autowired
	private ProductRepository productRepo;
	
	
	public Product findById(Long productId) {
		try {
			// Solo se muestra cuando estamos a nivel de DEBUG en logging.level.root
			log.debug("findById => " + productId);
			
			Product product = productRepo.findById(productId)
					.orElseThrow(() -> new NoDataFoundException("No existe el producto"));
			return product;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
		
	}
	
	@Transactional
	public void delete(Long productId) {
		try {
			Product product = productRepo.findById(productId)
					.orElseThrow(()-> new NoDataFoundException("No existe el producto"));
			
			productRepo.delete(product);	
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
		
	}
	
	public List<Product> findAll(Pageable page) {
		try {
			List<Product> products = productRepo.findAll(page).toList();
			return products;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
		
	}
	
	@Transactional
	public Product save(Product product) {
		
		try {
			ProductValidator.save(product);
			
			if (product.getId() == null) {
				
				Product newProduct = productRepo.save(product);
				return newProduct;
				
			} else {
				
				Product existProduct = productRepo.findById(product.getId())
						.orElseThrow(()-> new NoDataFoundException("No existe el producto"));
				
				existProduct.setName(product.getName());
				existProduct.setPrice(product.getPrice());
				productRepo.save(existProduct);
				
				return existProduct;
				
			}
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
		
	}
	
}
