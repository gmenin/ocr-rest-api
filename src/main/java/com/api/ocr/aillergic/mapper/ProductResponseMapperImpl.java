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
package com.api.ocr.aillergic.mapper;

import org.springframework.stereotype.Component;

import com.api.ocr.aillergic.model.Product;
import com.api.ocr.aillergic.rest.dto.ProductResponse;

/**
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
@Component
public class ProductResponseMapperImpl implements ProductResponseMapper {

	@Override
	public ProductResponse productToProductResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setTranscription(product.getTranscription());
		
		return response;
	}

}
