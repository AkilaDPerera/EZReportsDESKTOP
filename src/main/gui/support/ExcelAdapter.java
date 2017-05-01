package main.gui.support;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class ExcelAdapter implements ActionListener{
	private String rowstring,value;
	private Clipboard system;
	private StringSelection stsel;
	private JTable table;
	private DefaultTableModel model;
	
	
	/**
	 * The Excel Adapter is constructed with a
	 * JTable on which it enables Copy-Paste and acts
	 * as a Clipboard listener.
	 */
	public ExcelAdapter(JTable myJTable){
		table = myJTable;
		model = (DefaultTableModel) table.getModel();
		
		KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
		KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
		KeyStroke delete = KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE);
		
		table.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
		table.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
		table.registerKeyboardAction(this, "Clear", delete, JComponent.WHEN_FOCUSED);
		
		system = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	
	/**
	 * Public Accessor methods for the Table on which this adapter acts.
	 */
	public JTable getJTable() {
		return table;
	}
	public DefaultTableModel getTableModel(){
		return model;
	}
	public void addEmptyRecord() {
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		Object[] array = new Object[columnCount];
		Arrays.fill(array, null);
		
		for (int column=0; column<columnCount; column++){
			if (table.getValueAt(rowCount-1, column)!=null){
				model.addRow(array);
				break;
			}
		}
	}
	public void removeSelected(){
		
		int[] rows = table.getSelectedRows();
		int[] columns = table.getSelectedColumns();
		
		table.getCellEditor().stopCellEditing();
		
		for (int r : rows){
			for (int c : columns){
				table.setValueAt(null, r, c);
			}
		}
	}
	
	
	/**
	 * This method is activated on the Keystrokes we are listening to
	 * in this implementation. Here it listens for Copy and Paste ActionCommands.
	 * Selections comprising non-adjacent cells result in invalid selection and
	 * then copy action cannot be performed.
	 * Paste is done by aligning the upper left corner of the selection with the
	 * 1st element in the current selection of the JTable.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		
		if (arg0.getActionCommand().compareTo("Copy") == 0){
			StringBuffer sbf = new StringBuffer();
			// Check to ensure we have selected only a contiguous block of
			// cells
			int numcols = table.getSelectedColumnCount();
			int numrows = table.getSelectedRowCount();
			int[] rowsselected = table.getSelectedRows();
			int[] colsselected = table.getSelectedColumns();
			if (rowsselected.length==0){return;}
			if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] && numrows==rowsselected.length) && (numcols-1==colsselected[colsselected.length-1]-colsselected[0] && numcols==colsselected.length))){
				JOptionPane.showMessageDialog(null, "Invalid Copy Selection", "Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (int i=0; i<numrows; i++){
				for (int j=0; j<numcols; j++){
					sbf.append(table.getValueAt(rowsselected[i],colsselected[j]));
					if (j<numcols-1) sbf.append("\t");
				}
				sbf.append("\n");
			}
			
			stsel  = new StringSelection(sbf.toString());
			system = Toolkit.getDefaultToolkit().getSystemClipboard();
			system.setContents(stsel,stsel);
		}
		
		if (arg0.getActionCommand().compareTo("Paste")==0){
			if (table.getSelectedRows().length==0){return;}
			
			int startRow=(table.getSelectedRows())[0];
			int startCol=(table.getSelectedColumns())[0];
			
			Boolean cont = true;
			
			try{
				String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
				StringTokenizer st1=new StringTokenizer(trstring,"\n");
				
				for(int i=0;st1.hasMoreTokens();i++){

					rowstring=st1.nextToken();
					StringTokenizer st2=new StringTokenizer(rowstring,"\t");
					
					for(int j=0;st2.hasMoreTokens();j++){
						
						value = (String) st2.nextToken();
						
						if (startRow+i< table.getRowCount()  && startCol+j< table.getColumnCount()){
							if (startCol+j==1){
								table.setValueAt(value,startRow+i,startCol+j);
							}else{
								try{
									if(startCol+j==0){
										table.setValueAt(Integer.valueOf(value),startRow+i,startCol+j);
									}
									else{
										table.setValueAt(Double.valueOf(value),startRow+i,startCol+j);
									}
								}catch (Exception e){
									JOptionPane.showMessageDialog(null, "Incompatible data type detected!", "Data copying...", JOptionPane.WARNING_MESSAGE);
									cont = false;
									break;
								}
							}
						}
					}
					//If data incompatible detected
					if (!cont){
						break;
					}
					this.addEmptyRecord();
				}
			}catch(Exception ex){
				
			}
		}
		
		if (arg0.getActionCommand().compareTo("Clear")==0){
			this.removeSelected();
		}
	}
	
}