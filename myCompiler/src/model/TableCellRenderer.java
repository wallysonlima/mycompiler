/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author wallysonlima
 */
public class TableCellRenderer extends DefaultTableCellRenderer {
    private ArrayList<Integer> wrong;

    public TableCellRenderer(ArrayList<Integer> wrong)
    {
        this.wrong = wrong;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
    {
        // Cells are by default rendered as a JLabel
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        
        if ( wrong.get(row) == 1 )
            l.setBackground(Color.RED);
        else if ( row % 2 == 0)
            l.setBackground(Color.LIGHT_GRAY);
        else
            l.setBackground(Color.white);
        
        // return the JLabel which renders the cell
        return l;
    }
}
