/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.standard;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Factory for {@link UAX29URLEmailTokenizer}. 
 * <pre class="prettyprint">
 * &lt;fieldType name="text_urlemail" class="solr.TextField" positionIncrementGap="100"&gt;
 *   &lt;analyzer&gt;
 *     &lt;tokenizer class="solr.UAX29URLEmailTokenizerFactory" maxTokenLength="255"/&gt;
 *   &lt;/analyzer&gt;
 * &lt;/fieldType&gt;</pre> 
 *
 * @since 3.1
 * @lucene.spi {@value #NAME}
 */
public class UAX29URLEmailTokenizerFactory extends TokenizerFactory {

  /** SPI name */
  public static final String NAME = "uax29UrlEmail";

  private final int maxTokenLength;

  /** Creates a new UAX29URLEmailTokenizerFactory */
  public UAX29URLEmailTokenizerFactory(Map<String,String> args) {
    super(args);
    maxTokenLength = getInt(args, "maxTokenLength", StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }

  @Override
  public UAX29URLEmailTokenizer create(AttributeFactory factory) {
    UAX29URLEmailTokenizer tokenizer = new UAX29URLEmailTokenizer(factory);
    tokenizer.setMaxTokenLength(maxTokenLength);
    return tokenizer;
  }
}
