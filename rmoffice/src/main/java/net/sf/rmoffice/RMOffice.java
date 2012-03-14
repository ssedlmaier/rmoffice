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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.ui.RMFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class RMOffice {
	private final static Logger log = LoggerFactory.getLogger(RMOffice.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final String LATEST_VERSION_CHECK_URL = "http://sourceforge.net/projects/rmoffice/files/old/lv.nfo/download";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (log.isInfoEnabled()) {
			log.info("starting "+getProgramString());
			log.info("locale: "+Locale.getDefault());
		}
		RMFrame frame = null;
		try {
			frame = new RMFrame();
			RMPreferences.init();
			MetaDataLoader loader = new MetaDataLoader();
			final MetaData data = loader.load();			
			frame.init(data);
			frame.pack();
			frame.setVisible(true);
			if (args == null || args.length == 0 || ! "-offline".equals(args[0])) {
				checkForUpdate(frame);
			} else {
				log.debug("offline mode: no latest release check");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Writer sOut = new StringWriter();
			e.printStackTrace(new PrintWriter(sOut ));
			JOptionPane.showMessageDialog(frame, RESOURCE.getString("error.application")+"\n\n"+e.getClass().getName()+" "+sOut.toString());
		}
	}
	
	private static void checkForUpdate(final RMFrame frame) {		
		try {
			Thread t = new Thread("Online check") { 
				@Override
				public void run() {
					InputStream is = null;
					try {
						if (log.isDebugEnabled()) log.debug("get "+LATEST_VERSION_CHECK_URL);
						URL latestRevisionUrl = new URL(LATEST_VERSION_CHECK_URL);
						Object content = latestRevisionUrl.getContent();
						if (content instanceof InputStream) {
							is = (InputStream)content;
							byte[] b = new byte[10];
							int len = is.read(b);
							if (len > 0) {
								final String latestVersion = new String(b, 0, len);
								log.debug("received latest version is "+latestVersion);
								if (!RESOURCE.getString("rolemaster.version").equals(latestVersion)) {
									SwingUtilities.invokeLater(new Runnable() {

										@Override
										public void run() {
											frame.newLatestVersion(latestVersion);
										}
									});
								} else {
									log.debug("current version is up to date");
								}
							}
						}
					} catch (Exception e) {
						log.error("Could not check for update: "+e.getMessage());
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								log.warn(e.getMessage(), e);
							}
						}
					}
				} 
			};
			t.setDaemon(true);
			t.start();
		} catch (Exception e) {
			log.error("Could not check for update: "+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return the version 
	 */
	public static String getProgramString() {
		return MessageFormat.format(RESOURCE.getString("rolemaster.versioninfo"), RESOURCE.getString("rolemaster.version"));
	}
}
