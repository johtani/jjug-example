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
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Sample for JJUG night seminar
 *
 * Searcher sample
 *
 * Created by johtani on 14/12/16.
 */
public class SearcherSample {

    public static void main(String[] args) {

        String indexDirectory = "./indexdir";
        //String keyword = "ナイト";
        String keyword = "johtani";
        IndexReader reader = null;

        try {
            //インデックス保管場所の指定
            Directory dir = FSDirectory.open(new File(indexDirectory));
            //IndexReaderの生成
            reader = DirectoryReader.open(dir);
            //IndexSearcherの生成
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer;
            //Standard
            analyzer = new StandardAnalyzer();
            //日本語用
            //analyzer = new JapaneseAnalyzer();
            //クエリパーサーの生成
            QueryParser parser = new QueryParser(
                "content",
                analyzer);
            //クエリのパース（クエリの組み立て）
            Query query = parser.parse(keyword);
            //クエリと取得したい件数（1ページあたりのデータ）を指定して検索
            TopDocs hits = searcher.search(query, 10);

            //検索条件にヒットしたデータ件数
            System.out.println("Found " + hits.totalHits + " document(s)");

            //検索結果の取得処理
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                System.out.println("--- " + scoreDoc.doc + " ---");
                //データをIDで取得
                Document doc = searcher.doc(scoreDoc.doc);
                //データの取り出し
                System.out.println(doc.get("content"));
                System.out.println("---------");
            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }


    }
}
