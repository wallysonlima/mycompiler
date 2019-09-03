/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Control;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import model.Analyse;
import model.TableCellRenderer;

/**
 *
 * @author wallysonlima
 */
public class Main extends javax.swing.JFrame {
    private Control control;
    private ArrayList<Analyse> listAnalyse;
    File file;
    HashSet<String> hashWord;
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        control = new Control();
        listAnalyse = new ArrayList<>();
        initializeReservedWord();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedLexical = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableLexical = new javax.swing.JTable();
        tabbedPaneEditor = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPaneAreaEdit = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        textAreaLines = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemOpen = new javax.swing.JMenuItem();
        menuItemSave = new javax.swing.JMenuItem();
        menuItemClose = new javax.swing.JMenuItem();
        menuAnalyse = new javax.swing.JMenu();
        menuItemLexic = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("myCompiler");
        setResizable(false);
        setSize(new java.awt.Dimension(1024, 768));

        tableLexical.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lexeme", "Token", "Line", "IniCol", "EndCol", "Error"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableLexical);

        tabbedLexical.addTab("Lexical Table", jScrollPane1);

        jScrollPane2.setViewportView(textPaneAreaEdit);

        tabbedPaneEditor.addTab("Editor", jScrollPane2);

        textAreaLines.setEditable(false);
        textAreaLines.setColumns(20);
        textAreaLines.setRows(5);
        jScrollPane6.setViewportView(textAreaLines);

        menuFile.setText("File");

        menuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuItemOpen.setText("Open");
        menuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpenActionPerformed(evt);
            }
        });
        menuFile.add(menuItemOpen);

        menuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSave.setText("Save");
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuItemSave);

        menuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        menuItemClose.setText("Close");
        menuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCloseActionPerformed(evt);
            }
        });
        menuFile.add(menuItemClose);

        jMenuBar1.add(menuFile);

        menuAnalyse.setText("Analyse");

        menuItemLexic.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuItemLexic.setText("Lexic");
        menuItemLexic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuItemLexicMouseClicked(evt);
            }
        });
        menuItemLexic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLexicActionPerformed(evt);
            }
        });
        menuAnalyse.add(menuItemLexic);

        jMenuBar1.add(menuAnalyse);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPaneEditor))
            .addComponent(tabbedLexical, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPaneEditor)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 30, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedLexical, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenActionPerformed
        String line = "";
        String textEdit = "";
        BufferedReader in = null;
        int counter = 0;
        
        // Handle open button action
        final JFileChooser fc = new JFileChooser();

        int returnVal = fc.showOpenDialog(this);

        if ( returnVal == JFileChooser.APPROVE_OPTION)
        {
            file = fc.getSelectedFile();

            try
            {
                in = new BufferedReader(new FileReader(file));
                
                while ( (line = in.readLine()) != null )
                {
                    counter++;
                    textEdit += line + "\n";
                }

                // This is where a real application would open the file
                System.out.println("Opening: " + file.getName() + "\n");
                System.out.println("\n\n" + line);
            }

            catch (IOException ex)
            {
                System.err.println("Error ! Isn't possible open the file ! " + ex);
            }

            finally
            {
                try {
                    in.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                textPaneAreaEdit.setText(textEdit);
                setColorReservedWords();
                textAreaLines.setText( populateLines(counter) );
            }
        }

        else
        {
            JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
        }
    }//GEN-LAST:event_menuItemOpenActionPerformed

    private void menuItemLexicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLexicActionPerformed
        // Do Analyse Lexic
        //JOptionPane.showMessageDialog(null, "Entrou aqui !");
        
        if ( textPaneAreaEdit.getText() != "" )
            listAnalyse = control.analyseLexic(textPaneAreaEdit.getText());
        else
            JOptionPane.showMessageDialog(null, "Error ! Without Text ! You need fill the textArea !");
        
        // Popula Table
        populateLexicalTable(listAnalyse);
        
        if ( listAnalyse.get( listAnalyse.size() - 1).getLexeme().equals("Error") )
            JOptionPane.showMessageDialog(null, "Error ! You missed close the comment !");

    }//GEN-LAST:event_menuItemLexicActionPerformed

    private void menuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCloseActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "Do you really want close the application ?");
        
        if ( input == 0 )
            this.dispose();
    }//GEN-LAST:event_menuItemCloseActionPerformed

    private void menuItemLexicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuItemLexicMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItemLexicMouseClicked

    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        
        int retval = fileChooser.showSaveDialog(this);
        
        if ( retval == JFileChooser.APPROVE_OPTION )
        {
            File file = fileChooser.getSelectedFile();
            
            if ( file == null )
                return;
            
            if ( !file.getName().toLowerCase().endsWith(".txt") )
                file = new File(file.getParentFile(), file.getName() + ".txt");
            
            try
            {
                textPaneAreaEdit.write(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                Desktop.getDesktop().open(file);
            }
            
             catch (Exception e)
             {
                 e.printStackTrace();
             }
        }
    }//GEN-LAST:event_menuItemSaveActionPerformed

    public void populateLexicalTable(ArrayList<Analyse> list)
    {
        DefaultTableModel model = (DefaultTableModel) tableLexical.getModel();
        ArrayList<Integer> wrong = new ArrayList<>(); 
        
        Object rowData[] = new Object[6];
        
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        
        for( int i = 0; i < list.size(); i++ )
        {   
            if ( list.get(i).getToken().equalsIgnoreCase("Caractere_Invalido") || !list.get(i).getError().equalsIgnoreCase("") )
                wrong.add(1); 
            else
                wrong.add(0);
        
            rowData[0] = list.get(i).getLexeme();
            rowData[1] = list.get(i).getToken();
            rowData[2] = list.get(i).getLine();
            rowData[3] = list.get(i).getIniCol();
            rowData[4] = list.get(i).getEndCol();
            rowData[5] = list.get(i).getError();
            
            model.addRow(rowData);
        }
        
        tableLexical.setDefaultRenderer(Object.class, new TableCellRenderer(wrong));
        tableLexical.setModel(model);
    }
    
    public String populateLines(int QtdLines)
    {
        String lines = "";
        
        for(int i = 1; i <= QtdLines; i++)
            lines += i + "\n";
        
        return lines;
    }
    
    public void initializeReservedWord()
    {
        hashWord = new HashSet<String>();
        
        hashWord.add("program");
        hashWord.add("procedure");
        hashWord.add("var");
        hashWord.add("int");
        hashWord.add("boolean");
        hashWord.add("read");
        hashWord.add("write");
        hashWord.add("true");
        hashWord.add("false");
        hashWord.add("begin");
        hashWord.add("end");
        hashWord.add("if");
        hashWord.add("while");
        hashWord.add("do");
        hashWord.add("else");
        hashWord.add("then");
        hashWord.add("div");
        hashWord.add("and");
        hashWord.add("or");
        hashWord.add("not");
    }
    
    public void setColorReservedWords()
    {   
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setBold(set, true);
        StyleConstants.setItalic(set, true);
        StyleConstants.setForeground(set, Color.CYAN);
        Document doc = textPaneAreaEdit.getStyledDocument();
        
        try {
            if ( textPaneAreaEdit.getText().contains("program") )
                doc.insertString(doc.getLength(), "program", set);
            if ( textPaneAreaEdit.getText().contains("procedure") )
                doc.insertString(doc.getLength(), "procedure", set);
            if ( textPaneAreaEdit.getText().contains("var") )
                doc.insertString(doc.getLength(), "var", set);
            if ( textPaneAreaEdit.getText().contains("int") )
                doc.insertString(doc.getLength(), "int", set);
            if ( textPaneAreaEdit.getText().contains("boolean") )
                doc.insertString(doc.getLength(), "boolean", set);
            if ( textPaneAreaEdit.getText().contains("read") )
                doc.insertString(doc.getLength(), "read", set);
            if ( textPaneAreaEdit.getText().contains("write") )
                doc.insertString(doc.getLength(), "write", set);
            if ( textPaneAreaEdit.getText().contains("true") )
                doc.insertString(doc.getLength(), "true", set);
            if ( textPaneAreaEdit.getText().contains("false") )
                doc.insertString(doc.getLength(), "false", set);
            if ( textPaneAreaEdit.getText().contains("begin") )
                doc.insertString(doc.getLength(), "begin", set);
            if ( textPaneAreaEdit.getText().contains("end") )
                doc.insertString(doc.getLength(), "end", set);
            if ( textPaneAreaEdit.getText().contains("if") )
                doc.insertString(doc.getLength(), "if", set);
            if ( textPaneAreaEdit.getText().contains("while") )
                doc.insertString(doc.getLength(), "while", set);
            if ( textPaneAreaEdit.getText().contains("do") )
                doc.insertString(doc.getLength(), "do", set);
            if ( textPaneAreaEdit.getText().contains("else") )
                doc.insertString(doc.getLength(), "else", set);
            if ( textPaneAreaEdit.getText().contains("then") )
                doc.insertString(doc.getLength(), "then", set);
            if ( textPaneAreaEdit.getText().contains("div") )
                doc.insertString(doc.getLength(), "div", set);
            if ( textPaneAreaEdit.getText().contains("and") )
                doc.insertString(doc.getLength(), "and", set);
            if ( textPaneAreaEdit.getText().contains("or") )
                doc.insertString(doc.getLength(), "or", set);
            if ( textPaneAreaEdit.getText().contains("not") )
                doc.insertString(doc.getLength(), "not", set);
            
        } catch (BadLocationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JMenu menuAnalyse;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuItemClose;
    private javax.swing.JMenuItem menuItemLexic;
    private javax.swing.JMenuItem menuItemOpen;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JTabbedPane tabbedLexical;
    private javax.swing.JTabbedPane tabbedPaneEditor;
    private javax.swing.JTable tableLexical;
    private javax.swing.JTextArea textAreaLines;
    private javax.swing.JTextPane textPaneAreaEdit;
    // End of variables declaration//GEN-END:variables
}
