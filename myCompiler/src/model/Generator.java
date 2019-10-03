/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;

/**
 *
 * @author wallysonlima
 */
public class Generator {
    public static void main(String[] args)
    {
        String path = "/home/wallysonlima/Documents/Compiladores/myCompiler/myCompiler/src/model/";
        String filePath = path + "Language.lex";
        
        File file = new File(filePath);
        jflex.Main.generate(file);
    }
}
