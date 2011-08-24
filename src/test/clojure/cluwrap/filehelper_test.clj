(ns cluwrap.filehelper_test
    (:use 
      [cluwrap.filehelper]
      [clojure.test] 
      [cljgiven.core] 
      ))

(defspec get_canonical_dir_or_throw_test

         (Context "test with existing cononical_dir pwd"
                  (Given [dir (-> "user.dir" ( System/getProperty))])
                  (When result (get_canonical_dir_or_throw dir) )
                  (Then (.equalsIgnoreCase result dir)) ; there might be case differences on Windows and MacOS 
                  ) 

         (Context ". should resolve cononical to pwd"
                  (Given [ dir "." 
                          pwd (-> "user.dir" ( System/getProperty)) ])
                  (When result (get_canonical_dir_or_throw dir) )
                  (Then (.equalsIgnoreCase result pwd ))) 

         (Context "bogus dir must throw"
                  (When  bogus "this surely is not a directory" )
                  (Then (thrown? IllegalArgumentException (get_canonical_dir_or_throw bogus))))

         )


