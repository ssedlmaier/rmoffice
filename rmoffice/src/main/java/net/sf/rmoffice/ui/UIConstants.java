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
package net.sf.rmoffice.ui;

import java.awt.Color;

import javax.swing.ImageIcon;

import net.sf.rmoffice.ui.panels.InfoPagePanel;


/**
 * 
 */
public final class UIConstants {
	/** background color for all editable table cells */
	public static final Color COLOR_EDITABLE_BG = new Color(1f, 1f, 0.75f);
	
	public static final Color COLOR_SELECTION_BG = new Color(195, 212, 232);
	public static final Color COLOR_SELECTION_FG = Color.BLACK;
	
	/** table row height, because I could not set Table.rowHeight in UIManager
	 * (for Mac L&F we need at least 20 to paint the JSpinner buttons, but 22 looks nice 
	 * and the Mac JSpinner buttons are painted completely) */
	public static final int TABLE_ROW_HEIGHT = 22;
	
	public static final ImageIcon FRAME_ICON = new ImageIcon(RMFrame.class.getResource("/images/rmo48.png"));
	public static final ImageIcon RMO_LOGO125 = new ImageIcon(RMFrame.class.getResource("/images/rmo125.png"));
	public static final ImageIcon ICON_ARRUP = new ImageIcon(RMFrame.class.getResource("/images/icons/arrup.png"));
	public static final ImageIcon ICON_ARRDOWN = new ImageIcon(RMFrame.class.getResource("/images/icons/arrdown.png"));
	public static final ImageIcon ICON_DELETE = new ImageIcon(RMFrame.class.getResource("/images/icons/del.png"));
	public static final ImageIcon ICON_EDIT = new ImageIcon(RMFrame.class.getResource("/images/icons/edit.png"));
	public static final ImageIcon ICON_NEWEDIT = new ImageIcon(RMFrame.class.getResource("/images/icons/newedit.png"));
	public static final ImageIcon ICON_NEWLINE = new ImageIcon(RMFrame.class.getResource("/images/icons/newline.png"));
	public static final ImageIcon ICON_NEW = new ImageIcon(RMFrame.class.getResource("/images/icons/new.png"));
	public static final ImageIcon ICON_NEWITEM = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/newitem.png"));
	public static final ImageIcon ICON_WAND = new ImageIcon(RMFrame.class.getResource("/images/icons/wand.png"));
	
	public static final ImageIcon ICON_PDF = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/pdf.png"));
	public static final ImageIcon ICON_SAVE = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/save.png"));
	public static final ImageIcon ICON_SAVE_AS = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/save-as.png"));
	public static final ImageIcon ICON_NEW_SHEET = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/newsheet.png"));
	public static final ImageIcon ICON_OPEN = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/open.png"));
	public static final ImageIcon ICON_GEN_NAME = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/gen-name.png"));
	public static final ImageIcon ICON_GEN_CHARACTERISTICS = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/gen-characteristics.png"));
	public static final ImageIcon ICON_GEN_ALL = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/gen.png"));
	public static final ImageIcon ICON_HELP = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/help.png"));
	public static final ImageIcon ICON_HELP_SMALL = new ImageIcon(InfoPagePanel.class.getResource("/images/icons/help_22.png"));
}
