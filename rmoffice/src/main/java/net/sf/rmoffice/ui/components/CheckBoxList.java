package net.sf.rmoffice.ui.components;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

/**
 * Checkbox list component that displays a list of selectable objects.
 */
public class CheckBoxList<T> extends JList<CheckboxItem<T>> {
	private static final long serialVersionUID = 1L;
	public static final String ITEMS_CHECKED_PROP = "itemsChecked";
	private DefaultListModel<CheckboxItem<T>> choiceListModel = new DefaultListModel<CheckboxItem<T>>();
	private List<T> selection =new ArrayList<T>();
	private int itemsChecked = -1;
	
	/**
	 * Constructs a new checkbox list component.
	 */
	public CheckBoxList() {
		setModel(choiceListModel);
		setCellRenderer(new CellRenderer());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());
				if (index != -1) {
					CheckboxItem<T> cbItem = getModel().getElementAt(index);
					cbItem.setSelected(!cbItem.isSelected());
					if (cbItem.isSelected()) {
						/* add object to selection */
						selection.add(cbItem.getObject());
					} else {
						/* remove object from selection */
						selection.remove(cbItem.getObject());
					}
					setItemsChecked(selection.size());
					repaint();
				}
			}
		});
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	/**
	 * Returns a list of checked items.
	 * 
	 * @return list of checked items
	 */
	public List<T> getCheckedItems() {
		List<T> result = new ArrayList<T>();
		result.addAll(selection);
		return result;
	}
	
	public int getItemsChecked() {
		return itemsChecked;
	}

	public void setItemsChecked(int itemsChecked) {
		int oldValue = this.itemsChecked;
		this.itemsChecked = itemsChecked;
		firePropertyChange(ITEMS_CHECKED_PROP, oldValue, this.itemsChecked);
	}


	public void setSelectableValues(T[] choiceables) {
		for (T choiceable : choiceables) {
			choiceListModel.addElement(new CheckboxItem<T>(choiceable));	
		}
		selection.clear();
		setItemsChecked(0);
	}

	private static class CellRenderer implements ListCellRenderer<JCheckBox> {

		@Override
		public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = value;

			if (!isSelected) {
				checkbox.setBackground(UIManager.getColor("List.background"));
			}
			return checkbox;
		}
	}
}