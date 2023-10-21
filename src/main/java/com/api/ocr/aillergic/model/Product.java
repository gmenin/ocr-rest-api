/**
 * Copyright 2023 AIllergic.
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
package com.api.ocr.aillergic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity class for Product
 * 
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
@Document(collection = "products")
public class Product {
	
	@Id
	private String id;
	
	@Field(name = "name")
	private String name;
	
	@Field(name = "label")
	private String label;
	
	@Field(name = "transcription")
	private String transcription;
	
	@Field(name = "similarity")
	private double similarity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	/**
	 * Default constructor
	 */
	public Product() {}

	/**
	 * Constructor with fields
	 * 
	 * @param <code>id</code>					the id of the food label
	 * @param <code>name</code>					the name of the product 
	 * @param <code>label</code>				the food label described on the product packaging
	 * @param <code>transcription</code>		the text converted by the OCR library from the food label
	 * @param <code>similarity</code>			the similarity between the information in the image file and the extracted text
	 */
	public Product(String id, String name, String label, String transcription, double similarity) {
		super();
		this.id = id;
		this.name = name;
		this.label = label;
		this.transcription = transcription;
		this.similarity = similarity;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", label=" + label + ", transcription=" + transcription
				+ ", similarity=" + similarity + "]";
	}
	
}
