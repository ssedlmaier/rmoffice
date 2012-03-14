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
package net.sf.rmoffice.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jgoodies.binding.beans.BeanAdapter;

import net.sf.rmoffice.core.ExportImport;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.ui.RMFrame;


/**
 * 
 */
public class SaveAction implements ActionListener {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final RMFrame rmFrame;
	private final BeanAdapter<RMSheet> beanAdapter;

	/**
	 * @param rmFrame
	 * @param beanAdapter 
	 */
	public SaveAction(RMFrame rmFrame, BeanAdapter<RMSheet> beanAdapter) {
		this.rmFrame = rmFrame;
		this.beanAdapter = beanAdapter;
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (rmFrame.getCurrentFile() != null) {
			if (rmFrame.getCurrentFile().canWrite()) {
				try {						
					File bakFile = new File(rmFrame.getCurrentFile().getAbsolutePath() + "."+System.currentTimeMillis()+".bak");
					if (rmFrame.getCurrentFile().renameTo(bakFile)) {
						if (rmFrame.getCurrentFile().createNewFile()) {
							Writer fwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rmFrame.getCurrentFile()), ExportImport.ENCODING));
							ExportImport.exportXml(beanAdapter.getBean(), fwriter);
							fwriter.close();
							/* if saving was successful, we delete the bak file */
							if (!bakFile.delete()) {
								bakFile.deleteOnExit();
							}
						} else {
							JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.couldNotCreate"));
						}
					} else {
						/* rename the temp file to current file */
						JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.save.renameTemp")+"\n"+bakFile.getName());
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.save")+": "+e1.getLocalizedMessage());
				}
			} else {
				JOptionPane.showMessageDialog(rmFrame, RESOURCE.getString("error.file.notwriteable"));
			}
		}
	}

}
