package canvas;

import javax.swing.table.DefaultTableModel;

public class WhiteBoardTableModel extends DefaultTableModel {

    /**
     * Whether or not a cell in a table should be editable.
     */
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	/**
	 * Remove all rows from (empty) this table.
	 */
	public void removeAllRows() {
		while (this.getRowCount() > 0) {
			this.removeRow(0);
		}
	}
}
