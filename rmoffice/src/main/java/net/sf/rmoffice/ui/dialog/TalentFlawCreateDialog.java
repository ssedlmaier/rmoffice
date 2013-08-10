/*
 * Copyright 2013 Daniel Golesny
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
package net.sf.rmoffice.ui.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawType;
import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawContext;
import net.sf.rmoffice.meta.talentflaw.TalentFlawFactory;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.actions.DesktopAction;
import net.sf.rmoffice.ui.converters.IntegerToStringConverter;
import net.sf.rmoffice.ui.panels.AbstractPanel;
import net.sf.rmoffice.ui.renderer.EnumListCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.ButtonStyle;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.OverlayTextField;

/**
 * Dialog to create a new talent flaw that can be added to the sheet.
 */
public class TalentFlawCreateDialog extends JDialog {
	private static final String HELP_URL = "http://rmoffice.sourceforge.net/readme.html#howtocreatetalentflaws";
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final JPanel panel;
	private final MetaData metaData;
	private final ValueHolder valueHolder;
	private BeanAdapter<TalentFlaw> talentFlawAdapter;
	private final BeanAdapter<RMSheet> beanAdapter;
	private final Frame owner;
	

	public TalentFlawCreateDialog(Frame owner, MetaData metaData, BeanAdapter<RMSheet> beanAdapter) {
		super(owner, RESOURCE.getString("ui.talentflaw.dialog.title"), true);
		this.owner = owner;
		this.metaData = metaData;
		this.beanAdapter = beanAdapter;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		valueHolder = new ValueHolder("");
		talentFlawAdapter = new BeanAdapter<TalentFlaw>(newTalentFlaw());
		panel = createPanel();
		JOptionPane optionPane = createContentPane();
		setContentPane( optionPane );
		pack();
	}
	
	private JOptionPane createContentPane() {
		final JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION );
		optionPane.addPropertyChangeListener(
			    new PropertyChangeListener() {
			        @Override
					public void propertyChange(PropertyChangeEvent e) {
			            if (isVisible() && (JOptionPane.VALUE_PROPERTY.equals(e.getPropertyName()))) {
			                setVisible(false);
			                if (Integer.valueOf(JOptionPane.OK_OPTION).equals(e.getNewValue())) {
			                	TalentFlaw tf = createTalentFlaw();
			                	List<TalentFlaw> talentsFlaws = new ArrayList<TalentFlaw>();
			                	talentsFlaws.addAll(beanAdapter.getBean().getTalentsFlaws());
			                	talentsFlaws.add(tf);
			                	beanAdapter.getBean().setTalentsFlaws(talentsFlaws);
			                }
			                // clear old values (for the second time)
			                talentFlawAdapter.setBean(newTalentFlaw());
			                valueHolder.setValue("");
			                optionPane.setValue(null);
			            }
			        }
			    });
		return optionPane;
	}
	
	private TalentFlaw newTalentFlaw() {
		TalentFlaw tf = new TalentFlaw();
		tf.setLevel(TalentFlawLevel.FREE);
		tf.setType(TalentFlawType.SPECIAL);
		return tf;
	}
	
	private JPanel createPanel() {
		FormLayout layout = new FormLayout("pref, 3dlu, 250dlu", StringUtils.repeat(AbstractPanel.ROW_WITH_GAP, 5) +" 5dlu, fill:120dlu");
		JPanel panel = new JPanel(layout);
		// name 
		panel.add(new JLabel(RESOURCE.getString("ui.talentflaw.tablecol.name")+": "), CC.xy(1,1));
		JTextField talentFlawName = new JTextField();
		Bindings.bind(talentFlawName, talentFlawAdapter.getValueModel("name"));
		panel.add(talentFlawName, CC.xy(3, 1));
		// level
		panel.add(new JLabel(RESOURCE.getString("ui.talentflaw.tablecol.level")+": "), CC.xy(1,3));
		ComboBoxAdapter<TalentFlawLevel> lvl = new ComboBoxAdapter<TalentFlawLevel>(TalentFlawLevel.values(), talentFlawAdapter.getValueModel("level"));
		JComboBox cbLevel = new JComboBox();
		cbLevel.setRenderer(new EnumListCellRenderer());
        cbLevel.setModel(lvl);
        panel.add(cbLevel, CC.xy(3, 3));
        
		// type
        
		panel.add(new JLabel(RESOURCE.getString("ui.talentflaw.tablecol.type")+": "), CC.xy(1,5));
		ComboBoxAdapter<TalentFlawType> type = new ComboBoxAdapter<TalentFlawType>(TalentFlawType.values(), talentFlawAdapter.getValueModel("type"));
		JComboBox cbType = new JComboBox();
		cbType.setRenderer(new EnumListCellRenderer());
		cbType.setModel(type);
        panel.add(cbType, CC.xy(3, 5));
        
        // costs
		panel.add(new JLabel(RESOURCE.getString("ui.talentflaw.value.costs")+": "), CC.xy(1,7));
		JTextField talentFlawCosts = new JTextField();
		Bindings.bind(talentFlawCosts, new IntegerToStringConverter(talentFlawAdapter.getValueModel("costs")));
		panel.add(talentFlawCosts, CC.xy(3, 7));
		
		// tf value input field
		String text = "<html><u>"+RESOURCE.getString("ui.talentflaw.encoding")+"</u></html>:";
		JideButton link = new JideButton(text, UIConstants.ICON_HELP_SMALL);
		link.setButtonStyle(ButtonStyle.HYPERLINK_STYLE);
		link.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DesktopAction(URI.create(HELP_URL)).doPerform();
			}
		});
		panel.add(link, CC.xy(1,9));
		OverlayTextField talentFlawInput = new OverlayTextField();
		Bindings.bind(talentFlawInput, valueHolder);
		panel.add(talentFlawInput, CC.xy(3, 9));
		
		// result table
		JTextArea ta = new JTextArea();
		Bindings.bind(ta, new TalentFlawConverter(metaData, valueHolder));
		ta.setEditable(false);
		panel.add(new JScrollPane(ta), CC.xyw(1, 12, 3) );
		return panel;
	}
	
	private TalentFlaw createTalentFlaw() {
		TalentFlaw talFlaw = talentFlawAdapter.getBean();
		// add the dynamic parts
		TalentFlawContext context = new TalentFlawContext(owner, beanAdapter.getBean());
		TalentFlawFactory factory = new TalentFlawFactory(metaData); 
		if (!StringUtils.isEmpty(valueHolder.getString())) {
			String[] parts = StringUtils.splitPreserveAllTokens(valueHolder.getString(), ",");
			for (String part : parts) {
				ITalentFlawPart talFlawPart = factory.parseTalentFlawPart(part);
				talFlawPart.addToTalentFlaw(context, talFlaw);
			}
		}
		return talFlaw;
	}
	
	/*
	 * 
	 */
	private static class TalentFlawConverter extends AbstractConverter {
		
		private final MetaData meta;

		public TalentFlawConverter(MetaData meta, ValueModel subject) {
			super(subject);
			this.meta = meta;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void setValue(Object arg0) {
			// from textarea (not needed, its readonly)
		}

		@Override
		public Object convertFromSubject(Object stringFromTextfieldObj) {
			String stringFromTextfield = (String)stringFromTextfieldObj;
			// parse the string
			TalentFlawFactory factory = new TalentFlawFactory(meta); 
				if (!StringUtils.isEmpty(stringFromTextfield)) {
					String[] parts = StringUtils.splitPreserveAllTokens(stringFromTextfield, ",");
					StringBuilder sb = new StringBuilder();
					for (String part : parts) {
						try {
							ITalentFlawPart parseTalentFlawPart = factory.parseTalentFlawPart(part);
							sb.append(RESOURCE.getString("ui.talentflaw.value."+parseTalentFlawPart.getId())).append(": ");
							sb.append(parseTalentFlawPart.asText()).append("\n");
						} catch (Exception e) {
							sb.append("Invalid format: ").append(part).append("\n");
						}
					}
					return sb.toString();
				}
			return "";
		}
		
	}
}
