package canvas;

import javax.swing.table.DefaultTableModel;

public class WhiteBoardTableModel extends DefaultTableModel {

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
