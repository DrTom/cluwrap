(ns cluwrap.filehelper_test
    (:use 
      [clojure.test]
      [cluwrap.filehelper]
      ))

(deftest get_canonical_dir_or_throw_test

  (testing 
    "test with existing cononical_dir pwd"
    (let [
          dir (-> "user.dir" ( System/getProperty))
          result (get_canonical_dir_or_throw dir)
          ]
      (is (.equalsIgnoreCase result dir))))
 
  (testing ". should resolve cononical to pwd"
           (let [ dir "." 
                 pwd (-> "user.dir" ( System/getProperty)) 
                 result (get_canonical_dir_or_throw dir) ]
             (is (.equalsIgnoreCase result pwd ))) )
  
  (testing "bogus dir must throw"
           (let [bogus "this surely is not a directory" ]
             (is (thrown? IllegalArgumentException (get_canonical_dir_or_throw bogus)))))
  )


