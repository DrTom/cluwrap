(ns cluwrap.extract_test
    (use
      [cluwrap.extract]
      [clojure.test])
    (import 
      [java.io File]))


(def main_dir (System/getProperty "user.dir"))
(def separator (File/separator) )
(def test_resources_dir (str main_dir separator "src" separator "test" separator "resources"))

(deftest
  test-tika-extraction
  (testing
    "calling and return type"
    (let 
      [hello_txt_content (extract_filecontent_with_tika (File.  (str test_resources_dir separator "hello.txt")))] 
      (is (= (type hello_txt_content)  java.lang.String)) 
      ))
  
  (testing
    "extracting from a text-file"
    (let 
      [hello_txt_content (extract_filecontent_with_tika (File.  (str test_resources_dir separator "hello.txt")))] 
      (is (= "Hello World!\n" hello_txt_content))))

  (testing
    "extracting from a html-file"
    (let 
      [hello_html_content (extract_filecontent_with_tika (File.  (str test_resources_dir separator "hello.html")))] 
      (is (re-matches #"(?s).*Hello World!.*" hello_html_content))))
  (testing
    "extracting a binary file"
    (let 
      [nosense (extract_filecontent_with_tika (File.  (str test_resources_dir separator "nosense.bin")))] 
      ))

  )
