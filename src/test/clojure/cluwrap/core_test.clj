(ns cluwrap.core_test
    (use 
      [cluwrap.core]
      [clojure.test] 
      [cljgiven.core]
      ) 
    (import
      [org.apache.lucene.index IndexWriter IndexWriterConfig]
      ))

(deftest 
  test-create-indexer
  (let [index (create_index_writer {})
        doc1 {:id "doc1"  
              :fields [ 
                        {:name "chars"  :content "The quick brown fox jumps over the lazy dog."} 
                        {:name "alice" :content "Alice was beginning to get very tired of sitting by her sister on the bank,..."}  ] } 
        doc2 {:id "doc2"  
              :fields [ 
                        {:name "title"  :content "New Jockeying in Congress for Next Phase in Budget Fight."} 
                        {:name "content" :content "Congressional leaders have two weeks to name panel members to the committee that is supposed to recommend at least $1.5 trillion of additional deficit-reduction measures."}  ] } 
              ] 
    
    (testing 
      "creating an index"
      (= (class (:writer index))  org.apache.lucene.index.IndexWriter))

    (testing 
      "adding documents"
      ((:add_doc index) doc1 )
      ((:add_doc index) doc2 )
      (is (= (.numDocs (:writer index)) 2))

      (testing 
        "quering the document"
        (let [ result ((index :query) "fox" 100 )]
          ; result must contain doc1
          (is (some #{"doc1"} (map (fn [x] (:id x)) result )))
          ; but must not contain doc2
          (is (not (some #{"doc2"} (map (fn [x] (:id x)) result ))))
          ))

      (testing 
        "getting all tokens"
        (let [tokens((:get_tokens index)) ]
          ; it should contain alice 
          (is (some #{"alice"} tokens))
          ; it should not contain doc2 (i.e. the key)
          (is (not (some #{"doc2"} tokens)))
          ))

      (testing
        "deleting a document"
        ((:delete_doc index) (:id doc1))
        ; now only one doc should be left
        (is (= (.numDocs (:writer index)) 1))
        )

      )))
