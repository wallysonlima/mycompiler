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
            boolean closeComment = false;

            while ( (line = in.readLine()) != null )
            {
                char [] chLine = line.toCharArray();
                int max = chLine.length - 1;
                
                for( int i = 0; i < chLine.length; i++ )
                {
                    if ( chLine[i] == ' ' )
                        continue;
                    
                    if ( chLine[i] == '/' )
                        if ( i < max )
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
                        
                    }
                }
                        
            }
        }

        catch (IOException ex)
        {
            System.err.println("Error ! Doesn't possible open the file ! " + ex);
        }
    }
}
