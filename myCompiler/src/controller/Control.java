/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Analyse;
import model.Code;
import model.LexicalAnalyzer;
import model.Error;
import model.Stack;
import model.Symbol;

/**
 *
 * @author wallysonlima
 */
public class Control {
    int MAX = 15;
    ArrayList<Analyse> tokens;
    ArrayList<Error> list;
    int count;
    ArrayList<Symbol> globalList; 
    ArrayList<Symbol> localList;
    
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
    public ArrayList<Error> analyseSintatic(String textEdit) {
        // Do the Lexic Analyse
        tokens = analyseLexic(textEdit);
        list = new ArrayList<>();
        count = 0;
        
        if ( tokens.isEmpty() ) {
            list.add(new Error( "0", "Erro ! Não possui tokens para ser feita a análise sintática !") );
            return list;
        }
        
        // Control a sintatic analyse
        while( count < tokens.size() ) {
            if ( tokens.get(count).getLine().equals("1") ) 
                if ( accept("Palavra_Reservada_Program") ) {
                    if ( expect("Identificador") ) {
                        if ( !expect("Ponto_Virgula") ) 
                            list.add(new Error( tokens.get(count).getLine(), "Erro ! O próximo token precisa ser ';'") );
                
                    } else list.add(new Error( tokens.get(count).getLine(), "Erro ! O próximo token precisa ser um 'Identificador (int, boolean)'") );
                
                } else list.add(new Error( tokens.get(count).getLine(), "Erro ! O programa precisa inicializar com a palavra reservada 'program' ") );
            
            if ( !accept("Palavra_Reservada_Begin") )
                nextToken();
            
            if ( accept("Palavra_Reservada_End") || count == tokens.size() - 1 ) {
                count++;
                continue;
            }
            
            block();
        }

        if ( list.size() == 0 )
            list.add(new Error( "-1", "Sucesso ! A análise sintática obteve sucesso !" ) );
        
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
    
    // Compare if the previous token is accept
    public boolean acceptPreviousToken(String token) {
        --count;
        
        if ( accept(token) ) {
            ++count;
            return true;
        }
        
        ++count;
        return false;
    }
    
    // Increment to the next token
    public void nextToken() {
        int MAX = tokens.size() - 1;
        
        if ( count < MAX)
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
        
        if ( accept("Palavra_Reservada_Procedure") )
            procedurePart();
        
        if ( accept("Palavra_Reservada_Begin") ) {
            compCondition();
        }
            
        else {
            if ( !accept("Ponto_Virgula") ) {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado a palavra reservada 'begin'/'procedure'/'int'/'boolean' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' !") );

                while( !accept("PONTO_VIRGULA") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            }
        }
    }
    
    public void partVarDeclaration() {
        varDeclaration();
        
        while ( accept("Ponto_Virgula") ) {
            nextToken();
            varDeclaration();
        }
        
        previousToken();
        
        if ( !accept("Ponto_Virgula") ) {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") ); 
            nextToken();
        }
        
        nextToken();
    }
    
    public void varDeclaration() {
        if ( accept("Palavra_Reservada_Int") || accept("Palavra_Reservada_Boolean") ) {
            nextToken();
            identList();
        }   
    }
    
    public void identList() {
        if ( accept("Identificador") ) {
            nextToken();
            
            while( accept("Virgula") ) {
                nextToken();
                identList();
            }
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado um 'Identificador' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | ':' !") );
            
            while ( !accept("Ponto_Virgula") || !accept("DOIS_PONTOS") ) {
                nextToken();
             
                if ( count == tokens.size() - 1 )
                      break;
            }
        }   
    }
    
    public void procedurePart() {
        while( accept("Palavra_Reservada_Procedure") ) {
            procedureDeclaration();
            
            if ( !accept("Ponto_Virgula") && !accept("Palavra_Reservada_End") && !accept("Palavra_Reservada_Begin") ) {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") );
                nextToken();
            }
        }
    }
    
    public void procedureDeclaration() {
        if ( accept("Palavra_Reservada_Procedure") ) {
            if ( expect("Identificador") ) {
                if ( expect("Abre_Parenteses") ) {
                    formalParam();
                    
                    if ( expect("Ponto_Virgula") ) {
                        nextToken();
                        block();
                    } else {
                        list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") );
                        nextToken();
                    } 
                }
                
                if ( expect("Ponto_Virgula") ) {
                    nextToken();
                    block();
                } else {
                    list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ';' !") );
                    nextToken();
                } 
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
            
            if ( !accept("Fecha_Parenteses") ) {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ')' !") ); 
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                while ( !accept("Ponto_Virgula") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            } 
        }
    }
    
    public void formalSectionParam() {
        if ( accept("Palavra_Reservada_Var") ) {
            nextToken();
            identList();
            
            if ( accept("Operador_Dois_Pontos")) {
                nextToken();
                
                if ( !accept("Palavra_Reservada_Int") && !accept("Palavra_Reservada_Boolean")  ) {
                    list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado 'Identificador' !") );
                    list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                    while ( !accept("Ponto_Virgula") ) {
                        nextToken();
                        
                        if ( count == tokens.size() - 1 )
                            break;
                    }
                }
                
            } else {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ':' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' !") );
                
                while ( !accept("Ponto_Virgula") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            }                    
            
        } else {
            identList();
            
            if ( accept("Operador_Dois_Pontos")) {
                nextToken();
                
                if ( !accept("Identificador") ) {
                    list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado 'Identificador' !") );
                    list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                    while ( !accept("Ponto_Virgula") ) {
                        nextToken();
                        
                        if ( count == tokens.size() - 1 )
                            break;
                    }
                }
            
            } else {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o símbolo ':' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                    while ( !accept("Ponto_Virgula") ) {
                        nextToken();
                        
                        if ( count == tokens.size() - 1 )
                            break;
                    }
            }
        }
    }
    
    public void compCondition() {
        if ( accept("Palavra_Reservada_Begin") ) {
            nextToken();
            condition();
            
            while ( accept("Ponto_Virgula") ) {
                nextToken();
                condition();
                
                if ( !accept("Palavra_Reservada_End") && !accept("Ponto_Virgula") && !accept("Palavra_Reservada_Begin")) {
                    list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'end' !") );
                    list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                    while ( !accept("Ponto_Virgula") ) {
                        nextToken();
                        
                        if ( count == tokens.size() - 1 )
                            break;
                    }
                }
            }
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'begin' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
            while ( !accept("Ponto_Virgula") ) {
                nextToken();

                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    // Missing  Chamada de Procedimento ???????
    public void condition() {
        if ( accept("Identificador") || accept("Palavra_Reservada_Write") || accept("Palavra_Reservada_Read") ) {
           attribution();
        
        } else if ( accept("Palavra_Reservada_Begin") ) {
           compCondition();
        
        } else if ( accept("Palavra_Reservada_If") ) {
            conditionIf();
        
        } else if ( accept("Palavra_Reservada_While") ) {
            conditionLoop();
        
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado algum 'comando' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    public void attribution() {
        variable();
        
        if ( accept("Operador_Igual") ) {
            nextToken();
            expression();
        
        } else if ( accept("Abre_Parenteses") && (acceptPreviousToken("Palavra_Reservada_Write") || acceptPreviousToken("Palavra_Reservada_Read")) ) {
            nextToken();
            expression();
        }
        
        else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado o operador ':=' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    public void procedureCall() {
        if ( accept("Identificador") ) {
            if ( expect("Abre_Parenteses") ) {
                nextToken();
                listExpression();
                
                if ( !expect("Fecha_Parenteses") ) {
                    list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado ')' !") );
                    list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
                    while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                        nextToken();
                        
                        if ( count == tokens.size() - 1 )
                            break;
                    }
                }
                
            } else{
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado '(' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
                while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            }
            
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado um 'Identificador' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    // Parse the conditionIF
    public void conditionIf() {
        if ( accept("Palavra_Reservada_If") ) {
            nextToken();
            expression();
            
            if ( expect("Palavra_Reservada_Then") ) {
                nextToken();
                condition();
                
                if ( accept("Palavra_Reservada_Else") ) {
                    nextToken();
                    condition();
                } 
                
            } else if ( acceptPreviousToken("Palavra_Reservada_Then") ) {
                condition();
                
                if ( accept("Palavra_Reservada_Else") ) {
                    nextToken();
                    condition();
                } 
            } else {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'then' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
                while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            }
            
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'if' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    // Parse the condition
    public void conditionLoop() {
        if ( accept("Palavra_Reservada_While") ) {
            nextToken();
            expression();
            
            if ( accept("Palavra_Reservada_Do") ) {
                nextToken();
                condition();
            } //else list.add( new SintaticError( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'do' !") );
            
            if ( accept("Fecha_Parenteses") )
                nextToken();
            
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado palavra reservada 'while' !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';' | 'end' | 'else'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        }
    }
    
    // Parse the expression
    public void expression() {
        simpleExpression();
        
        if ( accept("Operador_Menor") || accept("Operador_Igual") || accept("Operador_Maior") || accept("Operador_Menor_Igual") ||
            accept("Operador_Maior_Igual") || accept("Operador_Diferente") ) {
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
        
        while( accept("Operador_Soma") || accept("Operador_Subtração") || accept("Palavra_Reservada_Or") || accept("Operador_Multiplicação")) {
            nextToken();
            term();
        }
    }
    
    // Parse the term
    public void term() {
        factor();
        
        while ( accept("Operador_Multiplicação") || accept("Palavra_Reservada_Div") || accept("Palavra_Reservada_And") || accept("Operador_Divisão") ) {
            nextToken();
            factor();
            
            if ( accept("Palavra_Reservada_End") )
                nextToken();
        }
    }
    
    // Parse the factor
    public void factor() {
        if ( accept("Identificador") || accept("Palavra_Reservada_True") || accept("Palavra_Reservada_False") || accept("Palavra_Reservada_Write") || accept("Palavra_Reservada_Read") ) {
            nextToken();
            variable();
        } else if ( accept("Inteiro") || accept("Real") ) {
            nextToken();
        } else if ( accept("Abre_Parenteses") ) {
            nextToken();
            expression();
            
            if ( !accept("Fecha_Parenteses") && !accept("Palavra_Reservada_Begin") && !accept("Palavra_Reservada_Then") ) {
                list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado ')' !") );
                list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
                while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                    nextToken();
                    
                    if ( count == tokens.size() - 1 )
                        break;
                }
            }
            
        } else if ( accept("Palavra_Reservada_Not") ) {
            nextToken();
            factor();
            
        } else {
            list.add(new Error( tokens.get(count).getLine(), "Erro ! Esperado um fator !") );
            list.add(new Error( tokens.get(count).getLine(), "Realizado o tratamento de erros ! Ignorar tokens até encontrar ';'!") );
                
            while ( !accept("Ponto_Virgula") || !accept("Palavra_Reservada_End") || !accept("Palavra_Reservada_Else") ) {
                nextToken();
                
                if ( count == tokens.size() - 1 )
                    break;
            }
        } 
    }
    
    // Parse the variable
    public void variable() {
        if ( accept("Identificador") || accept("Palavra_Reservada_True") || accept("Palavra_Reservada_False") || accept("Palavra_Reservada_Write") || accept("Palavra_Reservada_Read") ) {
            
            nextToken();
            if ( accept("Operador_Soma") || accept("Operador_Subtração") )
                expression();
            
           
        } else if ( accept("Fecha_Parenteses") )
            nextToken();
        
    }
    
    // Parse list of expression
    public void listExpression() {
        expression();
        
        while ( expect("Virgula") ) {
            nextToken();
            expression();
        }
    }
    
    // ######################### --- Semantic Methods --- ###################################
    public ArrayList<Error> analyseSemantic(String textEdit) {
        ArrayList<Error> errorList = createSemanticTable(textEdit);
        ArrayList<Error> globalError;
        ArrayList<Error> localError;
        
        // Verify if a sintatic is ok, if not, return sintatic errors and stop semantic analyse
        if ( errorList.size() > 0 )
            if ( errorList.get( errorList.size()-1 ).getLine().equals("999") )
                return errorList;
        
        globalError = findSemanticErrors(0);
        localError = findSemanticErrors(1);

        // Append the errorList in only one list
        if ( !globalError.isEmpty() )
            errorList.addAll(globalError);

        if ( !localError.isEmpty() )
            errorList.addAll(localError);

        if ( errorList.isEmpty() ) {
            errorList.add(new Error("-1", "Nenhum erro obtido, analise semantica feita com sucesso !"));
            return errorList;
        } else {
            errorList.sort(new Sortybyroll());
            return errorList;
        }
    }
    
    public ArrayList<Error> findSemanticErrors(int level) {
        ArrayList<String> procedimento = new ArrayList<>();
        ArrayList<String> procedimento2 = new ArrayList<>();    
        ArrayList<Error> errorList = new ArrayList<>();
        HashMap <Integer, String> type = new HashMap<>();
        int countRead = 0;
        int countWrite = 0;
        String lexeme, oldLine;
        Symbol symbol;
        int position = 0;
        int begin, end;
        
        if ( level == 0 ) {
            begin = Integer.parseInt(globalList.get(0).getPosition());
            end = Integer.parseInt(globalList.get( globalList.size() - 1).getPosition());
        }
        else {
            begin = Integer.parseInt(localList.get(0).getPosition());
            end = Integer.parseInt(localList.get( localList.size() - 1).getPosition());
        }
        
        for( int i = begin; i < end; i++ )
        {
            lexeme = tokens.get(i).getLexeme();
            symbol = searchSymbol(tokens.get(i).getLexeme(), level);
            
            if ( tokens.get(i).getToken().equals("Identificador") ) { 
                if ( !isDeclared(symbol) && !tokens.get(i-1).getLexeme().equals("program") && !tokens.get(i-1).getLexeme().equals("procedure") && !tokens.get(i-1).getLexeme().equals("var") ) 
                    errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Variavel nunca é declarada: " + symbol.getLexeme() + " ! ") );
                else if ( (level == 0) && searchSymbol(lexeme, 1) != null && !isDeclared(symbol) )
                    errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Escopo inadequado: " + symbol.getLexeme() + " ! ") );
                
                if ( tokens.get(i+1).getToken().equals("Operador_Igual") ) {
                    if ( symbol.getType().equals("Inteiro") ) {
                        if ( tokens.get(i+2).getToken().equals("Palavra_Reservada_True") || tokens.get(i+2).getToken().equals("Palavra_Reservada_False") || tokens.get(i+2).getToken().equals("Real") )
                            errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Atribuindo valor de tipo diferente a variavel !"));
                        
                        if ( tokens.get(i+3).getToken().equals("Operador_Divisão") ){
                            oldLine = tokens.get(i).getLine(); 
                            position = i+2;

                            while( position < tokens.size() && tokens.get(position).getLine().equals(oldLine) ) {
                                if ( tokens.get(position).getToken().equals("Real") )
                                   errorList.add(new Error(tokens.get(position).getLine(), "Erro ! Atribuindo valor de tipo diferente a variavel !"));

                                if ( (tokens.get(position).getToken().equals("Operador_Divisão") || tokens.get(position).getToken().equals("Palavra_Reservada_Div"))  && tokens.get(position+1).getLexeme().equals("0") )
                                    errorList.add(new Error(tokens.get(position).getLine(), "Erro ! Nao e possivel fazer divisao por zero !"));

                                position++;
                            }
                        }
                    } else if ( symbol.getType().equals("Booleano") ) {
                        if ( tokens.get(i+2).getToken().equals("Inteiro") || tokens.get(i+2).getToken().equals("Real") )
                            errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Atribuindo valor de tipo diferente a variavel !"));
                    }
                }
            } else if ( tokens.get(i).getToken().equals("Palavra_Reservada_Read") ) {
                if ( searchSymbol(tokens.get(i+2).getLexeme(), level).getType().equals("Inteiro") )
                    type.put(countRead, "Inteiro");
                else
                    type.put(countRead,"Booleano");
                   
                countRead++;
                
            } else if ( tokens.get(i).getToken().equals("Palavra_Reservada_Write") ) {
                if ( searchSymbol(tokens.get(i+2).getLexeme(), level).getType().equals("Inteiro") ) {
                    if (type.get(countWrite).equals("Booleano") )
                        errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Read and Write com tipos diferentes !"));
                }
                    
                else if ( type.get(countWrite).equals("Inteiro") )
                    errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Read and Write com tipos diferentes !"));
                
                countWrite++;
            
            } else if ( tokens.get(i).getToken().equals("Palavra_Reservada_Procedure") ) {
                oldLine = tokens.get(i).getLine(); 
                position = i;
                position++;
                
                while( position < tokens.size() && tokens.get(position).getLine().equals(oldLine) )
                {
                    if ( tokens.get(position).getToken().equals("Identificador") 
                            || tokens.get(position).getToken().equals("Palavra_Reservada_Int")
                            || tokens.get(position).getToken().equals("Palavra_Reservada_Boolean"))
                        procedimento.add( tokens.get(position).getLexeme() );
                    
                    position++;
                }
                
            } else if ( procedimento.size() > 0 && tokens.get(i).getLexeme().equals(procedimento.get(0)) && !tokens.get(i-1).getToken().equals("Palavra_Reservada_Procedure") ) {
                    oldLine = tokens.get(i).getLine();
                    position = i;
                    position++;
                    
                    while( position < tokens.size() && tokens.get(position).getLine().equals(oldLine) )
                    {
                        if ( tokens.get(position).getToken().equals("Identificador") 
                                || tokens.get(position).getToken().equals("Palavra_Reservada_Int")
                                || tokens.get(position).getToken().equals("Palavra_Reservada_Boolean"))
                            procedimento2.add( tokens.get(position).getLexeme() );
                        
                        position++;
                    }

                    int j = 0;
                    
                    if ( procedimento.size() != procedimento2.size() )
                        errorList.add(new Error(tokens.get(i).getLine(), "Erro ! O numero de elementos dos parametros estao errados !"));
                    else
                        for ( String s: procedimento ) {
                            if ( !s.equals(procedimento2.get(j)) )
                                errorList.add(new Error(tokens.get(i).getLine(), "Erro ! Os elementos dos parametros estao diferentes !"));

                            j++;
                        }
                }
        }
        
        errorList.addAll( isNeverUsedError(level) );
        
        return errorList;
    }
    
    public ArrayList<Error> createSemanticTable(String textEdit) {
        int level;
        ArrayList<Error> errorList = new ArrayList<>();
        globalList = new ArrayList<>();
        localList = new ArrayList<>();
        
        String category, type, value, scope, isUsed, line;
        Symbol symbol = null;
        
        // Verify if sintatic analyse is ok, if yes, clean the errorList and do the semantic analyse
        if ( (errorList = analyseSintatic(textEdit)).size() == 1 && errorList.get(0).getLine().equals("-1") ) {
            errorList = new ArrayList<>();
            level = count = 0;
            
            while ( count < tokens.size() ) {
                category = type = value = scope = isUsed = "";
                
                if ( accept("Palavra_Reservada_Procedure") )
                    level++;
                
                if ( (symbol = searchSymbol(tokens.get(count).getLexeme(), level)) == null ) {
                    category = setCategory(tokens.get(count).getToken());
                    scope = setScope( tokens.get(count).getLexeme(), level);
                    line = tokens.get(count).getLine();
                   
                    if ( category.equals("Variavel") ) {
                        type = setType(count, tokens.get(count).getLine());
                         isUsed = "N";
                    }
                  
                    if ( level == 0 ) 
                        globalList.add(
                          new Symbol(tokens.get(count).getLexeme(), tokens.get(count).getToken(), category, type, value, scope, isUsed, line, String.valueOf(count)) );
                
                    else
                        localList.add(
                          new Symbol(tokens.get(count).getLexeme(), tokens.get(count).getToken(), category, type, value, scope, isUsed, line, String.valueOf(count)) );
                }
                
                else {
                    if ( tokens.get(count).getToken().equals("Identificador") ) {
                        if ( tokens.get(count+1).getToken().equals("Operador_Igual") )
                            if ( tokens.get(count+2).getToken().equals("Inteiro") )
                                symbol.setValue(tokens.get(count+2).getLexeme() + ":Inteiro" );
                            else if ( tokens.get(count+2).getToken().equals("Real") )
                                symbol.setValue(tokens.get(count+2).getLexeme() + ":Real" );
                            else if ( tokens.get(count+2).getToken().equals("Palavra_Reservada_True") || tokens.get(count+2).getToken().equals("Palavra_Reservada_False") )
                                symbol.setValue(tokens.get(count+2).getLexeme() + ":Booleano" );
                            else
                                symbol.setValue(tokens.get(count+2).getLexeme() + ":Expressao" );
                        
                        symbol.setIsUsed("S");
  
                        if ( isDeclared(symbol) ) {
                           int position = count;
                           String oldLine = tokens.get(position).getLine();

                           while ( position > 0 && tokens.get(position).getLine().equals(oldLine) ) {
                               if ( tokens.get(position).getToken().equals("Palavra_Reservada_Int") || tokens.get(position).getToken().equals("Palavra_Reservada_Boolean") ) {
                                   errorList.add(new Error(symbol.getLine(), "Erro ! Variavel já declarada !"));
                                   break;
                               }

                               position--;
                           }
                        }
                    }
                }

                count++;
            }
        } 
        
        // Return the errorList with sintatic errors
        else {
            errorList.add(new Error("999", "Erro ! Erro sintático !!") );
            return errorList;
        }
        
        return errorList;
    }
    
    public ArrayList<Error> isNeverUsedError(int level) {
        ArrayList<Error> errorList = new ArrayList<>();
        ArrayList<Symbol> temp;
        int i = 0;
        
        if ( level == 0 )
            temp = globalList;
        else
            temp = localList;
        
        for ( Symbol s: temp ) {
            if ( (level == 0 && searchSymbol(s.getLexeme(), 1) == null) && s.getIsUsed().equals("N") && !temp.get(i-1).getLexeme().equals("program") && !temp.get(i-1).getLexeme().equals("procedure") && !temp.get(i-1).getLexeme().equals("var") )
                errorList.add(new Error(s.getLine(), "Erro ! Variavel nunca utilizada: " + s.getLexeme() + " ! ") );
        
            i++;
        }
        
        return errorList;
    }
    
    // Verify if the variable is declared   
    public boolean isDeclared(Symbol symbol) {
        int position = Integer.parseInt(symbol.getPosition());
        String oldLine = tokens.get(position).getLine();
        
        while ( position > 0 && tokens.get(position).getLine().equals(oldLine) ) {
            if ( tokens.get(position).getToken().equals("Palavra_Reservada_Int") || tokens.get(position).getToken().equals("Palavra_Reservada_Boolean") )
                return true;

            position--;
        }
        
        return false;
    }
    
    // Search for a existent symbol in the table
    public Symbol searchSymbol(String lexeme, int level) {
        ArrayList<Symbol> temp = new ArrayList<>();
        
        if ( level == 0 )
            temp = globalList;
        else
            temp = localList;
       
        if ( temp != null )
            for( Symbol s: temp )
                if ( s.getLexeme().equals(lexeme) )
                    return s;
        
        return null;
    }
    
    // Insert a Symbol in the table
    public boolean insertSymbol(Symbol symbol, String line, int level) {
        ArrayList<Symbol> temp = new ArrayList<>();
        
        if ( level == 0 )
            temp = globalList;
        else
            temp = localList;
        
        if ( searchSymbol(symbol.getLexeme(),  level) == null ) {
            temp.add(symbol);
            return true;
        } 
        
        return false;
    }
    
    // Remove a Symbol in the table
    public boolean removeSymbol(String lexeme, int level) {
        ArrayList<Symbol> temp = new ArrayList<>();
        
        if ( level == 0 )
           temp = globalList;
        else
           temp = localList;
        
        for( Symbol s: temp )
            if ( s.getLexeme().equals(lexeme) ) {
                temp.remove(s);
                
                return true;
            } 
        
        return false;
    }
    
    public String setCategory(String token) {
        switch(token) {
            case "Palavra_Reservada_Program":
                return "Palavra que inicia o programa";
               
            case "Identificador":
                return "Variavel";
                
            case "Palavra_Reservada_Procedure":
                return "Funcao do programa";
                
            case "Palavra_Reservada_Begin":
                return "Inicio de um bloco";
                
            case "Palavra_Reservada_End":
                return "Final de um bloco";
                
            case "Palavra_Reservada_If":
                return "Condicional";
                
            case "Palavra_Reservada_Read":
                return "Leitor";
                
            case "Palavra_Reservada_Write":
                return "Escrita";
                
            case "Palavra_Reservada_While":
                return "Laco de Repeticao";
                
            case "Palavra_Reservada_Int":
            case "Palavra_Reservada_Boolean":
                return "Tipo de variavel";
                
            default:
                return "--------------";
        }
    }
    
    public String setType(int count, String line) {  
        for ( int i = count; i > 0; i-- ) {
            if ( tokens.get(i).getLine().equals(line) ) {
                if ( tokens.get(i).getToken().equals("Palavra_Reservada_Int") )
                    return "Inteiro";
                else if ( tokens.get(i).getToken().equals("Palavra_Reservada_Boolean") )
                    return "Booleano";
            }
            
            else
                break;
        }
        
        return "";
    }
    
    public String setScope(String lexeme, int level) {
        if ( lexeme.equals("procedure") )
            return "Procedimento";
        else if ( lexeme.equals("a1") )
            return "Parametro";
        else if ( level == 0 )
            return "Global";
        else
            return "Local";
    }
    
    // ######################### --- Generate and Execute Intermediate Code Methods --- ###################################
    public ArrayList<Code> generateIntermediateCode(String textEdit) {
        ArrayList<Analyse> tokens = analyseLexic(textEdit);
        ArrayList<Code> codeList = new ArrayList<>();
        int count = 0;
        int positionExpression = -1;
        String lineExpression = "";
        String actualLine;
        int i = 0;
        
        while ( true ) {  
            actualLine = tokens.get(i).getToken();
            
            switch(tokens.get(i).getToken())
            {
                case "Palavra_Reservada_Program":
                    codeList.add(new Code(-1, "program", "INPP", count) );
                    count++;
                    break;
                
                case "Identificador":
                    if ( !tokens.get(i-1).getToken().equals("Palavra_Reservada_Program") )
                        if ( isDeclaring(tokens, i) ) {
                            codeList.add(new Code(-1, tokens.get(i).getLexeme(), "AMEM 1", count) );
                            count++;
                        }

                        else {
                            codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                                    "CRVL " + getPosition(codeList, tokens.get(i).getLexeme(), i), count) );

                            if ( tokens.get(i+1).getToken().equals("Operador_Igual") ) {
                                positionExpression = count;
                                lineExpression = tokens.get(i).getLine();
                            }

                            count++;

                        }
                    break;
                    
                case "Palavra_Reservada_Read":
                    codeList.add(new Code(-1, "read", "LEIT", count) );
                    count++;
                    
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                            "ARMZ " + getPosition(codeList, tokens.get(i+2).getLexeme(), count), count) );
                    count++;
                    
                    codeList.add(new Code(-1, "read", "LEIT", count) );
                    count++;
                    
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                            "ARMZ " + getPosition(codeList, tokens.get(i+4).getLexeme(), count), count) );
                    count++;
                    i+=6;
                    break;
                
                case "Inteiro":
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                                "CRCT " + tokens.get(i).getLexeme(), count) );
                    count++;
                    break;
                    
                case "Palavra_Reservada_Div":
                    codeList.add(new Code(-1, tokens.get(i+1).getLexeme(), 
                                "CRVL " + getPosition(codeList, tokens.get(i+1).getLexeme(), count), count) );
                    count++;
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                                "DIVI", count) );
                    count++;
                    i++;
                    break;
                
                case "Operador_Soma":
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                                "SOMA", count) );
                    count++;
                    break;
                    
                case "Operador_Multiplicação":
                     codeList.add(new Code(-1, tokens.get(i).getLexeme(), 
                                "MULT", count) );
                     count++;
                     break;
                     
                 case "Palavra_Reservada_Write":
                    codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CRVL 0", count));
                    count++;
                    
                    codeList.add(new Code(-1, "write", "IMPE", count) );
                    count++;
                    i += 3;
                    break;
                    
                 case "Palavra_Reservada_If":
                     codeList.add(new Code(-1, tokens.get(i+1).getLexeme(), 
                            "CRVL " + getPosition(codeList, tokens.get(i+1).getLexeme(), count), count) );
                     count++;
                     
                     codeList.add(new Code(-1, tokens.get(i+3).getLexeme(), 
                            "CRVL " + getPosition(codeList, tokens.get(i+3).getLexeme(), count), count) );
                     count++;
                     
                     if ( tokens.get(i+2).getLexeme().equals(">") ) {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMMA", count) );
                         count++;
                     }
                     else if ( tokens.get(i+2).getLexeme().equals("<") ) {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMME", count) );
                         count++;
                     }
                     else if ( tokens.get(i+2).getLexeme().equals("=") ) {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMIG", count) );
                         count++;
                     }
                     else if ( tokens.get(i+2).getLexeme().equals(">=") ) {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMAG", count) );
                         count++;
                     }
                     else if ( tokens.get(i+2).getLexeme().equals("<=") ) {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMEG", count) );
                         count++;
                     }
                     else {
                         codeList.add(new Code(-1, tokens.get(i+2).getLexeme(), 
                            "CMDG", count) );
                         count++;
                     }
                     
                     codeList.add(new Code(-1, "DSVF", "DSVF 0", count) );
                     count++;
                     
                     i += 3;
                     break;
                     
                 case "Palavra_Reservada_End":
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), "NADA", count) );
                    count++;
                    
                    codeList.add(new Code(-1, tokens.get(i).getLexeme(), "PARA", count) );
                    count++;
            }
            
            if ( codeList.get(count-1).getCode().equals("PARA") ) {
                break;
            }
            
            if ( positionExpression != -1 && !actualLine.equals(lineExpression) ) {
                codeList.add(new Code(-1, "", 
                                "ARMZ ", positionExpression));
                positionExpression = -1;
                lineExpression = "";
                count++;
            }
            
            i++;
        }
        
        return codeList;
    }
    
    public boolean isDeclaring(ArrayList<Analyse> tokens, int position) {
        String oldLine = tokens.get(position).getLine();
        
        if ( position > 0)
            while ( position > 0 && tokens.get(position).getLine().equals(oldLine) ) {
                if ( tokens.get(position).getLexeme().equals("var") )
                    return true;

                position--;
            }
        
        return false;
    }
    
    public int getPosition(ArrayList<Code> codeList, String lexeme, int position) {
        while ( --position > 0 && position < codeList.size()) {
            if ( codeList.get(position).getLexeme().equals(lexeme) )
                return position-1;
        }
        
        return -1;
    }
}

class Sortybyroll implements Comparator<Error>
{
    public int compare(Error a, Error b)
    {
        int linea = Integer.parseInt(a.getLine());
        int lineb = Integer.parseInt(b.getLine());

        return linea - lineb;
    }
}
 