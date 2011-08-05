(defproject cluwrap "1.0.0"
            :description "A lightweight wrapper around lucene written in clojure."
            :dependencies [
                           [clargon "1.0.0"]
                           [org.apache.lucene/lucene-core "3.3.0"]
                           [org.apache.lucene/lucene-queries "3.3.0"]
                           [org.clojure/clojure "1.2.1"]
                           [org.clojure/clojure-contrib "1.2.0"]
                           ] 
            :dev-dependencies [
                               [swank-clojure "1.3.2"]
                               [clojure-given "1.0.0"] 
                               [vimclojure/server "2.2.0"]
                               ]
            :source-path  "src/main/clojure" 
            :test-path "src/test/clojure"
            :aot [cluwrap.demo.main] 
            :main cluwrap.demo.main
            )

