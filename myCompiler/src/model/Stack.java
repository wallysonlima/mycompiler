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
public class Stack {
    public Object[] stack;
    public int position;
    
    public Stack() {
        this.position = -1;
        this.stack = new Object[1000];
    }
    
    public boolean isEmpty() {
        if ( this.position == - 1)
            return true;
        
        return false;
    }
    
    public int size() {
        if ( this.isEmpty() )
            return 0;
        
        return this.position + 1;
    }
    
    public Object getTop() {
        if ( this.isEmpty() )
            return null;
        
        return this.stack[this.position];
    }
    
    public Object pop() {
        if ( isEmpty() )
            return null;
        
        return this.stack[this.position--];
    }
    
    public void push(Object value) {
        if ( this.position < this.stack.length - 1)
            this.stack[++position] = value; 
    }
}
