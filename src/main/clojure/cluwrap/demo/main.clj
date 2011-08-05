(ns cluwrap.demo.main
    (:gen-class) 
    (:require [clargon.core :as cli])
    (:import 
      [java.io File] 
      [org.apache.lucene.index IndexReader] 
      )
    (:use 
      [cluwrap.demo.filehelper] 
      [cluwrap.core]  
      ))

(defn add_file_to_index [file_info,index]
      (let [ 
        doc {
          :id (:relative_path file_info)
          :fields [ {:name "content",  :content (slurp (:canonical_path file_info)) } ] 
          }] 
        ((:add_doc index) doc)
      ))

(defn -main [& args]
  (let [
        opts (cli/clargon args 
                          (cli/required ["--dir" "where to look for files to be indexed" ])
                          (cli/optional ["--regex" "regex to match to be indexed files" :default ".*"]) 
                          (cli/optional ["--query" "the query"] )
                          (cli/optional ["--list-tokens"])
                          )  
      
        dir (get_canonical_dir_or_throw (opts :dir))
        index (create_index {}) ]

    (println opts)

    ;; add files
    (walkdir dir
      (re-pattern(opts :regex))        
      (fn [h]
        (if (:is_file? h) 
          (add_file_to_index h index)
          )))

    ((:optimize index))

    (if (opts :list-tokens)
      (println ((:get_tokens index)))) 

    ;; run query
    (if (opts :query)
      ((def results ((index :query) (opts :query) 30 )) 
       (doseq [result results]
         (println result))))

    ))



