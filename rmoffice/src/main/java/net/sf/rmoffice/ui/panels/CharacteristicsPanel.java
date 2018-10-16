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
package net.sf.rmoffice.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.ui.converters.ByteArrayToIconConverter;
import net.sf.rmoffice.ui.converters.EnumValueConverter;
import net.sf.rmoffice.ui.converters.IntegerToStringConverter;
import net.sf.rmoffice.ui.converters.LengthFormatToIntegerConverter;
import net.sf.rmoffice.ui.models.CharacteristicsPresentationModel;
import net.sf.rmoffice.util.ExtensionFileFilter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Panel for 
 */
public class CharacteristicsPanel extends AbstractPanel<Characteristics> {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(CharacteristicsPanel.class);
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private FileFilter fileFilter = new ExtensionFileFilter(new String[] {"jpg", "jpeg", "gif", "png"}, RESOURCE.getString("common.images")+" (jpg, gif, png)");
	
	public CharacteristicsPanel(CharacteristicsPresentationModel characteristicsPM, BeanAdapter<RMSheet> sheetAdapter, ValueHolder enabledValueHolder) {
		FormLayout layout = new FormLayout(
				COL_DEFINITION_2COLGROUPS,
				StringUtils.repeat(ROW_WITH_GAP, 7)
				+ HEADLINE_SEPARATOR +
				StringUtils.repeat(ROW_WITH_GAP, 8) 
				+ HEADLINE_SEPARATOR +
				ROW_WITH_GAP + ROW_WITH_GAP +
				" 160dlu");
		layout.setColumnGroups(new int[][]{{1, 7}});

		// PanelBuilder just for the border
		PanelBuilder builder = new PanelBuilder(layout, this);
		builder.setDefaultDialogBorder();

		/* Characteristics */
		int row = 1;
		
	    ValueModel appConverter = new IntegerToStringConverter(characteristicsPM.getModel(Characteristics.PROP_APPEARANCE));
		addTextFieldL(Characteristics.PROP_APPEARANCE, row, characteristicsPM, enabledValueHolder, appConverter, builder);
		addTextFieldR(Characteristics.PROP_DEMEANOR, row, characteristicsPM, enabledValueHolder, null, builder);
		row += 2;
		
		addTextFieldL(Characteristics.PROP_AGE_APPEAR, row, characteristicsPM, enabledValueHolder, null, builder);
		ValueModel ageConverter = new IntegerToStringConverter(characteristicsPM.getModel(Characteristics.PROP_AGE));
		addTextFieldR(Characteristics.PROP_AGE, row, characteristicsPM, enabledValueHolder, ageConverter, builder);
		row += 2;
		
		/* Gender */
		add(new JLabel(RESOURCE.getString("rolemaster.characteristics.gender")), CC.xy(COL_LBL_L, row));
		ValueModel genderConverter = new AbstractConverter(characteristicsPM.getModel(Characteristics.PROP_IS_FEMALE)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Object convertFromSubject(Object subjectValue) {
				if (subjectValue == null || ((Boolean) subjectValue).booleanValue()) {
					return RESOURCE.getString("gender.female");
				} else {
					return RESOURCE.getString("gender.male");
				}
			}

			@Override
			public void setValue(Object newValue) {
				subject.setValue( Boolean.valueOf( ! RESOURCE.getString("gender.male").equals(newValue) ) );
			}
		};
		ComboBoxAdapter<String> cbGenderAdapter = new ComboBoxAdapter<String>(new String[] {RESOURCE.getString("gender.male"), 
				                                                                            RESOURCE.getString("gender.female")}, genderConverter);
		JComboBox cbGender =  new JComboBox(cbGenderAdapter);
		Bindings.bind(cbGender, "enabled", enabledValueHolder);
		add(cbGender, CC.xyw(COL_COMP_L, row, 3));

		addTextFieldR(Characteristics.PROP_SKIN, row, characteristicsPM, enabledValueHolder, null, builder);
		
		row +=2;
		
		/* Height + Unit */
		builder.addLabel(RESOURCE.getString("rolemaster.characteristics.height"), CC.xy(COL_LBL_L, row));
		JTextField tfHeight =  new JTextField();
		LengthFormatToIntegerConverter heightConverter = new LengthFormatToIntegerConverter(characteristicsPM.getModel(Characteristics.PROP_HEIGHT_CM), sheetAdapter);
		Bindings.bind(tfHeight, heightConverter, true);
		Bindings.bind(tfHeight, "enabled", enabledValueHolder);
		builder.add(tfHeight, CC.xyw(COL_COMP_L, row, 3));
		
		/* Weight*/
		builder.addLabel(RESOURCE.getString("rolemaster.characteristics.weight"), CC.xy(COL_LBL_R, row));
		JFormattedTextField tfWeight = BasicComponentFactory.createIntegerField(characteristicsPM.getModel(Characteristics.PROP_WEIGHT));
		Bindings.bind(tfWeight, "enabled", enabledValueHolder);
		builder.add(tfWeight, CC.xy(COL_COMP_R, row));

		EnumValueConverter weightCon = new EnumValueConverter(sheetAdapter.getValueModel(RMSheet.PROPERTY_WEIGHT_UNIT));
		JLabel lblWeightUnit = BasicComponentFactory.createLabel(weightCon);
		builder.add(lblWeightUnit, CC.xy(COL_COMP_R + 2, row));

		row += 2;
		
		addTextFieldL(Characteristics.PROP_HAIR, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_EYE, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;

		addTextFieldL(Characteristics.PROP_PERSONALITY, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_MOTIVATION, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;

		addTextFieldL(Characteristics.PROP_ALIGNMENT, row, characteristicsPM, enabledValueHolder, null, builder);
		
		row += 2;

		builder.addSeparator(RESOURCE.getString("rolemaster.characteristics.background.title"), CC.xyw(1, row, 11));
		row += 1;
		
		
		addTextFieldL(Characteristics.PROP_NATIONALITY, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_HOME_TOWN, row, characteristicsPM, enabledValueHolder, null, builder);

		
		row += 2;
		addTextFieldL(Characteristics.PROP_DEITY, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_LORD, row, characteristicsPM, enabledValueHolder, null, builder);
		
		row += 2;

		addTextFieldL(Characteristics.PROP_PARENT, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_SPOUSE, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;
		
		addTextFieldL(Characteristics.PROP_SIBLINGS, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_CHILDREN, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;

		addTextFieldL(Characteristics.PROP_MISC1, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_CLOTH_SIZE, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;
		
		addTextFieldL(Characteristics.PROP_MISC2, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_HAT_SIZE, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;
		
		addTextFieldL(Characteristics.PROP_MISC3, row, characteristicsPM, enabledValueHolder, null, builder);
		addTextFieldR(Characteristics.PROP_SHOE_SIZE, row, characteristicsPM, enabledValueHolder, null, builder);

		row += 2;
		
		addTextFieldL(Characteristics.PROP_MISC4, row, characteristicsPM, enabledValueHolder, null, builder);
		
		row += 2;
		builder.addSeparator(RESOURCE.getString("rolemaster.characteristics.image.title"), CC.xyw(1, row, 11));
		row += 1;
		/* Image */
		final ValueModel charImageConverter = new ByteArrayToIconConverter(characteristicsPM.getModel(Characteristics.PROP_CHARIMAGE));
		JButton btCharImage = new JButton(RESOURCE.getString("pdf.page4.characterimage.header"));
		btCharImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fch = new JFileChooser();
				fch.setAcceptAllFileFilterUsed(false);
				fch.setFileFilter( fileFilter );
				int result = fch.showOpenDialog(CharacteristicsPanel.this);
				if (JFileChooser.APPROVE_OPTION == result) {
					File selectedFile = fch.getSelectedFile();
					InputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(selectedFile);
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						byte[] b = new byte[2048];
						while (fileInputStream.available() > 0) {
							int len = fileInputStream.read(b);
							if (len > 0) {
								out.write(b, 0, len);
							}
						}
						byte[] bytes = out.toByteArray();
						new ImageIcon(bytes);
						charImageConverter.setValue(bytes);
					} catch (IOException ex) {
						if (log.isWarnEnabled()) log.warn(ex.getMessage());
					} finally {
						if (fileInputStream != null) {
							try {
								fileInputStream.close();
							} catch (IOException e1) {
								log.warn("Could not close file stream {0}: {1}", selectedFile.toString(), e1);
							}
						}
					}
				}
			}
		});
		JButton btDelImage = new JButton(RESOURCE.getString("rolemaster.characteristics.image.delete"));
		btDelImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				charImageConverter.setValue(null);
			}
		});
		
		builder.add(btCharImage, CC.xy(COL_LBL_L, row));
		Bindings.bind(btDelImage, "enabled", enabledValueHolder);
		builder.add(btDelImage, CC.xy(COL_LBL_L, row + 2));
		
		JLabel lbCharImage = new JLabel();
		Bindings.bind(lbCharImage, "icon", charImageConverter);
		Bindings.bind(btCharImage, "enabled", enabledValueHolder);
		builder.add(new JScrollPane(lbCharImage), CC.xywh(COL_COMP_L, row, 9, 5, "fill fill"));
	}
		
	@Override
	public String getResKeyPrefix() {
		return "rolemaster.characteristics.";
	}
}
