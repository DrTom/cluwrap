(ns cluwrap.demo.filehelper_test
    (:use 
      [cluwrap.demo.filehelper]
      [clojure.test] 
      [cljgiven.core] 
      ))

(defspec get_canonical_dir_or_throw_test

         (Context "test with existing cononical_dir pwd"
                  (Given [dir (-> "user.dir" ( System/getProperty))])
                  (When result (get_canonical_dir_or_throw dir) )
                  (Then (= result dir))
                  ) 

         (Context ". should resolve cononical to pwd"
                  (Given [ dir "." 
                          pwd (-> "user.dir" ( System/getProperty)) ])
                  (When result (get_canonical_dir_or_throw dir) )
                  (Then (= result pwd ))) 

         (Context "bogus dir must throw"
                  (When  bogus "this surely is not a directory" )
                  (Then (thrown? IllegalArgumentException (get_canonical_dir_or_throw bogus))))

         )


