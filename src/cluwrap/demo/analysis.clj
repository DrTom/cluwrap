(ns cluwrap.demo.analysis
;    (:use clojure.contrib.str-utils)
    (import [java.io File])
    (import 
      [java.io BufferedReader Reader File] 
      [org.apache.lucene.analysis.standard StandardAnalyzer ]
      [org.apache.lucene.index IndexWriter IndexWriterConfig Term]
      [org.apache.lucene.store RAMDirectory NIOFSDirectory]
      [org.apache.lucene.util Version]
      [org.apache.lucene.document Document]
      [org.apache.lucene.document Document Field Field$Store Field$Index NumericField]
      [org.apache.lucene.search IndexSearcher] 
      [org.apache.lucene.queryParser QueryParser] 
      )
    (:use 
     [cluwrap.core]
     ;[clojure.tools.logging :only (error warn info debug)]
     )
    (:require 
     [clojure.tools.logging :as logging])
    )


(defn create_pretokenize_substreader [reader substitutions]
  "This is just for demonstration in the analyzer_demo at this time."
  (def overflow (atom ""))
  (proxy [BufferedReader][reader]
         (read [cbuff off len]
               ; call read(...) of super
               (def read_chars(proxy-super read cbuff off len)) 
               (logging/debug "------------------------------------")
               (logging/debug "CALLED read(cbuff off len) OF BUFFER")
               (logging/debug (str "off: " off))
               (logging/debug (str "len: " len))
               (logging/debug (str "read: " read_chars))
               (def reminder_string (String. cbuff 0  off))
               (def appended_string (String. cbuff off (min (max read_chars 0) len)))
               (logging/debug (str "appended_string: " appended_string))

               (def length_valid_string_in_buffer (+ off (max read_chars 0)))
               (def valid_string_in_buffer (String. cbuff 0 length_valid_string_in_buffer )) 
               (logging/debug (str "valid_string_in_buffer: " valid_string_in_buffer))

               (def substituted_string 
                    (loop [substitutions substitutions, 
                      current_string valid_string_in_buffer ] 
                          (if (first substitutions)
                            (let [ pair (first substitutions)
                              ; use replace instead of re-gsub (which is kinda deprecated), not tested yet
                              ; new_string (re-gsub (first pair) (first (rest pair)) current_string) 
                              new_string (replace current_string (first pair) (first (rest pair)) )
                                  ] 
                              (recur (rest substitutions) new_string))
                            current_string
                            ))) 
               (logging/debug (str "substituted_string: " substituted_string))
               ;; NOW: put back, and possibly take care of the length and prepend overlap at next call  
               read_chars
               )))
