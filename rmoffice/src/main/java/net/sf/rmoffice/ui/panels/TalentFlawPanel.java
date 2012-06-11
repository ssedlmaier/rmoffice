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
package net.sf.rmoffice.ui.panels;

import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.ui.models.TalentFlawPresentationModel;
import net.sf.rmoffice.ui.models.TalentFlawTableAdapter;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Talent and flaw panel provides buttons for adding and removing the characters talents and flaws.
 * It shows an all talents and flaws of the character in a table. For each talent entry the user can
 * write a background story or further information. 
 */
public class TalentFlawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final TalentFlawPresentationModel talentPresModel;

	/**
	 * Default constructor.
	 * 
	 * @param talentPresModel the presentation model, not {@code null}
	 */
	public TalentFlawPanel(TalentFlawPresentationModel talentPresModel) {
		this.talentPresModel = talentPresModel;
		init();
	}

	private void init() {
		FormLayout layout = new FormLayout("200dlu, 5dlu, 250dlu", "20dlu, 3dlu, fill:150dlu, 5dlu, 20dlu");
		setLayout(layout);
		
		PanelBuilder builder = new PanelBuilder(layout, this);
		builder.setDefaultDialogBorder();
		
		ButtonBarBuilder2 btBuilder = new ButtonBarBuilder2();
		btBuilder.addButton(talentPresModel.getAddTalentFlawAction());
		btBuilder.addRelatedGap();
		btBuilder.addButton(talentPresModel.getDelTalentFlawAction());
		add(btBuilder.getPanel(), CC.xy(1, 1));
		
		builder.addLabel(RESOURCE.getString("ui.talentflaw.textbox.description"), CC.xy(3, 1));
		
		JTable currentTalentFlaws = new JTable(new TalentFlawTableAdapter(talentPresModel.getListModel()));
		currentTalentFlaws.setSelectionModel(new SingleListSelectionAdapter(talentPresModel.getListModel().getSelectionIndexHolder()));
		currentTalentFlaws.getTableHeader().setReorderingAllowed(false);
		builder.add(new JScrollPane(currentTalentFlaws), CC.xy(1, 3));
		
		BeanAdapter<TalentFlaw> beanAdapter = new BeanAdapter<TalentFlaw>(talentPresModel.getListModel());
		JTextArea bgArea = BasicComponentFactory.createTextArea(beanAdapter.getValueModel("description"), false);
		bgArea.setLineWrap(true);
		builder.add(new JScrollPane(bgArea), CC.xy(3, 3));
		Bindings.bind(bgArea, "enabled", talentPresModel.getSelectionEnabledHolder());
		
		JCheckBox cbOwnPage = BasicComponentFactory.createCheckBox(talentPresModel.getModel(RMSheet.TALENTFLAW_OWN_PAGE_PROP), RESOURCE.getString("ui.talentflaw.ownpage"));
		Bindings.bind(cbOwnPage, "enabled", talentPresModel.getSelectionEnabledHolder());
		builder.add(cbOwnPage, CC.xyw(1, 5, 3));
	}
}
