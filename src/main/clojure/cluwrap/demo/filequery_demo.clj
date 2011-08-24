(ns cluwrap.demo.filequery_demo
    (:gen-class) 
    (:require [clargon.core :as cli])
    (:import 
      [java.io File] 
      [org.apache.lucene.index IndexReader] 
      )
    (:use 
      [cluwrap.filehelper] 
      [cluwrap.core]  
      [cluwrap.extract]
      [clojure.tools.logging :only (error warn info)]
      ))


(defn add_file_to_index [file_info,index]
  (info (str "adding file to index: " (:relative_path file_info)))
  (try      
    (let [ 
           doc {
           :id (:relative_path file_info)
           :fields [ {:name "content",  :content (extract_filecontent_with_tika (File. (:canonical_path file_info))) } ] 
           }] 
      ((:add_doc index) doc))
    (catch Exception ex
           (warn ex))))


(defn 
  main [args]
  (let 
    [ opts 
      (cli/clargon 
        args 
        (cli/required ["--dir" "where to look for files to be indexed" ])
        (cli/optional ["--regex" "regex to match to be indexed files" :default ".*"]) 
        (cli/optional ["--query" "the query"] )
        (cli/optional ["--list-tokens"])
        (cli/optional ["--analyzer-proxy" "load a proxied analyzer from a file"])
        )  
      config (atom {})
      dir (get_canonical_dir_or_throw (opts :dir))]

    (println opts)

    (if (opts :analyzer-proxy)
      (swap! config assoc :analyzer (load-file (opts :analyzer-proxy))))

    (def index (create_index_writer @config)) 

    ;; add files
    (walkdir dir
      (re-pattern(opts :regex))        
      (fn [h]
        (if (:is_file? h) 
          (add_file_to_index h index)
          )))

    ((:optimize index))



    (def tokens
         (if (opts :list-tokens)
           ((:get_tokens index)) 
           []))

    (def query_results 
         (if (opts :query)
           ((index :query) (opts :query) 30 ) ; query
           []))

    ; return something more or less useful
    {:tokens tokens
    ,:query_results query_results
    }
    ))



(defn -main [& args]
      (main (flatten args)) )

