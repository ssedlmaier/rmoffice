/*
 * Copyright 2012 Daniel Nettesheim
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
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.beans.BeanAdapter;

import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.pdf.IPDFCreator;
import net.sf.rmoffice.pdf.NpcPDFCreator;
import net.sf.rmoffice.pdf.PDFCreator;
import net.sf.rmoffice.pdf.PDFCreator2;
import net.sf.rmoffice.pdf.PDFVersion;
import net.sf.rmoffice.ui.RMFrame;
import net.sf.rmoffice.ui.models.LongRunningUIModel;
import net.sf.rmoffice.util.ExtensionFileFilter;


/**
 * Action to create a PDF using the data provided from {@link RMSheet}.
 */
public class CreatePDFAction implements ActionListener {
	private final static Logger log = LoggerFactory.getLogger(CreatePDFAction.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final FileFilter pdfFileFilter = new ExtensionFileFilter(new String[] {"pdf"}, "Adobe PDF (*.pdf)");
	
	private final RMFrame frame;
	private final MetaData data;
	private final BeanAdapter<RMSheet> adapter;
	private final PDFVersion version;
	private final LongRunningUIModel longRunAdapter;

	/**
	 * @param frame the ui frame
	 * @param data the {@link MetaData}
	 * @param adapter the bean adapter
	 * @param fullVersion whether to generate the full version or NPC version of PDF
	 * @param longRunAdapter 
	 * 
	 */
	public CreatePDFAction(RMFrame frame, MetaData data, BeanAdapter<RMSheet> adapter, PDFVersion version, LongRunningUIModel longRunAdapter) {
		this.frame = frame;
		this.data = data;
		this.adapter = adapter;
		this.version = version;
		this.longRunAdapter = longRunAdapter;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			IPDFCreator pdf;
			switch (version) {
			case PDF_FULL_V2:
				pdf = new PDFCreator2(adapter.getBean(), data, frame, longRunAdapter);
				break;
			case PDF_MINIMAL_V1:
				pdf = new NpcPDFCreator(adapter.getBean(), data, frame, longRunAdapter);
				break;
			case PDF_FULL_V1:
			default:
				pdf = new PDFCreator(adapter.getBean(), data, frame, longRunAdapter);
				break;
			}
			JFileChooser fch = new JFileChooser(RMPreferences.getInstance().getLastDir());
			fch.setDialogTitle("PDF Export");
			fch.setAcceptAllFileFilterUsed(false);
			fch.setFileFilter(pdfFileFilter);
			if (frame.getCurrentFile() != null) {
				String name = frame.getCurrentFile().getName();
				fch.setSelectedFile(new File(name.substring(0, name.length() - 4) + ".pdf"));
			}
			int result = fch.showSaveDialog(frame);
			if (JFileChooser.APPROVE_OPTION == result) {
				File selectedFile = fch.getSelectedFile();
				if ( ! selectedFile.getName().endsWith("pdf")) {
					selectedFile = new File(selectedFile.getAbsolutePath() + ".pdf");
				}
				RMPreferences.getInstance().setLastDir( selectedFile.getParentFile() );
				pdf.doCreate(selectedFile.getAbsolutePath());
			}
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
			JOptionPane.showMessageDialog(frame, RESOURCE.getString("error.exportpdf")+": "+e1.getClass().getName()+" "+e1.getMessage());
		}
	}
}
