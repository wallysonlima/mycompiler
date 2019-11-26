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
public class Code {
    public int Ifposition;
    public String lexeme;
    public String Code;
    public int Position;

    public Code() {}

    public Code(int Ifposition, String lexeme, String Code, int Position) {
        this.Ifposition = Ifposition;
        this.lexeme = lexeme;
        this.Code = Code;
        this.Position = Position;
    }

    public int getIfposition() {
        return Ifposition;
    }

    public void setIfposition(int Ifposition) {
        this.Ifposition = Ifposition;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int Position) {
        this.Position = Position;
    }
}
