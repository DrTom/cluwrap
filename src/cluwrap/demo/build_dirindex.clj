(ns cluwrap.demo.build_dirindex
    (:gen-class) 
    (:require [clojure.tools.cli :as cli])
    (:import 
      [java.io File] 
      [org.apache.lucene.store NIOFSDirectory])
    (:use 
      [cluwrap.filehelper] 
      [cluwrap.core]  
      [cluwrap.extract]
      [clojure.tools.logging :as logging ]
      ))


(defn add_file_to_index [file_info,index]
  (logging/info (str "adding file to index: " (:relative_path file_info)))
  (try      
    (let [ 
           doc {
           :id (:relative_path file_info)
           :fields [ {:name "content",  :content (extract_filecontent_with_tika (File. (:canonical_path file_info))) } ] 
           }] 
      ((:add_doc index) doc))
    (catch Exception ex
           (logging/warn (str "Processing of " (:relative_path file_info) " failed; enable DEBUG for more information"))
           (logging/debug ex)
           )))

(defn 
  main [args]
  
  (let [ 
        opts (first (cli/cli
               args 
               ["--dir" "where to look for files to be indexed" ]
               ["--index-dir" "where to store the index on disk" ]
               ["--regex" "regex to match to be indexed files" :default ".*"]
               ["--analyzer-proxy" "load a proxied analyzer from a file"]
               ))  
        config (atom {})
        source_dir (get_canonical_dir_or_throw (opts :dir))
        ]

    (logging/info (str "input options: " opts))

    (swap! config assoc :dir (NIOFSDirectory. (File. (opts :index-dir))))

    (if (opts :analyzer-proxy)
      (swap! config assoc :analyzer (load-file (opts :analyzer-proxy))))


    (logging/info (str "create_index_writer config" @config))

    (def index (create_index_writer @config)) 

    ;; delete all that is there
    (.deleteAll (:writer index))

    ;; add files
    (walkdir source_dir
             (re-pattern(opts :regex))        
             (fn [h]
               (if (:is_file? h) 
                 (add_file_to_index h index)
                 )))

    ;; optimize and close
    ((:optimize index))
    (.close (:writer index))
    ))

(defn -main [& args]
      (main (flatten args)) )
