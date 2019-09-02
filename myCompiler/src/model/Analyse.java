/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author wallysonlima
 */
public class Analyse {
    private String lexeme, token, line, iniCol, endCol, error;

    public Analyse(String lexeme, String token, String line, String iniCol, String endCol, String error) {
        this.lexeme = lexeme;
        this.token = token;
        this.line = line;
        this.iniCol = iniCol;
        this.endCol = endCol;
        this.error = error;
    }
    
    public Analyse() {}

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getIniCol() {
        return iniCol;
    }

    public void setIniCol(String iniCol) {
        this.iniCol = iniCol;
    }

    public String getEndCol() {
        return endCol;
    }

    public void setEndCol(String endCol) {
        this.endCol = endCol;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
