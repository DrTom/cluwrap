(ns cluwrap.demo.query_index
    (:gen-class) 
    (:require [clojure.tools.cli :as cli])
    (:import 
      [java.io File] 
      [org.apache.lucene.store NIOFSDirectory])
    (:use 
      [cluwrap.filehelper] 
      [cluwrap.core]  
      [cluwrap.extract]
      [clojure.tools.logging :as logging ]))


(defn 
  main [args]
  (let 
    [ opts (first (cli/cli
        args 
        ["--index-dir" "location of the index to bequeried" ]
        ["--query" "the query"] 
        ["--analyzer-proxy" "load a proxied analyzer from a file"]))

      config (atom {}) ]


    (logging/info (str "input options: " opts))

    (swap! config assoc :dir (NIOFSDirectory. (File. (opts :index-dir))))

    (if (opts :analyzer-proxy)
      (swap! config assoc :analyzer (load-file (opts :analyzer-proxy))))


    (logging/info (str "create_index_reader config" @config))

    (def index (create_index_reader @config)) 

    ; TODO list-tokens

    (def query_results 
         (if (opts :query)
           ((index :query) (opts :query) 30 )
           []))


    (doseq [result  query_results ]
           (println result))

    ))




(defn -main [& args]
      (main (flatten args)) )
