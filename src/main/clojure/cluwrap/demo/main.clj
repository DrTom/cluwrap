(ns cluwrap.demo.main 
    (:gen-class)
    (:require 
     [cluwrap.demo.analyzer_demo] 
     [cluwrap.demo.build_dirindex] 
     [cluwrap.demo.query_index] 
     [cluwrap.demo.filequery_demo] 
     [cluwrap.demo.tika_demo] 
     ))


(def commands 
     { 

     "build-dirindex" { 
     :desc "build an search index on disk of all files under a given directory"
     :run (fn [args] (cluwrap.demo.build_dirindex/main args)) 
     }

     "query-index" { 
     :desc "query a index"
     :run (fn [args] (cluwrap.demo.query_index/main args)) 
     }


     "filequery" { 
     :desc "run a query over all files in a directory, optionally output found tokens"
     :run (fn [args] (cluwrap.demo.filequery_demo/main args)) 
     }

     "analyzer" { 
     :desc "custom analyzer demo"
     :run (fn [args] (cluwrap.demo.analyzer_demo/main args)) 
     }

     "tika" { 
     :desc "tika"
     :run (fn [args] (cluwrap.demo.tika_demo/main args)) 
     }


     })

(defn help [] 
      (println "about commands:")       
      ;; TODO output the command name(key) and desc in a nice way
      (doseq [command  commands]
        (println command)))
 
(defn -main [& args]

  (let [ [cmd & more] args] 

    (if (commands cmd)
      (do
        println (commands cmd)
        println ( ((commands cmd) :run) (flatten  more) ) 
        )
      (help)
      ))) 
