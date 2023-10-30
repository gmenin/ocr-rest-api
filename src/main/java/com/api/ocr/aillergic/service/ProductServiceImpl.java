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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.ocr.aillergic.model.Product;
import com.api.ocr.aillergic.repository.ProductRepository;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;

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
		
		OpenCV.loadShared();
		
		//Process p = new ProcessBuilder("python", "myScript.py", "firstargument").start();
		
		String transcription = "";
		
		File file = new File(image.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(image.getBytes());
		fos.close();  
		
		Mat destination = new Mat();
		Mat source = Imgcodecs.imread(file.getAbsolutePath());
		
		for (int i = 0; i < 2; i++) {
			destination = new Mat(source.rows(), source.cols(), source.type());
			Imgproc.GaussianBlur(source, destination, new Size(0,0), 10);
			Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);	
			Imgcodecs.imwrite(file.getAbsolutePath(), destination);
			source = destination;
		}
//		Mat destination = new Mat();
//		Mat source = Imgcodecs.imread(file.getAbsolutePath());
//		
//		destination = new Mat(source.rows(), source.cols(), source.type());
//		
//		// Resize the image with INTER_CUBIC interpolation
//		Size newSize = new Size(source.width() * 2, source.height() * 2);
//		Imgproc.resize(source, destination, newSize, 0, 0, Imgproc.INTER_CUBIC);
//
//		// Convert the resized image to grayscale
//		Mat grayImage = new Mat(destination.rows(), destination.cols(), destination.type());
//		Imgproc.cvtColor(destination, grayImage, Imgproc.COLOR_BGR2GRAY);
//
//		// Binarize the image
//		Mat binarizedImage = new Mat(grayImage.rows(), grayImage.cols(), grayImage.type());
//		Imgproc.threshold(grayImage, binarizedImage, 127, 255, Imgproc.THRESH_BINARY);
//
//		// Remove noise from the binarized image (you'll need to implement the remove_noise method)
//		Mat noiselessImage = new Mat(binarizedImage.rows(), binarizedImage.cols(), binarizedImage.type());
//		Imgproc.medianBlur(binarizedImage, noiselessImage, 5);
//		
//		for (int i = 0; i < 2; i++) {
//			Mat op = new Mat(noiselessImage.rows(), noiselessImage.cols(), noiselessImage.type());
//			Imgproc.GaussianBlur(noiselessImage, op, new Size(0,0), 10);
//			Core.addWeighted(noiselessImage, 1.5, op, -0.5, 0, op);	
//			Imgcodecs.imwrite(file.getAbsolutePath(), op);
//			op = destination;
//		}
		
		// Configuracao Tesseract lib
		Tesseract tesseract = new Tesseract();		
		tesseract.setDatapath("src/main/resources/tessdata");
		tesseract.setLanguage("por");
		tesseract.setOcrEngineMode(1);
		tesseract.setPageSegMode(6);
		tesseract.setVariable("tessedit_char_whitelist", "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzáÁéÉíÍóÓúÚãÃõÕâÂêÊôÔç'  '");

		try {
			// Extracao do texto da imagem
			transcription = tesseract.doOCR(file);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//file.delete();
		System.out.println(transcription);
		return transcription.replaceAll("\\R", " ");
	}

	@Override
	public double compareSimilarity(String label, String transcription) throws DataAccessException {
		JaroWinklerSimilarity sim = new JaroWinklerSimilarity();
		return Math.round((sim.apply(label, transcription)*100.0)*100.0)/100.0;
	}

}
