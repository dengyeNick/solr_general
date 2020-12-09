package com.example.demo.Factory;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;
public class MyVerticalLineTokenizer extends CharTokenizer {

    public MyVerticalLineTokenizer() {

    }
    public MyVerticalLineTokenizer(AttributeFactory factory) {
        super(factory);
    }

    /** Collects only characters which do not satisfy
     *  参数c指的是term的ASCII值，竖线的值为 124
     */
    @Override
    protected boolean isTokenChar(int c) {
        return !(c == 124);
    }
}

