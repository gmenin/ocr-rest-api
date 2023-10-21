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
package com.api.ocr.aillergic.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.api.ocr.aillergic.model.Product;

/**
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
public interface ProductService {
	
	Collection<Product> findAllProducts() throws DataAccessException;
	
	Optional<Product> findProductById(String id) throws DataAccessException;
	
	Product saveProduct(Product product) throws DataAccessException;
	
	void deleteProduct(Product product) throws DataAccessException;
	
	String extractTextFromImage(MultipartFile image) throws DataAccessException, IOException;
	
	double compareSimilarity(String label, String transcription) throws DataAccessException;
}
