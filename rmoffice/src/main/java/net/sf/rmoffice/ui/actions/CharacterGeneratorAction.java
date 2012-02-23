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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.generator.CharacterGenerator;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.ui.models.LongRunningUIModel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * 
 */
public class CharacterGeneratorAction implements ActionListener {
	private final static Logger log = LoggerFactory.getLogger(CharacterGeneratorAction.class);
	static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

	
	private final MetaData data;
	private final BeanAdapter<RMSheet> adapter;
	private final BeanAdapter<Characteristics> characteristicsAdapter;
	private final Component parent;
	private final LongRunningUIModel longRunningModel;

	/**
	 * @param parent the parent frame for dialogs
	 * @param data the {@link MetaData}
	 * @param adapter the bean adapter
	 * @param characteristicsAdapter the {@link Characteristics} adapter
	 * @param longRunAdapter the long running model
	 * 
	 */
	public CharacterGeneratorAction(Component parent, MetaData data, BeanAdapter<RMSheet> adapter, BeanAdapter<Characteristics> characteristicsAdapter, LongRunningUIModel longRunAdapter) {
		this.parent = parent;
		this.data = data;
		this.adapter = adapter;
		this.characteristicsAdapter = characteristicsAdapter;
		this.longRunningModel = longRunAdapter;
	}
	
	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(parent, RESOURCE.getString("ui.menu.generatecharacter.confirm"),
				RESOURCE.getString("ui.menu.generatecharacter"), JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Thread t = new Thread("generator thread") {
				@Override
				public void run() {
					try {
						/* start progress bar */
						long level = adapter.getBean().getLevel();
						longRunningModel.startProgress(7 + 2 * (int)level);
						stepDone("ui.basic.charactername");
						if (log.isInfoEnabled()) log.info("generating all missing information of this character");
						final CharacterGenerator gen = new CharacterGenerator(characteristicsAdapter, adapter, data);
						/* generate the character name  */
						if (StringUtils.isEmpty(adapter.getBean().getCharacterName())) {
							gen.generateName();
						}
						stepDone("ui.tab.stats");
						/* stats */
						gen.distributeStats();
						stepDone("ui.tab.stats");
						gen.generatePotStats();
						stepDone("ui.tab.characteristics");
						gen.generateCharacteristics();
						stepDone("ui.generatecharacter.generate.prepare");
						/* level ups */
						gen.levelUpPrepare();
						stepDone("ui.generatecharacter.generate.hobby");
						gen.levelUpHobbyAndLanguages();
						String lvlMsg = MessageFormat.format(RESOURCE.getString("ui.generatecharacter.generate.level"), Integer.valueOf(1));
						stepDone(lvlMsg);
						for (long lvl=1; lvl <= level; lvl++) {
							lvlMsg = MessageFormat.format(RESOURCE.getString("ui.generatecharacter.generate.level"), Long.valueOf(lvl));
							gen.doStatGains();
							stepDone(lvlMsg);
							gen.levelUpSkillsAndCategories(lvl);
							if (lvl == level) {
								lvlMsg = "ui.generatecharacter.generate.finish";
							}
							stepDone(lvlMsg);
						}
						gen.levelUpFinish();
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								adapter.getBean().fireSkillStructureChanged();
							}
						});
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						log.error(e.getMessage(), e);
					} finally {
						longRunningModel.done();
					}
				}

				private void stepDone(String nextStep) {
					longRunningModel.workDone(1, nextStep);
				}

			};
			t.setDaemon(true);
			t.start();
		}
	}
}
