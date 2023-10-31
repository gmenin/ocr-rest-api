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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.ocr.aillergic.model.Product;
import com.api.ocr.aillergic.repository.ProductRepository;
import com.api.ocr.aillergic.util.FileUtils;

import net.sourceforge.tess4j.ITessAPI.TessOcrEngineMode;
import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;
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
		
		// Tesseract setup
		Tesseract tesseract = new Tesseract();	
		tesseract.setDatapath("src/main/resources/tessdata");
		tesseract.setLanguage("por");
		tesseract.setOcrEngineMode(TessOcrEngineMode.OEM_LSTM_ONLY);
		tesseract.setPageSegMode(TessPageSegMode.PSM_SINGLE_BLOCK);
		tesseract.setVariable("tessedit_char_whitelist", "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzáÁéÉíÍóÓúÚãÃõÕâÂêÊôÔç.:,!()'  '");
		
		String transcription = "";
		
		File file = FileUtils.createFile(image);
		
		Mat destination = new Mat();
		Mat source = Imgcodecs.imread(file.getAbsolutePath());
		
		for (int i = 0; i < 2; i++) {
			destination = new Mat(source.rows(), source.cols(), source.type());
			Imgproc.GaussianBlur(source, destination, new Size(0,0), 10);
			Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);	
			Imgcodecs.imwrite(file.getAbsolutePath(), destination);
			source = destination;
		}
			
		try {
			// Extracting text from image
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
