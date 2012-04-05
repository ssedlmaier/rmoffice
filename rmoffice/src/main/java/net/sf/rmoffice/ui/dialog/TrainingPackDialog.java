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
package net.sf.rmoffice.ui.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.TrainPack;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.models.TrainPackDialogTableModel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The dialog to select a training package.
 */
public class TrainingPackDialog extends JDialog implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final static Logger log = LoggerFactory.getLogger(TrainingPackDialog.class);
	
	private JOptionPane optionPane;

	private JTable devPackTable;
	private JTextArea taDescription;
	private TrainPackDialogTableModel devPackModel;
	private final RMSheet sheet;
	private final MetaData metaData;

	public TrainingPackDialog(Frame owner, RMSheet sheet, MetaData metaData) {
		super(owner, RESOURCE.getString("ui.trainpack.add"), true);
		this.sheet = sheet;
		this.metaData = metaData;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setContentPane( createContentPane() );
		pack();
	}

	private Container createContentPane() {
		JComponent panel = createPanel();
		optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
		 optionPane.addPropertyChangeListener(
			    new PropertyChangeListener() {
			        @Override
					public void propertyChange(PropertyChangeEvent e) {
			            String prop = e.getPropertyName();

			            if (isVisible() 
			             && (e.getSource() == optionPane)
			             && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
			                /* If you were going to check something
			                 * before closing the window, you'd do
			                 * it here. */
			                setVisible(false);
			                int value = ((Integer)optionPane.getValue()).intValue();
			                if (log.isDebugEnabled()) log.debug("user action was "+value);
			                if (value == JOptionPane.YES_OPTION) {
			                	if (devPackTable.getSelectedRow() > -1) {
			                		sheet.addTrainPack( devPackModel.getDevPack( devPackTable.getSelectedRow() )  );
			                	}
			                }
			            }
			        }
			    });
		return optionPane;
	}

	private JComponent createPanel() {
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		/* list of dev packs */
		devPackModel = new TrainPackDialogTableModel(sheet);
		devPackTable = new JTable(devPackModel);
		devPackTable.getSelectionModel().addListSelectionListener(this);
		devPackTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tableScroller = new JScrollPane(devPackTable);
		Dimension preferredSize = tableScroller.getPreferredSize();
		preferredSize.height = 150;
		tableScroller.setPreferredSize(preferredSize );
		pane.setLeftComponent( tableScroller );
        /* description  */
		taDescription = new JTextArea(20, 50);
		taDescription.setEditable(false);
		pane.setRightComponent( new JScrollPane(taDescription) );
		return pane;
	}

	/** {@inheritDoc} */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getFirstIndex() > -1) {
			TrainPack trainPack = devPackModel.getDevPack( devPackTable.getSelectedRow() );
			/* collect information */
			StringBuilder sb = new StringBuilder();
			if (StringUtils.trimToNull(trainPack.getSource()) != null) {
				sb.append(RESOURCE.getString("ui.trainpack.source")).append(": ");
				sb.append(RESOURCE.getString("rolemaster.source."+trainPack.getSource())).append("\n");
			}
			sb.append(RESOURCE.getString("ui.trainpack.startmoney")).append(": ");
			sb.append(trainPack.getStartMoney()).append("\n");
			/* skill groups */
			Map<Integer, Integer> groups = trainPack.getSkillsGroups();
			for (Integer sgId : groups.keySet()) {
				SkillCategory sg = metaData.getSkillCategory(sgId);
				sb.append(""+groups.get(sgId)).append(" ");
				sb.append(RESOURCE.getString("ui.trainpack.todo.rankgroup")).append(" ");
				sb.append(sg.getName()).append("\n");
			}
			/* skills */
			Map<Integer, Integer> skills = trainPack.getSkills();
			for (Integer skillId : skills.keySet()) {
				ISkill sk = sheet.getSkill(skillId);
				sb.append(""+skills.get(skillId)).append(" ");
				sb.append(RESOURCE.getString("ui.trainpack.todo.rankskill")).append(" ");
				sb.append(sk.getName()).append("\n");
			}
			/* Stat gains */
			for (List<StatEnum> gain : trainPack.getStatGains()) {
				sb.append(RESOURCE.getString("ui.trainpack.statgain"));
				for (StatEnum st : gain) {
					sb.append(" ").append(RESOURCE.getString("StatEnum."+st.name()+".short"));
				}
				sb.append("\n");
			}
			/* Todos */
			for (String todo : trainPack.getTodos()) {
				sb.append(todo).append("\n");
			}
			taDescription.setText(sb.toString());
		}
	}
}
