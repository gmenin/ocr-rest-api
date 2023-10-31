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
package com.api.ocr.aillergic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gabriel Menin (gabrielgm@ufcspa.edu.br)
 * 
 */
public final class FileUtils {

	private FileUtils(){}
	
	public static File createFile(MultipartFile image) throws IOException {
		File file = new File(image.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(image.getBytes());
		fos.close(); 
		
		return file;
    }
}
