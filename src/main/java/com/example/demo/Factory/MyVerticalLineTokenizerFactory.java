package com.example.demo.Factory;

import org.apache.lucene.analysis.core.UnicodeWhitespaceTokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import org.apache.lucene.analysis.Tokenizer;
import java.util.Arrays;
import java.util.Collection;

import java.util.Map;

public class MyVerticalLineTokenizerFactory extends TokenizerFactory {
    public static final String RULE_JAVA = "java";
    public static final String RULE_UNICODE = "unicode";
    private static final Collection<String> RULE_NAMES = Arrays.asList(RULE_JAVA, RULE_UNICODE);

    private final String rule;

    /** Creates a new MyVerticalLineTokenizerFactory */
    public MyVerticalLineTokenizerFactory(Map<String,String> args) {
        super(args);

        rule = get(args, "rule", RULE_NAMES, RULE_JAVA);

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        switch (rule) {
            case RULE_JAVA:
                return new MyVerticalLineTokenizer(factory);
            case RULE_UNICODE:
                return new UnicodeWhitespaceTokenizer(factory);
            default:
                throw new AssertionError();
        }
    }

}
