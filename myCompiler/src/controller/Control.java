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
import java.util.ArrayList;
import model.Analyse;

/**
 *
 * @author wallysonlima
 */
public class Control {
    
    public void Control() {}
    
    // Read Text in file
    public ArrayList<Analyse> analyseLexic(File file)
    {
        int lineQtde;
        ArrayList<Analyse> list = new ArrayList<>();
        lineQtde = 0;
        boolean ignore = false;
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            String temp;
            boolean isFloat = false;
            
            while ( (line = in.readLine()) != null )
            {
                char [] chLine = line.toCharArray();
                int max = chLine.length - 1;
                int i = 0;
                
                while( i < chLine.length )
                {
                    temp = "";
                    
                    // Empty or Comment
                    if ( chLine[i] == ' ' || ignore )
                        continue;
                    
                    // Close Comment
                    if ( chLine[i] == '*' && i < max )
                        if ( chLine[++i] == '/' )
                            ignore = false;
                    
                    // Ignore -- Open Comment
                    if ( chLine[i] == '/' && i < max )
                        if ( chLine[++i] == '*' )
                        {
                            ignore = true;
                            continue;
                        }
                    
                        else if ( chLine[++i] == '/' )
                            break;
                    
                    switch( chLine[i] )
                    {
                        case '+':
                            list.add(new Analyse(String.valueOf(chLine[i]), "op_soma", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            break;
                          
                        case '-':
                            list.add(new Analyse(String.valueOf(chLine[i]), "op_subtração", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            break;
                            
                        case '*':
                            list.add(new Analyse(String.valueOf(chLine[i]), "op_multiplicação", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            break;
                            
                        case '/':
                            list.add(new Analyse(String.valueOf(chLine[i]), "op_divisao", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            break;
                            
                        case '(':
                            list.add(new Analyse(String.valueOf(chLine[i]), "AP", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            break;
                            
                        case ')':
                            list.add(new Analyse(String.valueOf(chLine[i]), "FP", "", Integer.toString(lineQtde), Integer.toString(i)) );
                        
                           
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                            while ( (chLine[i+1] >= 48 && chLine[i+1] <= 57) || chLine[i+1] == 46 )
                            {
                                temp += chLine[i];
                                temp += chLine[++i];
                                
                                if ( chLine[i+1] == 46 )
                                    isFloat = true;
                            }
                            
                            list.add(new Analyse(temp, isFloat ? "Inteiro" : "Real", "", Integer.toString(lineQtde), Integer.toString(i)) );
                            isFloat = false;
                            break;
                    }
                    
                    i++;
                }
                
                if ( ignore )
                    list.add(new Analyse("Error", "", "", "", "") );
            }
        }

        catch (IOException ex)
        {
            System.err.println("Error ! Isn't possible open the file ! " + ex);
        }
        
        return list;
    }
}
