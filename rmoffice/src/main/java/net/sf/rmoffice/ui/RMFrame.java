/*
 * Copyright 2012 Daniel Nettesheim
 * Modifications Copyright 2019 Stefan Sedlmaier
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.SearchableBar;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TreeSearchable;

import net.sf.rmoffice.RMOffice;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.Coins;
import net.sf.rmoffice.core.ExportImport;
import net.sf.rmoffice.core.RMLevelUp;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.RMSheet.State;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.pdf.PDFVersion;
import net.sf.rmoffice.ui.actions.CharacterCharacteristicsAction;
import net.sf.rmoffice.ui.actions.CharacterGeneratorAction;
import net.sf.rmoffice.ui.actions.CharacterNameAction;
import net.sf.rmoffice.ui.actions.CreatePDFAction;
import net.sf.rmoffice.ui.actions.DesktopAction;
import net.sf.rmoffice.ui.actions.SaveAction;
import net.sf.rmoffice.ui.actions.SaveAsAction;
import net.sf.rmoffice.ui.dialog.ModifySkillDialog;
import net.sf.rmoffice.ui.editor.NumberSpinnerTableCellEditor;
import net.sf.rmoffice.ui.models.BasicPresentationModel;
import net.sf.rmoffice.ui.models.CharacteristicsPresentationModel;
import net.sf.rmoffice.ui.models.LongRunningUIModel;
import net.sf.rmoffice.ui.models.SkillTreeNode;
import net.sf.rmoffice.ui.models.SkillcategoryTableModel;
import net.sf.rmoffice.ui.models.SkillsTableModel;
import net.sf.rmoffice.ui.models.StatValueModelAdapter;
import net.sf.rmoffice.ui.models.TalentFlawPresentationModel;
import net.sf.rmoffice.ui.panels.BasicPanel;
import net.sf.rmoffice.ui.panels.CharacteristicsPanel;
import net.sf.rmoffice.ui.panels.CoinsPanel;
import net.sf.rmoffice.ui.panels.EquipmentPanel;
import net.sf.rmoffice.ui.panels.HerbsPanel;
import net.sf.rmoffice.ui.panels.InfoPagePanel;
import net.sf.rmoffice.ui.panels.LevelUpStatusBar;
import net.sf.rmoffice.ui.panels.MagicalItemPanel;
import net.sf.rmoffice.ui.panels.ProgressGlassPane;
import net.sf.rmoffice.ui.panels.StatPanel;
import net.sf.rmoffice.ui.panels.TalentFlawPanel;
import net.sf.rmoffice.ui.panels.TodoPanel;
import net.sf.rmoffice.ui.renderer.BonusFormatTableCellRenderer;
import net.sf.rmoffice.ui.renderer.ColoredBooleanRenderer;
import net.sf.rmoffice.ui.renderer.NumberSpinnerTableRenderer;
import net.sf.rmoffice.ui.renderer.SkillTreeRenderer;
import net.sf.rmoffice.ui.renderer.SkillnameTableRenderer;
import net.sf.rmoffice.util.RMOFileFilter;


/**
 *
 */
public class RMFrame extends JFrame implements PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private final static Logger log = LoggerFactory.getLogger(RMFrame.class);
    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
    private static final String TITLE = "RoleMaster Office";

    private JTabbedPane tabbedPane;
    private Map<SkillCategory, JTextField> skillcostFields = new HashMap<SkillCategory, JTextField>();
    private List<SkillCategory> modifiableSkillgroups = new ArrayList<SkillCategory>();
    private RMSheet sheet;
    private MetaData data;
    private boolean refreshing = false;
    private JTable skillcatTable;
    private JTable skillsTable;
    private SkillsTableModel skillModel;

    private File currentFile;
    private SkillTreeNode skillSelectionRootNode;
    private JTree skillsSelectionTree;
    private JTextArea taSkillDescription;
    private TodoPanel toDoPanel;

    private BeanAdapter<Characteristics> characteristicsAdapter;
    private BeanAdapter<RMSheet> rmsheetAdapter;
    private BeanAdapter<Coins> coins;
    private ValueHolder enableValueHolder = new ValueHolder(false);
    private ValueHolder enableMenuSaveValueHolder = new ValueHolder(false);
    private ValueHolder enableMenuExportPDFValueHolder = new ValueHolder(false);
    private BeanAdapter<StatValueModelAdapter> statValueModelAdapter;
    private SkillcategoryTableModel skillcatModel;
    private JMenu newLatestVersionMenu;
    private LongRunningUIModel longRunAdapter;
    private ProgressGlassPane glassPane;

    /**
     *
     */
    public RMFrame() {
        super(TITLE);
        setPreferredSize(new Dimension(1000, 800));
        setIconImage(UIConstants.FRAME_ICON.getImage());
        glassPane = new ProgressGlassPane();
        setGlassPane(glassPane);
        longRunAdapter = new LongRunningUIModel(glassPane);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLookAndFeel();
    }

    private static void setLookAndFeel() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            log.info("os.name = {}", osName);
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (ClassNotFoundException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (InstantiationException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (IllegalAccessException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        }
    }

    public void init(MetaData data) {
        this.data = data;
        createCleanRMSheet();

        initMenu();
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        toDoPanel = new TodoPanel();
        toDoPanel.setSheet(sheet);
        toDoPanel.init();
        panel.add(toDoPanel, BorderLayout.NORTH);
        tabbedPane = new JideTabbedPane();
        BasicPresentationModel basicModel = new BasicPresentationModel(this, getRMSheetAdapter(), data, enableValueHolder);
        BasicPanel basicPanel = new BasicPanel(basicModel, enableValueHolder);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.basic"), new JScrollPane(basicPanel));
        /* ---- characteristics / BG / talents & flaws */
        JTabbedPane characteristicsTabs = new JideTabbedPane();
        CharacteristicsPresentationModel characteristicsModel = new CharacteristicsPresentationModel(getCharacteristicsAdapter().getBeanChannel());
        CharacteristicsPanel characteristicsPanel = new CharacteristicsPanel(characteristicsModel, getRMSheetAdapter(), enableValueHolder);
        characteristicsTabs.addTab(RESOURCE.getString("ui.tab.characteristics"), null, createNorthPanel(characteristicsPanel));

        TalentFlawPresentationModel talentPresModel = new TalentFlawPresentationModel(getRMSheetAdapter(), enableValueHolder, this, data);
        TalentFlawPanel talentPanel = new TalentFlawPanel(talentPresModel, data, getRMSheetAdapter());
        characteristicsTabs.addTab(RESOURCE.getString("ui.tab.talentflaws"), null, createNorthPanel(talentPanel));
        tabbedPane.addTab(RESOURCE.getString("ui.tab.characteristics"), null, characteristicsTabs);
        /* stats */
        StatPanel statPanel = new StatPanel(getStatValueModelAdapter(), getRMSheetAdapter(), enableValueHolder);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(statPanel, BorderLayout.WEST);
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(panel1, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(panel2);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.stats"), null, pane);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.skillgroups"), null, initSkillcategoryPanel());
        tabbedPane.addTab(RESOURCE.getString("ui.tab.weaponcost"), null, createNorthPanel(initWeaponCategories()));
        tabbedPane.addTab(RESOURCE.getString("ui.tab.skills"), null, initSkillsPanel());
        /* Equipment Panel */
        JTabbedPane equipmentTabs = new JideTabbedPane();
        EquipmentPanel equipmentPanel = new EquipmentPanel(getRMSheetAdapter());
        equipmentTabs.addTab(RESOURCE.getString("ui.tab.equipment"), equipmentPanel);
        /* Equipment/Coins Panel */
        CoinsPanel coinsPanel = new CoinsPanel(getCoinsAdapter());
        equipmentTabs.addTab(RESOURCE.getString("rolemaster.money.header"), createNorthPanel(coinsPanel));
        /* MagicalItem Panel */
        MagicalItemPanel itemPanel = new MagicalItemPanel(getRMSheetAdapter(), enableValueHolder);
        equipmentTabs.addTab(RESOURCE.getString("ui.tab.items"), null, createNorthPanel(itemPanel));
        /* Herbs */
        HerbsPanel herbsPanel = new HerbsPanel(getRMSheetAdapter());
        equipmentTabs.addTab(RESOURCE.getString("ui.tab.herbs"), herbsPanel);
        /* add tabs to parent tab pane*/
        tabbedPane.addTab(RESOURCE.getString("ui.tab.equipment"), null, equipmentTabs);
        /* Info Panel */
        InfoPagePanel infoPanel = new InfoPagePanel(getRMSheetAdapter());
        Bindings.bind(infoPanel, "enabled", enableValueHolder);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.info"), null, infoPanel);

        panel.add(tabbedPane);
        add(panel, BorderLayout.CENTER);
        LevelUpStatusBar statusBar = new LevelUpStatusBar();
        add(statusBar, BorderLayout.SOUTH);
        initPropertyChangeListener();
        sheet.setMetaData(data);
        sheet.init();
        Bindings.bind(statusBar, "text", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_STATUSTEXT, "getLvlUpStatusText", "setLvlUpStatusText"));
        Bindings.bind(statusBar, "devPoints", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_DEVPOINTS, "getLvlUpDevPoints", "setLvlUpDevPoints"));
        Bindings.bind(statusBar, "spellRanks", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_SPELLLISTS, "getLvlUpSpellLists", "setLvlUpSpellLists"));
    }

    /**
     *
     */
    private void createCleanRMSheet() {
        sheet = new RMSheet();
        updateAdapters();
    }

    private JComponent createNorthPanel(JComponent comp) {
        JPanel bgPanel = new JPanel(new BorderLayout());
        bgPanel.add(comp, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(bgPanel);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        return pane;
    }

    private JComponent initWeaponCategories() {
        /* search the switchable skillgroups */
        modifiableSkillgroups = new ArrayList<SkillCategory>();
        for (SkillCategory group : data.getSkillCategories()) {
            if (group.getRankType().isCostSwitchable()) {
                modifiableSkillgroups.add(group);
            }
        }
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints col1 = new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col2 = new GridBagConstraints(1, 0, 1, 1, 0.3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col3 = new GridBagConstraints(2, 0, 1, 1, 0.1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col4 = new GridBagConstraints(3, 0, 1, 1, 0.1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        /* create labels */
        ActionListener listenerUp = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = Integer.parseInt(e.getActionCommand());
                SkillCategory sg1 = modifiableSkillgroups.get(idx);
                SkillCategory sg2 = modifiableSkillgroups.get(idx - 1);
                sheet.switchSkillCategoryCosts(sg1, sg2);
                refreshing = true;
                sheet2ui();
                refreshing = false;
            }
        };
        ActionListener listenerDown = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = Integer.parseInt(e.getActionCommand());
                SkillCategory sg1 = modifiableSkillgroups.get(idx);
                SkillCategory sg2 = modifiableSkillgroups.get(idx + 1);
                sheet.switchSkillCategoryCosts(sg1, sg2);
                refreshing = true;
                sheet2ui();
                refreshing = false;
            }
        };
        for (int i = 0; i < modifiableSkillgroups.size(); i++) {
            panel.add(new JLabel(modifiableSkillgroups.get(i).getName()), col1);
            /* costs Textfield*/
            JTextField tfCost = new JTextField("? / ?");
            tfCost.setEditable(false);
            tfCost.setFocusable(false);
            tfCost.setPreferredSize(new Dimension(60, UIConstants.TABLE_ROW_HEIGHT));
            skillcostFields.put(modifiableSkillgroups.get(i), tfCost);
            panel.add(tfCost, col2);
            /* up button*/
            if (i > 0) {
                JButton btUp = new JButton(UIConstants.ICON_ARRUP);
                Bindings.bind(btUp, "enabled", enableValueHolder);
                btUp.addActionListener(listenerUp);
                btUp.setActionCommand("" + i);
                panel.add(btUp, col3);
            }
            if (i < (modifiableSkillgroups.size() - 1)) {
                JButton btDown = new JButton(UIConstants.ICON_ARRDOWN);
                Bindings.bind(btDown, "enabled", enableValueHolder);
                btDown.addActionListener(listenerDown);
                btDown.setActionCommand("" + i);
                panel.add(btDown, col4);
            }
            col1.gridy++;
            col2.gridy++;
            col3.gridy++;
            col4.gridy++;
        }
        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.add(panel, BorderLayout.WEST);
        return positionPanel;
    }

    /* TODO extract to a binded model and panel */
    private Component initSkillsPanel() {
        skillSelectionRootNode = new SkillTreeNode();
        skillsSelectionTree = new JTree(skillSelectionRootNode);
        skillsSelectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        skillsSelectionTree.setEditable(false);
        skillsSelectionTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (e.getNewLeadSelectionPath() != null) {
                    Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
                    if (lastPathComponent != null && lastPathComponent instanceof SkillTreeNode) {
                        SkillTreeNode node = (SkillTreeNode) lastPathComponent;
                        if (node.getUserObject() instanceof ISkill) {
                            ISkill skill = (ISkill) node.getUserObject();
                            String descr = skill.getDescription(sheet.getLengthUnit());
                            taSkillDescription.setText(descr);
                        }
                    }
                }
            }
        });
        skillsSelectionTree.setCellRenderer(new SkillTreeRenderer(getRMSheetAdapter()));
        skillsSelectionTree.addMouseListener(new MouseAdapter() {
            /** {@inheritDoc} */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addCurrentSelectedISkill(false);
                }
            }
        });
        TreeSearchable searchable = SearchableUtils.installSearchable(skillsSelectionTree);
        searchable.setRepeats(true);
        searchable.setRecursive(true);

        /* Skill Table */
        skillModel = new SkillsTableModel();
        skillModel.setSheet(sheet);
        skillsTable = new JTable(skillModel);
        skillsTable.getTableHeader().setReorderingAllowed(false);
        skillsTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
        skillsTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
        skillsTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        /* sorting*/
        TableColumn orderGroupCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_ORDERGROUP);
        orderGroupCol.setPreferredWidth(50);
        NumberSpinnerTableCellEditor orderSpinner = new NumberSpinnerTableCellEditor(1, true, 0);
        orderGroupCol.setCellEditor(orderSpinner);
        NumberSpinnerTableRenderer orderRenderer = new NumberSpinnerTableRenderer();
        orderGroupCol.setCellRenderer(orderRenderer);
        /* skill */
        TableColumn skillCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_SKILL);
        skillCol.setCellRenderer(new SkillnameTableRenderer());
        skillCol.setPreferredWidth(200);
        /* fav */
        TableColumn favCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_FAVORITE);
        favCol.setPreferredWidth(30);
        favCol.setCellRenderer(new ColoredBooleanRenderer());
        /* rank column*/
        TableColumn rankCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_RANK);
        rankCol.setPreferredWidth(50);
        NumberSpinnerTableCellEditor spinner = new NumberSpinnerTableCellEditor(0.5);
        rankCol.setCellEditor(spinner);
        NumberSpinnerTableRenderer spinnRenderer = new NumberSpinnerTableRenderer();
        rankCol.setCellRenderer(spinnRenderer);
        /* cost */
        skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_COST).setPreferredWidth(50);
        /* calculated special bonus */
        TableColumn special2Col = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_SPECIAL2_BONUS);
        special2Col.setCellRenderer(new BonusFormatTableCellRenderer(true));
        /* user special bonus col */
        TableColumn specialCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_SPECIAL_BONUS);
        specialCol.setPreferredWidth(50);
        NumberSpinnerTableCellEditor specialBonusSpinner2 = new NumberSpinnerTableCellEditor(1, true, -9999);
        specialCol.setCellEditor(specialBonusSpinner2);
        NumberSpinnerTableRenderer spinnRenderer2 = new NumberSpinnerTableRenderer();
        specialCol.setCellRenderer(spinnRenderer2);
        TableColumn totalBonus = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_TOTAL_BONUS);
        totalBonus.setPreferredWidth(50);
        totalBonus.setCellRenderer(new BonusFormatTableCellRenderer(false));
        skillsTable.setAutoCreateRowSorter(true);

        /* Button Panel*/
        JPanel buttonsPanel = new JPanel();
        JButton btAdd = new JButton(RESOURCE.getString("ui.skills.btAdd"), UIConstants.ICON_NEWLINE);
        Bindings.bind(btAdd, "enabled", enableValueHolder);
        btAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCurrentSelectedISkill(false);
            }
        });
        buttonsPanel.add(btAdd);

        JButton btEditBeforeAdd = new JButton(RESOURCE.getString("ui.skills.btEditBeforeAdd"), UIConstants.ICON_NEWEDIT);
        Bindings.bind(btEditBeforeAdd, "enabled", enableValueHolder);
        btEditBeforeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCurrentSelectedISkill(true);
            }
        });
        buttonsPanel.add(btEditBeforeAdd);
        /* inset */
        buttonsPanel.add(new JLabel(" | "));

        final ValueHolder skillSelectedValuseHolder = new ValueHolder(false);
        ListSelectionModel skillSelectionModel = skillsTable.getSelectionModel();
        skillSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        skillSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    skillSelectedValuseHolder.setValue(skillsTable.getSelectionModel().getMinSelectionIndex() > -1);
                }
            }
        });

        /* Rename */
        JButton btEdit = new JButton(RESOURCE.getString("ui.skills.btEdit"), UIConstants.ICON_EDIT);
        Bindings.bind(btEdit, "enabled", skillSelectedValuseHolder);
        btEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyExistingISkill();
            }
        });
        buttonsPanel.add(btEdit);

        /* Remove */
        JButton btRemove = new JButton(RESOURCE.getString("ui.skills.btRemove"), UIConstants.ICON_DELETE);
        Bindings.bind(btRemove, "enabled", skillSelectedValuseHolder);
        btRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (skillsTable.getSelectedRow() > -1) {
                    int actualRow = skillsTable.convertRowIndexToModel(skillsTable.getSelectedRow());
                    skillModel.removeRow(actualRow);
                }
            }
        });
        buttonsPanel.add(btRemove);

        /* Scroll bar */
        JScrollPane treechoserPane = new JScrollPane(skillsSelectionTree);
        final JPanel leftPanel = new JPanel(new BorderLayout());
        taSkillDescription = new JTextArea();
        taSkillDescription.setEditable(false);
        taSkillDescription.setWrapStyleWord(true);
        taSkillDescription.setLineWrap(true);
        taSkillDescription.setRows(2);


        leftPanel.add(new JScrollPane(taSkillDescription), BorderLayout.NORTH);
        leftPanel.add(treechoserPane, BorderLayout.CENTER);
        final SearchableBar searchableBar = new SearchableBar(searchable, true);
        int visibleBt = SearchableBar.SHOW_NAVIGATION | SearchableBar.SHOW_STATUS;
        searchableBar.setVisibleButtons(visibleBt);
        leftPanel.add(searchableBar, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0);
        splitPane.add(leftPanel);
        splitPane.add(new JScrollPane(skillsTable));

        /* w*/
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonsPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void createSkillSelectionData(SkillTreeNode rootNode) {
        /* prepare groups as folders */
        Map<SkillCategory, SkillTreeNode> folders = new HashMap<SkillCategory, SkillTreeNode>();
        for (SkillCategory group : data.getSkillCategories()) {
            SkillTreeNode node = new SkillTreeNode();
            node.setUserObject(group);
            rootNode.add(node);
            folders.put(group, node);
        }
        /* add skills */
        if (sheet.getRace() != null) {
            for (ISkill skill : data.getSkills()) {
                if (
                        (skill.getScope() == null || skill.getScope() != null
                                && skill.getScope().equals(sheet.getRace().getScope())
                        ) && (
                                !RMPreferences.getInstance().isExcluded(skill.getSource())
                                        && !RMPreferences.getInstance().isExcludedSkillId(skill.getId())
                        )
                ) {
                    SkillTreeNode skillnode = new SkillTreeNode();
                    skillnode.setUserObject(skill);
                    SkillTreeNode parentNode = folders.get(sheet.getSkillcategory(skill));
                    if (parentNode != null) {
                        parentNode.add(skillnode);
                    }
                }
            }
        }
    }

    /* TODO extract to a binded model and panel */
    private Component initSkillcategoryPanel() {

        skillcatModel = new SkillcategoryTableModel(data);
        skillcatModel.setSheet(sheet);
        skillcatTable = new JTable(skillcatModel);
        skillcatTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
        skillcatTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);

        skillcatTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        skillcatTable.setTableHeader(new JTableHeader(skillcatTable.getColumnModel()));
        skillcatTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_NAME).setPreferredWidth(200);
        skillcatTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_STAT).setPreferredWidth(40);
        skillcatTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_AP_COST).setPreferredWidth(40);

        /* rank renderer/editor*/
        TableColumn rankCol = skillcatTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_RANKS);
        rankCol.setPreferredWidth(20);
        NumberSpinnerTableCellEditor rankSpinner = new NumberSpinnerTableCellEditor(1, false, 0);
        rankCol.setCellEditor(rankSpinner);
        NumberSpinnerTableRenderer spinnRenderer = new NumberSpinnerTableRenderer();
        rankCol.setCellRenderer(spinnRenderer);

        /* special 2 bonus */
        TableColumn spec2Col = skillcatTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_SPEC2_BONUS);
        NumberSpinnerTableCellEditor specialBonusSpinnerCat = new NumberSpinnerTableCellEditor(1, true, -9999);
        spec2Col.setCellEditor(specialBonusSpinnerCat);
        NumberSpinnerTableRenderer spinnRenderer2 = new NumberSpinnerTableRenderer(true);
        spec2Col.setCellRenderer(spinnRenderer2);

        /**/
        for (int i = 4; i < 10; i++) {
            skillcatTable.getColumnModel().getColumn(i).setPreferredWidth(15);
        }

        /* Scroll bar */
        JScrollPane pane = new JScrollPane(skillcatTable);
        return pane;
    }

    private void initMenu() {
        JMenuBar bar = new JMenuBar();
        /* ---- FILE -----*/
        JMenu file = new JMenu(RESOURCE.getString("ui.menu.file"));
        bar.add(file);

        /* ---- NEW -----*/
        JMenuItem menuNew = new JMenuItem(RESOURCE.getString("ui.menu.new"), UIConstants.ICON_NEW_SHEET);
        menuNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sheet != null) {
                    sheet.dispose();
                }
                setCurrentFile(null);
                createCleanRMSheet();
                initPropertyChangeListener();
                toDoPanel.setSheet(sheet);
                sheet.setMetaData(data);
                sheet.init();
                skillModel.setSheet(sheet);
                skillcatModel.setSheet(sheet);
                sheet2ui();
                tabbedPane.setSelectedIndex(0);
                RMFrame.this.setTitle(TITLE);
            }
        });
        file.add(menuNew);

        /* Open */
        JMenuItem menuOpen = new JMenuItem(RESOURCE.getString("ui.menu.openfile"), UIConstants.ICON_OPEN);
        menuOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Reader freader = null;
                InputStream in = null;
                try {
                    JFileChooser fch = new JFileChooser(RMPreferences.getInstance().getLastDir());
                    fch.setAcceptAllFileFilterUsed(false);
                    fch.setFileFilter(new RMOFileFilter());
                    int result = fch.showOpenDialog(RMFrame.this);
                    if (JFileChooser.APPROVE_OPTION == result) {
                        if (sheet != null) {
                            removePropertyChangeListener();
                        }
                        File selectedFile = fch.getSelectedFile();
                        RMPreferences.getInstance().setLastDir(selectedFile.getParentFile());
                        in = new FileInputStream(selectedFile);
                        freader = new BufferedReader(new InputStreamReader(in, ExportImport.ENCODING));
                        sheet = ExportImport.importFile(selectedFile);
                        sheet.fixBackwardCompatibilites();
                        sheet.setMetaData(data);
                        if (validateExclusions(sheet)) {
                            updateAdapters();
                            setCurrentFile(selectedFile);
                            RMFrame.this.setTitle(selectedFile.getName());
                            initPropertyChangeListener();
                            toDoPanel.setSheet(sheet);
                            sheet.init();
                            if (refreshing) return;
                            refreshing = true;
                            skillModel.setSheet(sheet);
                            skillcatModel.setSheet(sheet);
                            if (RMSheet.State.NORMAL.equals(sheet.getState())) {
                                sheet2ui();
                            }
                        } else {
                            sheet = null;
                        }
                    }
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) log.debug("Exception while opening file: ", ex);
                    JOptionPane.showMessageDialog(RMFrame.this, RESOURCE.getString("error.file.open") + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                } finally {
                    refreshing = false;
                    if (in != null) {
                        try {
                            if (log.isDebugEnabled()) log.debug("closing opened file");
                            in.close();
                        } catch (IOException ex) {
                            JOptionPane.showConfirmDialog(RMFrame.this, ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                            log.error("ignoring Exception while closing the file stream " + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                        }
                    }
                    try {
                        if (freader != null) {
                            if (log.isDebugEnabled()) log.debug("closing input reader");
                            freader.close();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(RMFrame.this, ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                        if (log.isDebugEnabled())
                            log.debug("ignoring Exception while closing file reader " + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                    }
                }
            }

            private boolean validateExclusions(RMSheet sheet) {
                if (sheet.getRace() == null) {
                    JOptionPane.showMessageDialog(RMFrame.this, RESOURCE.getString("error.file.open") + ": " + RESOURCE.getString("error.file.open.raceexcluded"));
                    return false;
                }
                if (sheet.getProfession() == null) {
                    JOptionPane.showMessageDialog(RMFrame.this, RESOURCE.getString("error.file.open") + ": " + RESOURCE.getString("error.file.open.profexcluded"));
                    return false;
                }
                return true;
            }
        });
        file.add(menuOpen);

        /* Export PDF */
        JMenu menuExportPopup = new JMenu(RESOURCE.getString("ui.menu.exportpdf"));
        file.add(menuExportPopup);

        JMenuItem menuCreatePDF = new JMenuItem(RESOURCE.getString("ui.menu.exportpdf"), UIConstants.ICON_PDF);
        CreatePDFAction pdfCreateListener = new CreatePDFAction(this, data, getRMSheetAdapter(), PDFVersion.PDF_FULL_V1, longRunAdapter);
        menuCreatePDF.addActionListener(pdfCreateListener);
        menuExportPopup.add(menuCreatePDF);
        Bindings.bind(menuCreatePDF, "enabled", enableMenuExportPDFValueHolder);

        JMenuItem menuCreatePDF2 = new JMenuItem(RESOURCE.getString("ui.menu.exportpdf") + " (v2)", UIConstants.ICON_PDF);
        CreatePDFAction pdfCreateListener2 = new CreatePDFAction(this, data, getRMSheetAdapter(), PDFVersion.PDF_FULL_V2, longRunAdapter);
        menuCreatePDF2.addActionListener(pdfCreateListener2);
        menuExportPopup.add(menuCreatePDF2);
        Bindings.bind(menuCreatePDF2, "enabled", enableMenuExportPDFValueHolder);

        JMenuItem menuCreatePDFShort = new JMenuItem(RESOURCE.getString("ui.menu.exportpdf.short"), UIConstants.ICON_PDF);
        CreatePDFAction pdfCreateListenerNpc = new CreatePDFAction(this, data, getRMSheetAdapter(), PDFVersion.PDF_MINIMAL_V1, longRunAdapter);
        menuCreatePDFShort.addActionListener(pdfCreateListenerNpc);
        menuExportPopup.add(menuCreatePDFShort);
        Bindings.bind(menuCreatePDFShort, "enabled", enableMenuExportPDFValueHolder);

        /* -------- SAVE --------- */
        JMenuItem menuSave = new JMenuItem(RESOURCE.getString("ui.menu.save"), UIConstants.ICON_SAVE);
        menuSave.setEnabled(false);
        SaveAction saveAction = new SaveAction(this, getRMSheetAdapter());
        menuSave.addActionListener(saveAction);
        menuSave.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        file.add(menuSave);
        Bindings.bind(menuSave, "enabled", enableMenuSaveValueHolder);
        /* -------- SAVE  AS --------- */
        JMenuItem menuSaveAs = new JMenuItem(RESOURCE.getString("ui.menu.saveas"), UIConstants.ICON_SAVE_AS);
        SaveAsAction saveAsAction = new SaveAsAction(this, getRMSheetAdapter());
        menuSaveAs.addActionListener(saveAsAction);
        file.add(menuSaveAs);

        /* ---- GENERATOR MENU --- */
        JMenu generatorMenu = new JMenu(RESOURCE.getString("ui.menu.generator"));
        bar.add(generatorMenu);

        JMenuItem menuGenName = new JMenuItem(RESOURCE.getString("ui.menu.generatename"), UIConstants.ICON_GEN_NAME);
        generatorMenu.add(menuGenName);
        menuGenName.addActionListener(new CharacterNameAction(data, getCharacteristicsAdapter(), getRMSheetAdapter()));

        JMenuItem menuGenCharacteristics = new JMenuItem(RESOURCE.getString("ui.menu.generatecharacteristics"), UIConstants.ICON_GEN_CHARACTERISTICS);
        generatorMenu.add(menuGenCharacteristics);
        menuGenCharacteristics.addActionListener(new CharacterCharacteristicsAction(data, getCharacteristicsAdapter(), getRMSheetAdapter()));
        Bindings.bind(menuGenCharacteristics, "enabled", enableValueHolder);

        JMenuItem menuGenNPC = new JMenuItem(RESOURCE.getString("ui.menu.generatecharacter"), UIConstants.ICON_GEN_ALL);
        generatorMenu.add(menuGenNPC);
        menuGenNPC.addActionListener(new CharacterGeneratorAction(this, data, getRMSheetAdapter(), getCharacteristicsAdapter(), longRunAdapter));
        Bindings.bind(menuGenNPC, "enabled", enableValueHolder);

        /* ---- About ---- */
        JMenu helpMenu = new JMenu(RESOURCE.getString("ui.meni.help"));
        bar.add(helpMenu);

        JMenuItem showCustomConfigErrors = new JMenuItem(RESOURCE.getString("ui.menu.errors"), UIConstants.ICON_IMPORTANT);
        helpMenu.add(showCustomConfigErrors);
        showCustomConfigErrors.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                for (String err : RMPreferences.getInstance().getErrors()) {
                    sb.append(err).append("\n");
                }
                RMOffice.showError("ui.menu.errors", sb.toString());
            }
        });


        JMenuItem aboutMenu = new JMenuItem(RESOURCE.getString("ui.menu.about"), UIConstants.ICON_HELP);
        helpMenu.add(aboutMenu);
        aboutMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String about = RMOffice.getProgramString() + "\n\n" +
                        RESOURCE.getString("ui.about.configfilepath") + "\n" +
                        RMPreferences.getPropertiesFilePath() + "\n\nJava-Home: " + System.getProperty("java.home");
                JideOptionPane.showMessageDialog(RMFrame.this, about, RESOURCE.getString("ui.menu.about"),
                        JideOptionPane.INFORMATION_MESSAGE, UIConstants.RMO_LOGO125);
            }

        });

        /* New Version Menu*/
        newLatestVersionMenu = new JMenu(RESOURCE.getString("ui.menu.newversion"));
        bar.add(newLatestVersionMenu);
        newLatestVersionMenu.setVisible(false);

        JMenuItem getNewVersionMenu = new JMenuItem(RESOURCE.getString("ui.menu.getnewversion"), UIConstants.ICON_ARRDOWN);
        newLatestVersionMenu.add(getNewVersionMenu);
        getNewVersionMenu.addActionListener(new DesktopAction(URI.create("http://sourceforge.net/projects/rmoffice/")));

        setJMenuBar(bar);
    }

    /*
     * We have to fill the complete UI otherwise old values would be visible after loading an sheet.
     **/
    private void sheet2ui() {
        /* refresh skill table */
        skillModel.updateTable();
        skillcatModel.updateTable();
        /* weapons skillgroup costs */
        for (SkillCategory gr : modifiableSkillgroups) {
            skillcostFields.get(gr).setText(sheet.getSkillcost(gr).toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshing = true;
        try {
            if (RMSheet.PROPERTY_SKILL_CATEGORIES.equals(evt.getPropertyName())) {
                /* skill selection tree */
                skillSelectionRootNode.removeAllChildren();
                createSkillSelectionData(skillSelectionRootNode);
                DefaultTreeModel model = (DefaultTreeModel) skillsSelectionTree.getModel();
                model.setRoot(skillSelectionRootNode);
            } else if (RMSheet.PROPERTY_SHEET_ENABLE_ALL.equals(evt.getPropertyName())) {
                boolean enabled = ((Boolean) evt.getNewValue()).booleanValue();
                setAllEnabled(enabled);
            } else if (RMSheet.PROPERTY_SKILL_STRUCTURE_CHANGED.equals(evt.getPropertyName())) {
                skillModel.updateTable();
                for (SkillCategory sg : modifiableSkillgroups) {
                    skillcostFields.get(sg).setText(sheet.getSkillcost(sg).toString());
                }
            } else if (RMSheet.PROPERTY_SKILLCATEGORY_CHANGED.equals(evt.getPropertyName())) {
                skillcatModel.updateTable();
            } else if (RMSheet.PROPERTY_SKILLS_CHANGED.equals(evt.getPropertyName())) {
                skillModel.skillValuesChanged();
            } else if (RMSheet.PROPERTY_STATE.equals(evt.getPropertyName())) {
                enableMenuExportPDFValueHolder.setValue(State.NORMAL.equals(evt.getNewValue()));
            }
        } finally {
            refreshing = false;
        }
    }

    private void setAllEnabled(boolean enabled) {
        enableValueHolder.setValue(enabled);
        if (enabled) {
            refreshing = true;
            sheet2ui();
            refreshing = false;
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }


    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        enableMenuSaveValueHolder.setValue(currentFile != null);
    }

    /**
     * Bonus format
     *
     * @param number the number
     * @return a formatted string
     */
    public static String format(int number) {
        if (number > 0) {
            return "+" + number;
        } else if (number == 0) {
            return "0";
        }
        return "" + number;
    }

    /**
     *
     */
    private void modifyExistingISkill() {
        if (skillsTable.getSelectedRow() < 0) {
            return;
        }

        int actualRow = skillsTable.convertRowIndexToModel(skillsTable.getSelectedRow());
        ISkill skill = skillModel.getSkillAtRow(actualRow);
        if (skill != null) {
            ModifySkillDialog dialog = new ModifySkillDialog(skill, sheet.getSkillTypeCustom(skill));
            JOptionPane pane = new JOptionPane(dialog, JideOptionPane.QUESTION_MESSAGE, JideOptionPane.OK_CANCEL_OPTION);
            JDialog d = pane.createDialog(RMFrame.this, RESOURCE.getString("ui.skills.btEdit.title"));
            pane.selectInitialValue();
            d.setVisible(true);
            d.dispose();
            Object result = pane.getValue();
            if (result != null && result instanceof Integer && ((Integer) result).intValue() == JOptionPane.OK_OPTION) {
                sheet.modifySkill(skill, dialog.getSkillName(), dialog.getSkillType());
            }
        }
    }

    /**
     *
     */
    private void addCurrentSelectedISkill(boolean withModifiedName) {
        TreePath selectionPath = skillsSelectionTree.getSelectionPath();
        if (selectionPath != null) {
            Object lastPathComponent = selectionPath.getLastPathComponent();
            if (lastPathComponent != null && lastPathComponent instanceof SkillTreeNode) {
                SkillTreeNode node = (SkillTreeNode) lastPathComponent;
                if (node.getUserObject() instanceof ISkill) {
                    ISkill skill = (ISkill) node.getUserObject();
                    if (withModifiedName) {
                        ModifySkillDialog dialog = new ModifySkillDialog(skill, null);
                        JOptionPane pane = new JOptionPane(dialog, JideOptionPane.QUESTION_MESSAGE, JideOptionPane.OK_CANCEL_OPTION);
                        JDialog d = pane.createDialog(RMFrame.this, RESOURCE.getString("ui.skills.btEditBeforeAdd.title"));
                        pane.selectInitialValue();
                        d.setVisible(true);
                        d.dispose();
                        Object result = pane.getValue();
                        if (result != null && result instanceof Integer && ((Integer) result).intValue() == JOptionPane.OK_OPTION) {
                            skillModel.addSkill(skill, dialog.getSkillName(), dialog.getSkillType());
                        }
                    } else if (!sheet.hasSkillRank(skill)) {
                        skillModel.addSkill(skill);
                    }
                }
            }
        }
    }

    private void initPropertyChangeListener() {
        sheet.addPropertyChangeListener(RMFrame.this);
        if (sheet.getMagicalitems() != null) {
            for (MagicalItem mi : sheet.getMagicalitems()) {
                mi.init(getRMSheetAdapter());
                for (MagicalFeature mf : mi.getFeatures()) {
                    mf.init(getRMSheetAdapter());
                }
            }
        }
    }

    private void removePropertyChangeListener() {
        sheet.removePropertyChangeListener(RMFrame.this);
    }

    private BeanAdapter<Characteristics> getCharacteristicsAdapter() {
        if (characteristicsAdapter == null) {
            characteristicsAdapter = new BeanAdapter<Characteristics>(sheet.getCharacteristics(), true);
        }

        return characteristicsAdapter;
    }

    private BeanAdapter<Coins> getCoinsAdapter() {
        if (coins == null) {
            coins = new BeanAdapter<Coins>(sheet.getCoins(), true);
        }
        return coins;
    }

    private BeanAdapter<RMSheet> getRMSheetAdapter() {
        if (rmsheetAdapter == null) {
            rmsheetAdapter = new BeanAdapter<RMSheet>(sheet, true);
        }
        return rmsheetAdapter;
    }

    private BeanAdapter<StatValueModelAdapter> getStatValueModelAdapter() {
        if (statValueModelAdapter == null) {
            statValueModelAdapter = new BeanAdapter<StatValueModelAdapter>(new StatValueModelAdapter(getRMSheetAdapter()), true);
        }
        return statValueModelAdapter;
    }

    private void updateAdapters() {
        getRMSheetAdapter().setBean(sheet);
        getCharacteristicsAdapter().setBean(sheet.getCharacteristics());
        getCoinsAdapter().setBean(sheet.getCoins());
    }

    /**
     * @param latestVersion
     */
    public void newLatestVersion(String latestVersion) {
        String menuLabel = MessageFormat.format(RESOURCE.getString("ui.menu.newversion"), latestVersion);
        newLatestVersionMenu.setText(menuLabel);
        newLatestVersionMenu.setVisible(true);
        newLatestVersionMenu.getParent().repaint();
    }
}