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
import javax.swing.JOptionPane;
import model.Analyse;
import model.LexicalAnalyzer;
import model.SintaticError;

/**
 *
 * @author wallysonlima
 */
public class Control {
    int MAX = 15;
    
    public void Control() {}
    
    // Do the analyse lexic
    public ArrayList<Analyse> analyseLexic(String textEdit)
    {
        int i = 1;
        boolean ignore = false;
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
                    
                    if ( token.getToken().equals("Identificador") || token.getToken().equals("Inteiro") || token.getToken().equals("Real") )
                    {
                        int length = Integer.parseInt(token.getEndCol()) - Integer.parseInt(token.getIniCol());
                        
                        if ( length > MAX )
                            token.setError("Erro ! Excedeu o tamanho máximo (15) !\n");
                    }
                    
                    if ( token.getLexeme().equals("{") )
                        ignore = true;
                    else if ( token.getLexeme().equals("}") )
                    {
                        ignore = false;
                        continue;
                    }
                    
                    if (!ignore)
                        list.add(token);
                }
                
            i++;
                    
            } catch (IOException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        if ( ignore )
            JOptionPane.showMessageDialog(null, "Error ! You miss close the comment !");
       
        return list;
    }
    
    // Do the Analyse Sintatic
    public ArrayList<SintaticError> analyseSintatic(String textEdit) {
        ArrayList<Analyse> tokens = analyseLexic(textEdit);
        ArrayList<SintaticError> list = new ArrayList<>();
        int i = 0;
        
        if ( tokens.size() == 0 ) {
            list.add( new SintaticError( "0", "Erro ! Não possui tokens para ser feita a análise sintática !") );
            return list;
        }
        
        while( i < tokens.size() ) {
            if ( tokens.get(i).getLine().equals("0") ) 
                if ( tokens.get(i).getToken().equalsIgnoreCase("Palavra_Reservada_Program") ) {
                    nextToken(i, tokens);

                    if ( tokens.get(i).getToken().equalsIgnoreCase("Identificador") ) {
                        nextToken(i, tokens);

                        if ( !tokens.get(i).getToken().equalsIgnoreCase("Ponto_Virgula") ) 
                            list.add( new SintaticError( tokens.get(i).getLine(), "Erro ! O próximo token precisa ser ';'") );

                    }

                    else list.add( new SintaticError( tokens.get(i).getLine(), "Erro ! O próximo token precisa ser um 'Identificador (int, boolean)'") );
                }
            
            else list.add( new SintaticError( tokens.get(i).getLine(), "Erro ! O programa precisa inicializar com a palavra reservada 'program' ") );
        }

        if ( tokens.get(i).getToken().equalsIgnoreCase("Palavra_Reservada_Int") || tokens.get(i).getToken().equalsIgnoreCase("Palavra_Reservada_Boolean") ) {
            nextToken(i, tokens);

            //if ( tokens.get(i).getToken().equalsIgnoreCase(""))
        }
        
        else if ( tokens.get(i).getToken().equalsIgnoreCase("Palavra_Reservada_Procedure") ) {
             nextToken(i, tokens);
        }
        
        else if ( tokens.get(i).getToken().equalsIgnoreCase("Palavra_Reservada_Begin") ) {
            nextToken(i, tokens);
            
            if ( tokens.get(i).getToken().equalsIgnoreCase("Identificador") ) {
                nextToken(i, tokens);
                
                if ( tokens.get(i).getToken().equalsIgnoreCase("Operador_Soma") || tokens.get(i).getToken().equalsIgnoreCase("Operador_Subtração") ) {
                    nextToken(i, tokens);
                    
                    if ( tokens.get(i).getToken().equalsIgnoreCase("Identificador") ) {
                        nextToken(i, tokens);
                    }
                    
                }
            }
            
            else list.add( new SintaticError( tokens.get(i).getLine(), "Erro ! O próximo token precisa ser um 'Identificador' ! ") );
        }
        
        else list.add( new SintaticError( tokens.get(i).getLine(), "Erro ! O próximo token precisa ser um 'bloco' !") );

        if ( list.size() == 0 )
            list.add( new SintaticError( "-1", "Sucesso ! A análise sintática obteve sucesso !" ) );
        
        return list;
    }
    
    public void nextToken(int i, ArrayList<Analyse> tokens) {
        if ( i < tokens.size() )
            ++i;
    }
    
    public void previousToken(int i, ArrayList<Analyse> tokens) {
        if ( i > 0 )
            --i;
    }
    
    public void verificarFator(int i, ArrayList<Analyse> tokens, ArrayList<SintaticError> list) {
        if ( tokens.get(i).getToken().equalsIgnoreCase("Abre_Parenteses") ) {
            nextToken(i, tokens);
            
        }
        
        
    }
}
    