/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.ExportImport;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.ui.RMFrame;
import net.sf.rmoffice.util.RMOFileFilter;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * 
 */
public class SaveAsAction implements ActionListener {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final RMFrame rmFrame;
	private final BeanAdapter<RMSheet> beanAdapter;

	/**
	 * @param rmFrame
	 * @param beanAdapter 
	 */
	public SaveAsAction(RMFrame rmFrame, BeanAdapter<RMSheet> beanAdapter) {
		this.rmFrame = rmFrame;
		this.beanAdapter = beanAdapter;
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			JFileChooser fch = new JFileChooser(RMPreferences.getInstance().getLastDir());
			fch.setAcceptAllFileFilterUsed(false);
			fch.setFileFilter(new RMOFileFilter());
			int result = fch.showSaveDialog(rmFrame);
			if (JFileChooser.APPROVE_OPTION == result) {
				File selectedFile = fch.getSelectedFile();
				if ( ! selectedFile.getName().endsWith(RMPreferences.RMO_EXTENSION)) {
					selectedFile = new File(selectedFile.getAbsolutePath() + "." + RMPreferences.RMO_EXTENSION);
				}
				boolean doIt = true;
				if (selectedFile.exists()) {
					if (JFileChooser.APPROVE_OPTION != JOptionPane.showConfirmDialog(rmFrame, RESOURCE.getString("warning.file.overwrite"))) {
						doIt = false;
					}
				} else {
					if (!selectedFile.createNewFile()) {
						JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.couldNotCreate"));
						doIt = true;
					}
				}
				if (doIt) {
					if (selectedFile.canWrite()) {
						Writer fwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile), ExportImport.ENCODING));
						ExportImport.exportXml(beanAdapter.getBean(), fwriter);
						fwriter.close();
						RMPreferences.getInstance().setLastDir( selectedFile.getParentFile() );
						rmFrame.setCurrentFile( selectedFile );
						rmFrame.setTitle(selectedFile.getName());
					} else {
						JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.notwriteable"));
					}
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.save")+": "+ex.getLocalizedMessage());
		}
	}

}
