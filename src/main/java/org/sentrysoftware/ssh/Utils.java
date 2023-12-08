package org.sentrysoftware.ssh;

/*-
 * ╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲
 * SSH Java Client
 * ჻჻჻჻჻჻
 * Copyright 2023 Sentry Software
 * ჻჻჻჻჻჻
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
 * ╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱
 */

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;


/**
 * Utility Class (static), to be used anywhere in  Matsya, including in
 * standalone JARs (in CLI mode)
 *
 * @author Bertrand
 */
public class Utils {

	private Utils() { }

	/**
	 * Returns the proper Charset that can decode/encode the specified locale
	 * 
	 * @param locale The locale we're dealing with, as formatted in the LANG environment variable (e.g. zh_CN.utf8)
	 * @param defaultCharset The default Charset to use if the specified locale doesn't match any supported Charset
	 * @return The appropriate Charset instance
	 */
	public static Charset getCharsetFromLocale(final String locale, final Charset defaultCharset) {

		// What charset will we be dealing with coming from and going to the PATROL Agent
		Charset charset = defaultCharset;
		if (locale != null && !locale.isEmpty()) {
			final String[] localeElements = locale.split("\\.");
			if (localeElements.length > 0) {
				String charsetName = localeElements[localeElements.length - 1];
				if (charsetName != null) {
					charsetName = charsetName.trim();
					if (!charsetName.isEmpty() && !"c".equalsIgnoreCase(charsetName)) {
						if ("gb".equalsIgnoreCase(charsetName)) {
							charsetName = "GBK";
						}
   						try {
							charset = Charset.forName(charsetName);
						} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
							/* Unfortunately, there is nothing we can do here, as debug is not even set yet */
						}
					}
				}
			}
	 	}

		return charset;
	}

	/**
	 * Returns the proper Charset that can decode/encode the specified locale, or UTF-8 if specified locale
	 * cannot be converted to a Charset.
	 * 
	 * @param locale The locale we're dealing with, as formatted in the LANG environment variable (e.g. zh_CN.utf8)
	 * @return The appropriate Charset instance
	 */
	public static Charset getCharsetFromLocale(final String locale) {
		return getCharsetFromLocale(locale, StandardCharsets.UTF_8);
	}

	/**
	 * Check if the required field is not null.
	 *
	 * @param field
	 * @param name
	 * @throws IllegalStateException if the argument is null
	 */
	public static <T> void checkNonNullField(final T field, final String name) {
		if (field == null) {
			throw new IllegalStateException(name + " must not be null.");
		}
	}


	/**
	 * Check if the required argument is not negative or zero.
	 *
	 * @param argument
	 * @param name
	 * @throws IllegalArgumentException if the argument is null
	 */
	public static void checkArgumentNotZeroOrNegative(final long argument, final String name) {
		if (argument <= 0) {
			throw new IllegalArgumentException(String.format("%s=%d must not be negative or zero.", name, argument));
		}
	}


}
