/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ADMIN
 */
public class button implements TableCellEditor,TableCellRenderer,ActionListener{
    private JTable table;
    private JButton button;
    private Action action;
    private Object value;
    private int col;

    public button(JTable table, Action action, int col) {
        button = new JButton("Update");
        this.action = action;
        button.addActionListener(this);
        this.table = table;
        this.col = col;
        this.table.getColumnModel().getColumn(col).setCellEditor(this);
        this.table.getColumnModel().getColumn(col).setCellRenderer(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value=value;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
        
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
       
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        String ma = String.valueOf(table.getValueAt(row,0));
        ActionEvent evt = new ActionEvent(table,ActionEvent.ACTION_PERFORMED,ma);
        this.action.actionPerformed(evt);
    }

    
}
