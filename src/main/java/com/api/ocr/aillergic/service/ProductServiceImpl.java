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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.ocr.aillergic.model.Product;
import com.api.ocr.aillergic.repository.ProductRepository;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
@Service
public class ProductServiceImpl implements ProductService {
	
	private ProductRepository repository;

	@Autowired
	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Product> findAllProducts() throws DataAccessException {
		try {
			return repository.findAll();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findProductById(String id) throws DataAccessException {
		try {
			return repository.findById(id);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Product saveProduct(Product product) throws DataAccessException {
		try {
			return repository.save(product);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void deleteProduct(Product product) throws DataAccessException {
		try {
			repository.delete(product);
		} catch (Exception e) {
			
		}
	}

	@Override
	public String extractTextFromImage(MultipartFile image) throws DataAccessException, IOException {
		String transcription = "";
		
		File file = new File(image.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(image.getBytes());
		fos.close();  
		
		// Configuracao Tesseract lib
		Tesseract tesseract = new Tesseract();		
		tesseract.setDatapath("src/main/resources/tessdata");
		tesseract.setLanguage("por");
		
		try {
			// Extracao do texto da imagem
			transcription = tesseract.doOCR(file);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file.delete();
		return transcription.replaceAll("\\R", " ");
	}

	@Override
	public double compareSimilarity(String label, String transcription) throws DataAccessException {
		JaroWinklerSimilarity sim = new JaroWinklerSimilarity();
		return Math.round((sim.apply(label, transcription)*100.0)*100.0)/100.0;
	}

}
