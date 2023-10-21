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
package com.api.ocr.aillergic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.ocr.aillergic.model.Product;

/**
 * Repository class for <code>Product</code> domain objects 
 * All method names are compliant with Spring Data naming conventions 
 * so this interface can easily be extended for Spring Data
 * 
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
public interface ProductRepository extends MongoRepository<Product, String>{

}
