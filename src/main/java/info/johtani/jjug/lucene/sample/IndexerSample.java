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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample for JJUG night seminar
 *
 * Indexer sample
 *
 * Created by johtani on 14/12/16.
 */
public class IndexerSample {

    public static void main(String[] args) {

        String indexDirectory = "./indexdir";
        String[] texts = {
            "JJUGナイトセミナーでLuceneと日本語検索についてjohtaniが話をしました。",
            "JJUG CCCでElasticsearchとKibanaについてjohtaniが話をしました。",
            "Elasticsearch勉強会でKibana4についてjohtaniが話をしました。"
        };
        IndexWriter writer = null;

        try {
            //インデックス保管場所の指定
            Directory dir = FSDirectory.open(new File(indexDirectory));
            //インデキシング時にテキストを分割するアナライザ
            StandardAnalyzer analyzer = new StandardAnalyzer();
            //インデックス書き込みに関する設定
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
            //CREATE_OR_APPEND：すでにある場合は追記、ない場合は作成するモード
            //CREATE：作成するモード。すでにある場合は削除される
            //config.setOpenMode(OpenMode.CREATE_OR_APPEND);
            config.setOpenMode(OpenMode.CREATE);
            writer = new IndexWriter(dir, config);

            //インデックスへデータを追加
            for (String text : texts) {
                //データの書き込み
                writer.addDocument(getDocument(text));
            }

            // 複数の場合はListも利用可能
            //List<Document> docs = new ArrayList<Document>();
            //docs.add(document);
            //writer.addDocuments(docs);

            //writerを使い回し、定期的にコミットしたい場合は実行。
            //writer.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //close時にコミットも実行される
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        System.out.println("Finished!");
    }

    //インデックスするデータの生成
    public static Document getDocument(String text) {
        Document document = new Document();
        //"content"フィールドに対して文字列データを追加
        document.add(new TextField("content",text, Field.Store.YES));
        //document.add(new TextField("content", new StringReader(text)));
        return document;
    }

}
