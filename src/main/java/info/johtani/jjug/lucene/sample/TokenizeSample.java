/*
 * Copyright 2013 Jun Ohtani
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package info.johtani.jjug.lucene.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Tokenize sample for JJUG Night Seminar
 *
 * Created by johtani on 14/12/16.
 */
public class TokenizeSample {

    public static void main(String[] args) {

        String[] texts = {
            "JJUGナイトセミナーでLuceneと日本語検索についてjohtaniが話をしました。",
            "JJUG CCCでElasticsearchとKibanaについてjohtaniが話をしました。",
            "Elasticsearch勉強会でKibana4についてjohtaniが話をしました。"
        };

        Analyzer analyzer;

        //analyzer = new StandardAnalyzer();
        analyzer = new JapaneseAnalyzer();

        for (String text : texts) {
            printToken(text, analyzer);
        }
        analyzer.close();
    }


    private static void printToken(String text, Analyzer analyzer){
        System.out.println("--- Original: ["+text+"]");
        try {
            TokenStream tokens = analyzer.tokenStream("content", text);
            tokens.reset();
            CharTermAttribute termAttr = tokens.getAttribute(CharTermAttribute.class);
            while (tokens.incrementToken()) {
                System.out.println("[" + termAttr.toString() + "]");
            }
            tokens.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
