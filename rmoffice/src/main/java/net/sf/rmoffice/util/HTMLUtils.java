/*
 * Copyright 2012 Daniel Golesny
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.util;

import java.util.StringTokenizer;


/**
 * 
 */
public class HTMLUtils {
	public static String getHtmlTooltip(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		if (text != null) {
			if (text.length() < 80) {
				sb.append(text);
			} else {
				int lineIdx = 0;
				StringTokenizer t = new StringTokenizer(text);
				while (t.hasMoreElements()) {
					String token = t.nextToken();
					sb.append(token);
					lineIdx += token.length();
					if (lineIdx > 80) {
						sb.append("<br>");
						lineIdx = 0;
					} else {
						sb.append(" ");
						lineIdx++;
					}
				}
			}
		}
		sb.append("</html>");
		return sb.toString();
	}
}
