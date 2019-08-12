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
    private String lexeme, token, value, line, column;

    public Analyse(String lexeme, String token, String value, String line, String column) {
        this.lexeme = lexeme;
        this.token = token;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public String getLexema() {
        return lexeme;
    }

    public void setLexema(String lexema) {
        this.lexeme = lexema;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValor() {
        return value;
    }

    public void setValor(String valor) {
        this.value = valor;
    }

    public String getLinha() {
        return line;
    }

    public void setLinha(String linha) {
        this.line = linha;
    }

    public String getColuna() {
        return column;
    }

    public void setColuna(String coluna) {
        this.column = coluna;
    }
}
