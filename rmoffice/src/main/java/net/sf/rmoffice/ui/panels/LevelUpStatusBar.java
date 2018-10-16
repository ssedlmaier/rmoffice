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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.rmoffice.RMPreferences;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Font;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;


/**
 * A panel that shows information about the current level up mode. 
 */
public class LevelUpStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

	private static final Icon WARN = new ImageIcon(LevelUpStatusBar.class.getResource("/images/icons/warning.png"));
	
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lblSpellRanks;

	/**
	 * 
	 */
	public LevelUpStatusBar() {
		super(new GridBagLayout());
		setBackground(new Color(137, 187, 247));
		setVisible(false);
		setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), RESOURCE.getString("ui.levelup.title")));
		/* init components */
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(4, 10, 4, 10);
		c.gridx = 0; c.gridy = 0;
		c.weightx = 0.5;
		/* */
		lbl1 = new JLabel("");
		add(lbl1, c);
		/* spell Ranks */
		lblSpellRanks = new JLabel("");
		lblSpellRanks.setFont(lblSpellRanks.getFont().deriveFont(Font.BOLD));
		if (RMPreferences.getInstance().getSpelllistDPIncrease() > 0) {
			c.gridy++;
			add(lblSpellRanks, c);
		}
		/* */
		c.gridy = 0;
		c.gridheight = 2;
		c.gridx++;
		lbl2 = new JLabel("");
		add(lbl2, c);
	}
	
	public void setText(String text) {
		if (StringUtils.trimToEmpty(text).startsWith("WARNING")) {
			lbl2.setText(StringUtils.trimToEmpty(text).substring(7));
			lbl2.setIcon(WARN);
		} else {
			lbl2.setIcon(null);
			lbl2.setText(text);
		}
	}
	
	public String getText() {
		return lbl2.getText();
	}
	
	public void setDevPoints(String text) {
		if (StringUtils.trimToNull(text) == null) {
			setVisible(false);
		} else if (!isVisible()) {
			setVisible(true);
		}
		lbl1.setText(text);
	}
	
	public String getDevPoints() {
		return lbl1.getText();
	}
	
	public String getSpellRanks() {
		return lblSpellRanks.getText();
	}
	
	public void setSpellRanks(String spellRankText) {
		lblSpellRanks.setText(spellRankText);
	}
}
