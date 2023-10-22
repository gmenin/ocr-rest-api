/**
 * Copyright 2023 AIllergy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.api.ocr.aillergic.rest.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.ocr.aillergic.mapper.ProductRequestMapper;
import com.api.ocr.aillergic.mapper.ProductResponseMapper;
import com.api.ocr.aillergic.model.Product;
import com.api.ocr.aillergic.rest.dto.ProductRequest;
import com.api.ocr.aillergic.rest.dto.ProductResponse;
import com.api.ocr.aillergic.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
@RestController
@RequestMapping("/api/v1/ocr")
public class ProductRestController {
	
	@Autowired
	ProductService service;
	
	@Autowired
    ProductRequestMapper requestMapper;

    @Autowired
    ProductResponseMapper responseMapper;
    
    @Operation(summary = "Busca por todos os produtos cadastrados na base de dados")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "404", description = "Nenhum produto encontrado"),
    		@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Product>> getAllProducts() {	
		
		Collection<Product> products = this.service.findAllProducts();

		if (products == null) {
			return new ResponseEntity<Collection<Product>>(HttpStatus.NOT_FOUND);
		}
		
		if (products.isEmpty()) {
			return new ResponseEntity<Collection<Product>>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Collection<Product>>(products, HttpStatus.OK);
	}
    
    @Operation(summary = "Busca pelo id por um específico produto cadastrado base de dados")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
    		@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping(value = "/test/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
		
		Optional<Product> product = this.service.findProductById(id);
		
		if (product.isEmpty()) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
	}
    
    @Operation(summary = "Deleta um específico produto cadastrado base de dados")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
    		@ApiResponse(responseCode = "204", description = "Operação realizada com sucesso")
    })
    @DeleteMapping(value = "/test/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {

		Optional<Product> product = this.service.findProductById(id);

		if (product.isEmpty()) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}

		this.service.deleteProduct(product.get());

		return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
	}
    
    @Operation(summary = "Cadastra um produto na base de dados")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
    		@ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    })
    @PostMapping(value = "/test", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> addProduct(@RequestPart(name = "file") MultipartFile file, @RequestPart(name = "data") ProductRequest productRequest) throws IOException {

		if (productRequest == null || file == null) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
		
		Product product = this.requestMapper.productRequestToProduct(productRequest);
		
		String transcription = this.service.extractTextFromImage(file);
		product.setTranscription(transcription);
		
		double similarity = this.service.compareSimilarity(product.getLabel(), transcription);
		product.setSimilarity(similarity);

		Product savedProduct = this.service.saveProduct(product);

		return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
	}
    
    @Operation(summary = "Extrai o texto do rótulo alimentar presente na imagem")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
    		@ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> translateFoodLabelImage(@RequestParam(name = "file") MultipartFile file) throws IOException {
		
		if (file.isEmpty()) {
			return new ResponseEntity<ProductResponse>(HttpStatus.BAD_REQUEST);
		}
		
		String transcription = this.service.extractTextFromImage(file);
		
		Product product = new Product();
		product.setTranscription(transcription);
		
		ProductResponse response = this.responseMapper.productToProductResponse(product);
		
		return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
	}
}
