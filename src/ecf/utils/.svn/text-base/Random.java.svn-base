/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecf.utils;

/**
 *
 * @author Marijan
 */
public class Random extends java.util.Random {

    public Random() {
        super();
    }

    public Random(long seed) {
        super(seed);
    }

    @Override
    public int nextInt() {
        return super.nextInt() & 0x7FFFFFFF;
    }

    @Override
    public long nextLong() {
        return super.nextLong() & 0x7FFFFFFFFFFFFFFFL;
    }
    
}
