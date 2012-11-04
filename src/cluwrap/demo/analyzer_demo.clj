(ns cluwrap.demo.analyzer_demo
    (:gen-class)
    (:require [clojure.tools.cli :as cli])
    (:import 
     (java.io BufferedReader Reader File) 
     (org.apache.lucene.index IndexReader) 
     (org.apache.lucene.analysis Analyzer StopAnalyzer SimpleAnalyzer WhitespaceAnalyzer) 
     (org.apache.lucene.analysis.standard StandardAnalyzer StandardTokenizer StandardFilter)
     (org.apache.lucene.util Version) )
    (:use 
     [cluwrap.filehelper] 
     [cluwrap.core] 
     [cluwrap.demo.analysis] 
;     [clojure.contrib.str-utils]
     ))


(def substitutions
     [[#"(?i)f#" "fsharp"]
      [#"(?i)c#" "csharp"]
      ]) 

(defn presubs_stdtokenizer [fieldName,reader] 
  (StandardTokenizer. (Version/LUCENE_33) (create_pretokenize_substreader reader substitutions)))


(def std_analyzer 
  (proxy [Analyzer] []
         (tokenStream [fieldName,reader]
                      (presubs_stdtokenizer fieldName reader))))


(def myanalyzer
  (proxy [Analyzer] []
         (tokenStream [fieldName,reader]
                      (StandardTokenizer. (. Version LUCENE_33) reader)) 
         ))



(def markdown 
     "# Markdown 

     **This is in bold**

     _This is in italics_ and _italics_word_

     ")



(def test_doc {
     :id "some_id"
     :fields 
     [ 
       {:name "thefox", :content "The quick brown fox jumps over the lazy dog." }
       {:name "markdown", :content markdown} 
       {:name "shortcuts" :content "F# C#"}
       {:name "loreipsum" :content "Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
       ]}) 

(defn main [args]
  (let

    ;; TODO now change the api such that create_index_writer  accepts custome analyzers and test it ...
    [index (create_index_writer {:analyzer  std_analyzer})]

    (do ((:add_doc index) test_doc))

    ((:optimize index))


    (println ((:get_tokens index)))


    ))


