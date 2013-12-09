package canvas;

import javax.swing.table.DefaultTableModel;

public class WhiteBoardTableModel extends DefaultTableModel {

	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	public void removeAllRows() {
		while (this.getRowCount() > 0) {
			this.removeRow(0);
		}
	}
}
