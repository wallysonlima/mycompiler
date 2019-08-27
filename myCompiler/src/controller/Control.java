/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Analyse;
import model.LexicalAnalyzer;

/**
 *
 * @author wallysonlima
 */
public class Control {
    
    public void Control() {}
    
    // Read Text in file
    public ArrayList<Analyse> analyseLexic(String textEdit)
    {
        int i = 0;
        ArrayList<Analyse> list = new ArrayList<>();
        Scanner scanner = new Scanner(textEdit);
        Analyse token = new Analyse();
        
        while ( scanner.hasNextLine() )
        {
            String line = (scanner.nextLine()).toString();
            LexicalAnalyzer lexic = new LexicalAnalyzer(new StringReader(line));
            
            try {
                while ( (token = lexic.yylex()) != null )
                {
                    token.setLine(String.valueOf(i));
                    i++;
                    list.add(token);
                }
                    
            } catch (IOException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return list;
    }
}
