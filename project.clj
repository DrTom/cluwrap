(defproject cluwrap "1.1.0"
            :description "A lightweight wrapper around lucene written in clojure."
            :dependencies [
                           [clargon "1.0.0"]
                           [org.apache.lucene/lucene-core "3.3.0"]
                           [org.apache.lucene/lucene-queries "3.3.0"]
                           [org.apache.tika/tika-parsers "0.9"] 
                           [org.clojure/clojure "1.2.1"]
                           [org.clojure/clojure-contrib "1.2.0"]
                           [org.clojure/tools.logging "0.1.2"]
                           ] 
            :dev-dependencies [
                               [clojure-given "1.0.0"] 
                               [log4j "1.2.15" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]
                               [org.apache.tika/tika-app "0.9"] 
                               [swank-clojure "1.3.2"]
                               ]
            :source-path  "src/main/clojure" 
            :test-path "src/test/clojure"
            :dev-resources-path "src/dev/resources"
            :aot [cluwrap.demo.main] 
            :main cluwrap.demo.main
            )

