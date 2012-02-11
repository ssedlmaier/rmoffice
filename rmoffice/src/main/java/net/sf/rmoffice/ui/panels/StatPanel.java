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
package net.sf.rmoffice.ui.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.RMSheet.State;
import net.sf.rmoffice.generator.CharacterGenerator;
import net.sf.rmoffice.generator.DiceUtils;
import net.sf.rmoffice.generator.StatGainGenerator;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.models.StatValueModelAdapter;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;


/**
 * The stat panel.
 */
public class StatPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_ALL_STAT_TEMP_DICE = "allstatTempDice";
	private static final String ACTION_STAT_GAIN_DICE = "statGainDice";
	private static final String ACTION_STAT_POT_DICE_PREFIX = "statPotDice";
	private static final String ACTION_ALL_STAT_POT_FIXED = "allStatPotFixed";
	private static final String ACTION_ALL_STAT_POT_DICE = "allStatPotDice";
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final int COL_STAT_TEMP = 0;
	private static final int COL_STAT_POT = 1;
	@SuppressWarnings("unused")
	private static final int COL_STAT_BONUS = 2;
	@SuppressWarnings("unused")
	private static final int COL_STAT_RACE_BONUS = 3;
	private static final int COL_STAT_MISC_BONUS = 4;
	@SuppressWarnings("unused")
	private static final int COL_STAT_MISC2_BONUS = 5;
	private static final int COL_STAT_TOTAL_BONUS = 6;
	private static final int PREFERRED_FIELD_WIDTH = 40;
	private static final String[] colAsString = new String[]{"Temp", "Pot", "StatBonus", "Race", "Misc", "Misc2", "Total"};
	
	private ValueHolder enabledValueHolder;
	private Map<StatEnum, List<JTextField>> attributeFields = new HashMap<StatEnum, List<JTextField>>();
	private JTextField tempStatSumField;
	private JTextField devPointField;
	private final BeanAdapter<StatValueModelAdapter> beanAdapter;
	private final BeanAdapter<RMSheet> rmSheetAdapter;
	/**
	 * 
	 * @param beanAdapter the adapter to bind values
	 * @param rmSheetAdapter the rm sheet adapter (for some generation purposes)
	 * @param enableValueHolder the value model that control enabling components in state {@linkplain State#NORMAL}
	 */
	public StatPanel(BeanAdapter<StatValueModelAdapter> beanAdapter, BeanAdapter<RMSheet> rmSheetAdapter, ValueHolder enableValueHolder) {
		super(new GridBagLayout());
		this.beanAdapter = beanAdapter;
		this.rmSheetAdapter = rmSheetAdapter;
		enabledValueHolder = enableValueHolder;
		GridBagConstraints col1 = new GridBagConstraints();
		col1.gridx = 0;
		col1.insets = new Insets(2, 10, 2, 10);
		col1.weightx = 0.4;
		GridBagConstraints col2 = new GridBagConstraints();
		col2.insets = new Insets(2, 2, 2, 10);
		col2.weightx = 0.1;
		col2.anchor = GridBagConstraints.CENTER;

		
		/* header labels */
		col1.gridy = 0;
		col2.gridy = 0;
		col2.gridx = 1;
		JButton btTempStats = new JButton(RESOURCE.getString("ui.stats.temp"));
		btTempStats.setActionCommand(ACTION_ALL_STAT_TEMP_DICE);
		Bindings.bind(btTempStats, "enabled", enableValueHolder);
		btTempStats.addActionListener(this);
		add(btTempStats, col2);
		
		col2.gridx = 2;
		JButton btPotStats = new JButton(RESOURCE.getString("ui.stats.pot"));
		btPotStats.setActionCommand(ACTION_ALL_STAT_POT_DICE);
		Bindings.bind(btPotStats, "enabled", enableValueHolder);
		btPotStats.addActionListener(this);
		add(btPotStats, col2);
		col2.gridx = 3;
		add(new JLabel(RESOURCE.getString("ui.stats.bonus.rank")), col2);
		col2.gridx = 4;
		add(new JLabel(RESOURCE.getString("ui.stats.bonus.racial")), col2);
		col2.gridx = 5;
		add(new JLabel(RESOURCE.getString("ui.stats.bonus.misc")), col2);
		col2.gridx = 6;
		add(new JLabel(RESOURCE.getString("ui.stats.bonus.misc2")), col2);
		col2.gridx = 7;
		add(new JLabel(RESOURCE.getString("ui.stats.bonus.total")), col2);
		
		/* fields */
		KeyListener jumpKeyListener = new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getComponent() instanceof JTextField) {
					JTextField component = (JTextField)e.getComponent();
					if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
						StatEnum stat = (StatEnum) component.getClientProperty("stat");
						Integer col = (Integer) component.getClientProperty("col");
						if (col != null) {
							/* search next */
							int colI = col.intValue();
							int statIdx = Arrays.binarySearch(StatEnum.values(), stat);
							if (e.getKeyCode() == KeyEvent.VK_DOWN) {
								statIdx++;
							} else {
								statIdx--;
							}
							if (statIdx >= StatEnum.values().length) {
								statIdx = 0;
								do {
									colI++;
								} while (colI != COL_STAT_TEMP && colI != COL_STAT_POT && colI != COL_STAT_MISC_BONUS && colI < COL_STAT_TOTAL_BONUS && colI >= 0);
								if (colI >= COL_STAT_TOTAL_BONUS) {
									colI = COL_STAT_TEMP;
								}
							} else if (statIdx < 0) {
								statIdx = StatEnum.values().length - 1;
								do {
									colI--;
								} while (colI != COL_STAT_TEMP && colI != COL_STAT_POT && colI != COL_STAT_MISC_BONUS && colI < COL_STAT_TOTAL_BONUS && colI >= 0);
								if (colI < COL_STAT_TEMP) {
									colI = COL_STAT_MISC_BONUS;
								}
							}
							stat = StatEnum.values()[statIdx];
							attributeFields.get(stat).get(colI).requestFocus();
							attributeFields.get(stat).get(colI).selectAll();
						}
					}
				}
			}
			
		};
		for (StatEnum stat : StatEnum.values()) {
			col2.anchor = GridBagConstraints.CENTER;
			col1.gridy++;
			col2.gridy++;
			/* stat label*/
			JLabel lbl = new JLabel(stat.getFullI18N());
			Bindings.bind(lbl, "foreground", beanAdapter.getValueModel(stat.name()+"Foreground"));
			add(lbl, col1);
			/* stat fields */
			attributeFields.put(stat, new ArrayList<JTextField>());
			for (int i=0; i<colAsString.length; i++) {
				col2.gridx = (1 + i);
				JTextField field = new JTextField();
				field.putClientProperty("stat", stat);
				field.putClientProperty("col", Integer.valueOf(i));
				field.addKeyListener(jumpKeyListener);
				Bindings.bind(field, beanAdapter.getValueModel(stat.name()+colAsString[i]));
				if (i == COL_STAT_TEMP || i == COL_STAT_POT || i == COL_STAT_MISC_BONUS) {
					Bindings.bind(field, "enabled", enabledValueHolder);					
				} else {
					field.setEnabled(false);
					field.setEditable(false);
				}					
				Dimension preferredSize = field.getPreferredSize();
				preferredSize.width = PREFERRED_FIELD_WIDTH;
				field.setPreferredSize(preferredSize );
				add(field, col2);
				attributeFields.get(stat).add(field);
			}
			col2.anchor = GridBagConstraints.WEST;
			/* stat pot dice */
			JButton btStatPotDice = new JButton(RESOURCE.getString("ui.stats.action.statPotDice"));
			btStatPotDice.addActionListener(this);
			btStatPotDice.setActionCommand(ACTION_STAT_POT_DICE_PREFIX+stat.name());
			Bindings.bind(btStatPotDice, "enabled", enabledValueHolder);
			Bindings.bind(btStatPotDice, "visible", beanAdapter.getValueModel("statPotButtonsVisible"));
			Bindings.bind(btStatPotDice, "text", beanAdapter.getValueModel(stat.name()+"StatPotDice"));
			col2.gridx++;
			add(btStatPotDice, col2);
			/* stat gain dice */
			JButton btStatGainDice = new JButton(RESOURCE.getString("ui.stats.action.statsGainDice"));
			btStatGainDice.addActionListener(this);
			btStatGainDice.setActionCommand(ACTION_STAT_GAIN_DICE+stat.name());
			Bindings.bind(btStatGainDice, "enabled", enabledValueHolder);
			Bindings.bind(btStatGainDice, "visible", beanAdapter.getValueModel("statGainButtonsVisible"));
			col2.gridx++;
			add(btStatGainDice, col2);
		}
		/* temp Attribute Sum (costs) */
		col1.gridy++;
		col2.gridy++;
		col2.gridx = 1;
		col2.anchor = GridBagConstraints.CENTER;
		tempStatSumField = new JTextField();
		tempStatSumField.setEditable(false);
		tempStatSumField.setEnabled(false);
		Dimension preferredSize = tempStatSumField.getPreferredSize();
		preferredSize.width = PREFERRED_FIELD_WIDTH;
		tempStatSumField.setPreferredSize(preferredSize );
		add(tempStatSumField, col2);
		Bindings.bind(tempStatSumField, beanAdapter.getValueModel(RMSheet.PROPERTY_STAT_COST_SUM));
		
		/* stat pot all fixed */
		col2.gridx = 2;
		col2.gridwidth = 5;
		col2.anchor = GridBagConstraints.WEST;
		JButton btStatPotFixed = new JButton(RESOURCE.getString("ui.stats.action.statPotFixed"));
		btStatPotFixed.setActionCommand(ACTION_ALL_STAT_POT_FIXED);
		Bindings.bind(btStatPotFixed, "enabled", enabledValueHolder);
		Bindings.bind(btStatPotFixed, "visible", beanAdapter.getValueModel("statPotButtonsVisible"));
		btStatPotFixed.addActionListener(this);
		add(btStatPotFixed, col2);
		
		/* APs*/
		col1.gridy++;
		col2.gridy++;
		col2.gridx = 1;
		col2.gridwidth = 1;
		col2.anchor = GridBagConstraints.CENTER;
		add(new JLabel(RESOURCE.getString("rolemaster.dps")+":"), col1);
		devPointField = new JTextField();
		devPointField.setEditable(false);
		devPointField.setEnabled(false);
		preferredSize = devPointField.getPreferredSize();
		preferredSize.width = PREFERRED_FIELD_WIDTH;
		devPointField.setPreferredSize(preferredSize );
		add(devPointField, col2);
		Bindings.bind(devPointField, beanAdapter.getValueModel(RMSheet.PROPERTY_DEVPPOINTS));
		
		/**/
	}
	
	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_ALL_STAT_POT_FIXED.equals(e.getActionCommand())) {
			/* check all if 0 */
			for (StatEnum stat : StatEnum.values()) {
				if (!"0".equals(beanAdapter.getValue(stat.name()+"Pot"))) {
					JOptionPane.showMessageDialog(StatPanel.this, RESOURCE.getString("error.ui.stat.statPotFixed.not0"));
					return;
				}		
			}
			for (StatEnum stat : StatEnum.values()) {
				int tempStat = Integer.parseInt( beanAdapter.getValue(stat.name()+"Temp").toString() );
				JTextField tf = attributeFields.get(stat).get(COL_STAT_POT);
				tf.setText(""+StatGainGenerator.getStatPotFix(tempStat));
			}
		} else if (e.getActionCommand().startsWith(ACTION_STAT_POT_DICE_PREFIX)) {
			StatEnum stat = StatEnum.valueOf(e.getActionCommand().substring(ACTION_STAT_POT_DICE_PREFIX.length()));
			int tempStat = Integer.parseInt( beanAdapter.getValue(stat.name()+"Temp").toString() );
			JTextField tf = attributeFields.get(stat).get(COL_STAT_POT);
			tf.setText(""+StatGainGenerator.getStatPotDice(tempStat));
		} else if (e.getActionCommand().startsWith(ACTION_STAT_GAIN_DICE)) {
			StatEnum stat = StatEnum.valueOf(e.getActionCommand().substring(ACTION_STAT_GAIN_DICE.length()));
			int tempStat = Integer.parseInt( beanAdapter.getValue(stat.name()+"Temp").toString() );
			int potStat = Integer.parseInt( beanAdapter.getValue(stat.name()+"Pot").toString() );
			int dice1 = DiceUtils.roll(1, 10);
			int dice2 = DiceUtils.roll(1, 10);
			JPanel dicePanel = new JPanel(new GridLayout(2, 2));
			dicePanel.add(new JLabel(RESOURCE.getString("ui.stats.action.statsGain.dice1")));
			dicePanel.add(new JLabel(RESOURCE.getString("ui.stats.action.statsGain.dice2")));
			JTextField tfDice1 = new JTextField(""+dice1);
			JTextField tfDice2 = new JTextField(""+dice2);
			dicePanel.add(tfDice1);
			dicePanel.add(tfDice2);
			int result = JOptionPane.showConfirmDialog(StatPanel.this, dicePanel, RESOURCE.getString("ui.stats.action.statsGainDice") + ": "+RESOURCE.getString("StatEnum."+stat.name()+".short"), JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					dice1 = Integer.parseInt(StringUtils.trimToEmpty(tfDice1.getText()));
					dice2 = Integer.parseInt(StringUtils.trimToEmpty(tfDice2.getText()));
					if (dice1 < 1 || dice1 > 10 || dice2 < 1 || dice2 > 10) {
						throw new NumberFormatException();
					}
					int newPotStat = StatGainGenerator.getStatGainDice(tempStat, potStat, dice1, dice2);
					JTextField tf = attributeFields.get(stat).get(COL_STAT_TEMP);
					tf.setText(""+newPotStat);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(StatPanel.this, RESOURCE.getString("error.ui.stat.statsGain.nonumber"), RESOURCE.getString("ui.stats.action.statsGainDice"), JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (ACTION_ALL_STAT_TEMP_DICE.equals(e.getActionCommand())) {
			new CharacterGenerator(null, rmSheetAdapter, null).distributeStats();
		} else if (ACTION_ALL_STAT_POT_DICE.equals(e.getActionCommand())) {
			new CharacterGenerator(null, rmSheetAdapter, null).generatePotStats();
		}
	}
}
