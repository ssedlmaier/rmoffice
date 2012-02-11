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
package net.sf.rmoffice.ui.renderer;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.jgoodies.binding.beans.BeanAdapter;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.ui.models.SkillTreeNode;


/**
 * Renderer for the selection tree in the skill panel.
 */
public class SkillTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final BeanAdapter<RMSheet> rmSheetAdapter;

	/**
	 * @param rmSheetAdapter
	 */
	public SkillTreeRenderer(BeanAdapter<RMSheet> rmSheetAdapter) {
		this.rmSheetAdapter = rmSheetAdapter;
	}

	/** {@inheritDoc} */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value != null && value instanceof SkillTreeNode) {
			Object userObject = ((SkillTreeNode)value).getUserObject();
			if (userObject instanceof ISkill) {
				ISkill skill = (ISkill)userObject;
				StringBuilder sb = new StringBuilder();
				sb.append(skill.getName());
				SkillType skillType = rmSheetAdapter.getBean().getSkillType(skill);
				if (!SkillType.DEFAULT.equals(skillType)) {
					sb.append(" [").append( RESOURCE.getString("SkillType."+skillType.name())).append("]");
				}
				label.setText(sb.toString());
			} else if (userObject instanceof SkillCategory) {
				SkillCategory sg = (SkillCategory) userObject;
				Skillcost skillcost = rmSheetAdapter.getBean().getSkillcost(sg);
				StringBuilder sb = new StringBuilder();
				sb.append(sg.getName());
				sb.append(" [");
				for (int i=0; i< skillcost.size(); i++) {
					if (i > 0) {
						sb.append("/");
					}
					sb.append(""+skillcost.getCost(i));
				}
				sb.append("]");
				label.setText(sb.toString());
			}
		}
		return label;
	}
}
