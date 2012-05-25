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
package net.sf.rmoffice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.sf.rmoffice.meta.enums.LengthUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class RMPreferences {
	public static final String RMO_EXTENSION = "rmo";
	private final static Logger log = LoggerFactory.getLogger(RMPreferences.class);
	private static RMPreferences instance = new RMPreferences();
	private static final String SEP = System.getProperty("file.separator");
	private static final String RMOFFICE_USER_PROFILE = SEP + ".rmoffice" + SEP + "user.properties";
	private static final String PREF_EXCLUDE = "excludes";
	private static final String PREF_SPELLLIST_DP_INCREASING = "spelllist-dp-increasing";
	private static final String PREF_SHOW_OUTLINE_IMAGE = "outline-image";
	
	
	private final Set<String> excludes = new HashSet<String>();
	private File lastDir;
	private int spelllistDPincreasing = 5;
	private boolean printOutlineImage = true;
	private LengthUnit lengthUnit = LengthUnit.CM;
	private static String propertyFilePath;
	
	private RMPreferences() {
	}
	
	/**
	 * Initializes the preferences instance.
	 */
	/* package */ static void init() {
		propertyFilePath = System.getProperty("user.home") + RMOFFICE_USER_PROFILE;
		log.info("initialising preferences from "+propertyFilePath);
		
		FileReader reader = null;
		try {
			File prefFile = new File(propertyFilePath);
			if (prefFile.exists() && prefFile.canRead()) {
				Properties props = new Properties();
				reader = new FileReader(prefFile);
				props.load(reader);
				loadExcludes(props);
				loadValues(props);
			}
		} catch (Exception e) {
			log.warn("Could not load preferences: "+e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.warn("Could not close user.prefs");
				}
			}
		}
	}

	/**
	 * 
	 * @param props
	 */
	private static void loadValues(Properties props) {
		if (props.containsKey(PREF_SPELLLIST_DP_INCREASING)) {
			instance.spelllistDPincreasing = NumberUtils.toInt(props.getProperty(PREF_SPELLLIST_DP_INCREASING));
		}
		if (props.containsKey(PREF_SHOW_OUTLINE_IMAGE)) {
			instance.printOutlineImage = ! "0".equals(props.getProperty(PREF_SHOW_OUTLINE_IMAGE));
		}
	}

	/* loads the profession excludes */
	private static void loadExcludes(Properties props) {
		try {
			if (props.containsKey(PREF_EXCLUDE)) {
				String[] split = StringUtils.split(props.getProperty(PREF_EXCLUDE), ',');
				for (String ex : split) {
					log.info("Excluding source "+ex);
					instance.excludes.add(ex);
				}
			}
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn("Could not read excludes from user.properties: "+e.getMessage());
		}
	}

	/**
	 * 
	 * @return the preferences.
	 */
	public static RMPreferences getInstance() {
		return instance;
	}
	
	/**
	 * 
	 * @return the property file path
	 */
	public static String getPropertiesFilePath() {
		return propertyFilePath;
	}
	
	/**
	 *  Returns false of the given source is {@code null}
	 * @param source the source book string, may be {@code null}
	 * @return whether the given source is excluded or not.
	 */
	public boolean isExcluded(String source) {
		return excludes.contains(source);
	}

	
	public File getLastDir() {
		return lastDir;
	}

	
	public void setLastDir(File lastDir) {
		this.lastDir = lastDir;
	}
	
	public int getSpelllistDPIncrease() {
		return spelllistDPincreasing;
	}

	/**
	 * 
	 * @return whether the outline image (combat sheet) is printed or not
	 */
	public boolean printOutlineImage() {
		return printOutlineImage;
	}

	public LengthUnit getLengthUnit() {
		if (lengthUnit == null) {
			return LengthUnit.CM;
		}
		return lengthUnit;
	}
	
	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}
}
