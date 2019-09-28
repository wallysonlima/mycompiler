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
    ArrayList<Analyse> tokens;
    ArrayList<SintaticError> list;
    int count;
    
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
        // Do the Lexic Analyse
        tokens = analyseLexic(textEdit);
        list = new ArrayList<>();
        int count = 0;
        
        if ( tokens.isEmpty() ) {
            list.add( new SintaticError( "0", "Erro ! Não possui tokens para ser feita a análise sintática !") );
            return list;
        }
        
        // Control a sintatic analyse
        while( count < tokens.size() ) {
            if ( tokens.get(count).getLine().equals("1") ) 
                if ( accept("Palavra_Reservada_Program") ) {
                    if ( expect("Identificador") ) {
                        if ( !expect("Ponto_Virgula") ) 
                            list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! O próximo token precisa ser ';'") );
                
                    } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! O próximo token precisa ser um 'Identificador (int, boolean)'") );
                
                } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! O programa precisa inicializar com a palavra reservada 'program' ") );
            
            nextToken();
            block();
        }

        if ( list.size() == 0 )
            list.add( new SintaticError( "-1", "Sucesso ! A análise sintática obteve sucesso !" ) );
        
        return list;
    }
    
    // ######################### --- Help Methods to do sintatic analyse --- ###################################

    // Compare specific Token
    public boolean accept(String token) {
        if ( tokens.get(count).getToken().equalsIgnoreCase(token) )
            return true;   
        
        return false;
    }
    
    // Compare the next token
    public boolean expect(String token) {
        nextToken();
        
        if ( accept(token) )
            return true;
        
        return false;
    }
    
    // Increment to the next token
    public void nextToken() {
        if ( count < tokens.size() )
            ++count;
    }
    
    // Decrement previous token
    public int previousToken() {
        if ( count > 0 )
            --count;
        
        return count;
    }
    
    public void block() {
        if ( accept("Palavra_Reservada_Int") || accept("Palavra_Reservada_Boolean") )
            partVarDeclaration();
        
        if ( expect("Palavra_Reservada_Procedure") )
            procedurePart();
        
        if ( accept("Palavra_Reservada_Begin") )
            compCondition();
        else
            list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado a palavra reservada 'begin' !") );
    }
    
    public void partVarDeclaration() {
        varDeclaration();
        
        while ( accept("Ponto_Virgula") ) {
            nextToken();
            varDeclaration();
        }
        
        previousToken();
        
        if ( !accept("Ponto_Virgula") )
            list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") );   
    }
    
    public void varDeclaration() {
        if ( accept("Palavra_Reservada_Int") || accept("Palavra_Reservada_Boolean") ) {
            nextToken();
            identList();
        } //else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado uma palavra reservada 'int/boolean' !") );   
    }
    
    public void identList() {
        if ( accept("Identificador") ) {
            nextToken();
            
            while( accept("Virgula") ) {
                nextToken();
                identList();
            }
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado um 'Identificador' !") );   
    }
    
    public void procedurePart() {
        boolean enter = false;
        
        while( accept("Palavra_Reservada_Procedure") ) {
            procedureDeclaration();
            nextToken();
            enter = true;
        }
        
        if ( enter )
            if ( !expect("Ponto_Virgula") )
                list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") );   
    }
    
    public void procedureDeclaration() {
        if ( accept("Palavra_Reservada_Procedure") ) {
            if ( expect("Identificador") ) {
                if ( expect("Abre_Parenteses") ) {
                    formalParam();
                    
                    if ( expect("Ponto_Virgula") ) {
                        nextToken();
                        block();
                    } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") ); 
                }
                
                if ( expect("Ponto_Virgula") ) {
                    nextToken();
                    block();
                } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") ); 
            }
        }
    }
    
    public void formalParam() {
        if ( accept("Abre_Parenteses") ) {
            nextToken();
            formalSectionParam();
            
            while( expect("Ponto_Virgula") ) {
                nextToken();
                formalSectionParam();
            }
            
            if ( !accept("Fecha_Parenteses") )
               list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ')' !") ); 
        
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo '(' !") );  
    }
    
    public void formalSectionParam() {
        if ( accept("Palavra_Reservada_Var") ) {
            nextToken();
            identList();
            
            if ( accept("Operador_Dois_Pontos")) {
                nextToken();
                
                if ( !accept("Palavra_Reservada_Int") && !accept("Palavra_Reservada_Boolean")  )
                    list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado 'Identificador' !") );
            
            } else  list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ':' !") );
        } else {
            identList();
            
            if ( accept("Operador_Dois_Pontos")) {
                nextToken();
                
                if ( !accept("Identificador") )
                    list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado 'Identificador' !") );
            
            } else  list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ':' !") );
        }
    }
    
    public void compCondition() {
        if ( accept("Palavra_Reservada_Begin") ) {
            nextToken();
            condition();
            
            while ( expect("Ponto_Virgula") ) {
                nextToken();
                condition();
                
                if ( !expect("Palavra_Reservada_End") )
                    list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'end' !") );
            }
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'begin' !") );
    }
    
    // Missing  Chamada de Procedimento ???????
    public void condition() {
        if ( accept("Identificador") ) {
           attribution();
        
        } else if ( accept("Palavra_Reservada_Begin") ) {
           compCondition();
        
        } else if ( accept("Palavra_Reservada_If") ) {
            conditionIf();
        
        } else if ( accept("Palavra_Reservada_While") ) {
            conditionLoop();
        
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado algum 'comando' !") );
    }
    
    public void attribution() {
        variable();
        
        if ( expect("Operador_Igual") ) {
            nextToken();
            expression();
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado o operador ':=' !") );
    }
    
    public void procedureCall() {
        if ( accept("Identificador") ) {
            if ( expect("Abre_Parenteses") ) {
                nextToken();
                listExpression();
                
                if ( !expect("Fecha_Parenteses") )
                    list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado ')' !") );
                
            } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado '(' !") );
            
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado um 'Identificador' !") );
    }
    
    // Parse the conditionIF
    public void conditionIf() {
        if ( accept("Palavra_Reservada_If") ) {
            nextToken();
            expression();
            
            if ( expect("Palavra_Reservada_Then") ) {
                nextToken();
                condition();
                
                if ( expect("Palavra_Reservada_Else") ) {
                    nextToken();
                    condition();
                } 
                
            } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'then' !") );
            
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'if' !") );
    }
    
    // Parse the condition
    public void conditionLoop() {
        if ( accept("Palavra_Reservada_While") ) {
            nextToken();
            expression();
            
            if ( expect("Palavra_Reservada_Do") ) {
                nextToken();
                condition();
            } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'do' !") );
            
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'while' !") );
    }
    
    // Parse the expression
    public void expression() {
        simpleExpression();
        
        if ( expect("Operador_Menor") || expect("Operador_Igual") || expect("Operador_Maior") || expect("Operador_Menor_Igual") ||
            expect("Operador_Maior_Igual") || expect("Operador_Diferente") ) {
            nextToken();
            
            simpleExpression();
        }
    }
    
    // Parse the Simple Expression
    public void simpleExpression() {
        if ( accept("Operador_Soma") || accept("Operador_Subtração") ) {
            nextToken();
        }
        
        term();
        
        while( expect("Operador_Soma") || expect("Operador_Subtração") || expect("Palavra_Reservada_Or") ) {
            nextToken();
            term();
        }
    }
    
    // Parse the term
    public void term() {
        factor();
        
        while ( expect("Operador_Multiplicação") || expect("Palavra_Reservada_Div") || expect("Palavra_Reservada_And") ) {
            nextToken();
            factor();
        }
    }
    
    // Parse the factor
    public void factor() {
        if ( accept("Identificador") ) {
            nextToken();
            variable();
        } else if ( accept("Inteiro") ) {
            nextToken();
        } else if ( accept("Abre_Parenteses") ) {
            nextToken();
            expression();
            
            if ( !expect("Fecha_Parenteses") )
                list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado ')' !") );
            
        } else if ( accept("Palavra_Reservada_Not") ) {
            nextToken();
            factor();
            
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado um fator !") ); 
        
    }
    
    // Parse the variable
    public void variable() {
        if ( accept("Identificador") ) {
            if ( expect("Operador_Soma") || expect("Operador_Subtração") )
                expression();
        } else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado 'Identificador' !") );
    }
    
    // Parse list of expression
    public void listExpression() {
        expression();
        
        while ( expect("Virgula") ) {
            nextToken();
            expression();
        }
    }
}
    