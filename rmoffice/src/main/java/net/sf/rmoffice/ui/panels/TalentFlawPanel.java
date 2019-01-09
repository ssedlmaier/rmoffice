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

import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.ui.models.TalentFlawPresentationModel;
import net.sf.rmoffice.ui.models.TalentFlawTableAdapter;

/**
 * Talent and flaw panel provides buttons for adding and removing the characters talents and flaws.
 * It shows an all talents and flaws of the character in a table. For each talent entry the user can
 * write a background story or further information. 
 */
public class TalentFlawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final TalentFlawPresentationModel talentPresModel;
	private final MetaData metaData;
	private final BeanAdapter<RMSheet> beanAdapter;

	/**
	 * Default constructor.
	 * 
	 * @param talentPresModel the presentation model, not {@code null}
	 */
	public TalentFlawPanel(TalentFlawPresentationModel talentPresModel, MetaData metaData, BeanAdapter<RMSheet> beanAdapter) {
		this.talentPresModel = talentPresModel;
		this.metaData = metaData;
		this.beanAdapter = beanAdapter;
		init();
	}

	private void init() {
		FormLayout layout = new FormLayout("220dlu, 5dlu, 220dlu", "20dlu, 3dlu, fill:120dlu, 3dlu, fill:80dlu, 3dlu, 20dlu");
		setLayout(layout);
		
		PanelBuilder builder = new PanelBuilder(layout, this);
		builder.border(Borders.DIALOG);
		
		ButtonBarBuilder btBuilder = new ButtonBarBuilder();
		btBuilder.addButton(talentPresModel.getAddTalentFlawAction());
		btBuilder.addRelatedGap();
		btBuilder.addButton(talentPresModel.getDelTalentFlawAction());
		btBuilder.addRelatedGap();
		btBuilder.addButton(talentPresModel.getCreateTalentFlawAction());
		add(btBuilder.getPanel(), CC.xy(1, 1));
		
		builder.addLabel(RESOURCE.getString("ui.talentflaw.textbox.description"), CC.xy(3, 1));
		// left upper table
		JTable currentTalentFlaws = new JTable(new TalentFlawTableAdapter(talentPresModel.getListModel()));
		SingleListSelectionAdapter selectionAdapter = new SingleListSelectionAdapter(talentPresModel.getListModel().getSelectionIndexHolder());
		currentTalentFlaws.setSelectionModel(selectionAdapter);
		currentTalentFlaws.getTableHeader().setReorderingAllowed(false);
		builder.add(new JScrollPane(currentTalentFlaws), CC.xy(1, 3));
		// left bottom table
		TalentFlawConverter converter = new TalentFlawConverter(talentPresModel.getListModel().getSelectionHolder());
		JTextArea descrArea = BasicComponentFactory.createTextArea(converter, false);
		descrArea.setEnabled(false);
		descrArea.setLineWrap(true);
		builder.add(new JScrollPane(descrArea), CC.xy(1, 5));
		// right text area
		final BeanAdapter<TalentFlaw> listModelAdapter = new BeanAdapter<TalentFlaw>(talentPresModel.getListModel());
		JTextArea bgArea = BasicComponentFactory.createTextArea(listModelAdapter.getValueModel("description"), false);
		bgArea.setLineWrap(true);
		builder.add(new JScrollPane(bgArea), CC.xywh(3, 3, 1, 3));
		Bindings.bind(bgArea, "enabled", talentPresModel.getSelectionEnabledHolder());
		
		JCheckBox cbOwnPage = BasicComponentFactory.createCheckBox(talentPresModel.getModel(RMSheet.TALENTFLAW_OWN_PAGE_PROP), RESOURCE.getString("ui.talentflaw.ownpage"));
		builder.add(cbOwnPage, CC.xyw(1, 7, 3));
	}
	
	private class TalentFlawConverter extends AbstractConverter {
		private static final long serialVersionUID = 1L;
		public TalentFlawConverter(ValueModel subject) {
			super(subject);
		}


		@Override
		public void setValue(Object arg0) {
			// readonly, not needed
		}

		@Override
		public Object convertFromSubject(Object tfObj) {
			if (tfObj instanceof TalentFlaw) {
				return ((TalentFlaw)tfObj).asText(metaData, beanAdapter.getBean());
			}
			return null;
		}
		
	}
}
