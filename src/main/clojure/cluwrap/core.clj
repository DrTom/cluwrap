(ns cluwrap.core
  (import [java.io File])
  (import 
    [org.apache.lucene.analysis.standard StandardAnalyzer ]
    [org.apache.lucene.index IndexWriter IndexWriterConfig Term]
    [org.apache.lucene.store RAMDirectory NIOFSDirectory]
    [org.apache.lucene.util Version]
    [org.apache.lucene.document Document]
    [org.apache.lucene.document Document Field Field$Store Field$Index NumericField]
    [org.apache.lucene.search IndexSearcher] 
    [org.apache.lucene.queryParser QueryParser] 
    ))

(defn create_index [conf]
  "Create a lucene (in memory) index and expose/return interesting structures as a hash.
      The conf parameter should be a hash structure to simulate named parameters."

  (def catch_all_field "_catch_all")
  (def id_field "_id")

  (def version (. Version LUCENE_33))

  (def dir (RAMDirectory.))

  (def analyzer (StandardAnalyzer. version))

  (def config (IndexWriterConfig. version analyzer))
  
  (def writer (IndexWriter. dir config))

  (defn get_reader [] (.getReader writer))

  (defn add_doc [doc]
	"add a document, supply one parameter a map from :id to a
	string and :fields to a sequence of maps each mapping :name
	and :content"
    (let [document (Document.) ]
      (.add document (Field. id_field (:id doc) Field$Store/YES  Field$Index/NOT_ANALYZED))
      (doseq [field (:fields doc)]
        (if (some #{(:name field)} [id_field catch_all_field]) 
          ((throw (IllegalArgumentException. (str "no field may have the name " (:name field) )))))
        (.add document (Field. (:name field) (:content field) Field$Store/NO  Field$Index/ANALYZED)) 
        (.add document (Field. catch_all_field (:content field) Field$Store/NO  Field$Index/ANALYZED)))
      (.addDocument writer document)) 
    )

  (defn delete_doc [doc_id]
        "delete a doc, give the id of type string as the parameter"
        (.deleteDocuments writer (Term. id_field doc_id ))
        (.commit writer))

  (defn query [req_query, maxres]
        "returns a sequence of {:score score, :id id}"

    (let [searcher (IndexSearcher. (get_reader))
          parser (QueryParser. version catch_all_field analyzer)
          query (.parse parser req_query) 
          result (.search searcher query maxres)
          ]

      (map (fn [scoredoc]  
               (let [doc (.doc searcher (.doc scoredoc)) ]
                 {:score (.score scoredoc),  :id  (.get doc id_field)} )) 
           (.scoreDocs result))

      )) 

  (defn get_tokens []
        "returns all tokens in a sorted-set"
        (let [ 
          index_reader (get_reader)
          terms (.terms index_reader) ] 

          (loop [tokens (sorted-set)] 
            (if (.next terms)
              (let [term (.term terms)]
                (if (= (.field term) catch_all_field )  
                  (recur (conj tokens (.text term)) )
                  (recur tokens)
                  ))
              tokens ))))

  {
   :add_doc add_doc
   :analyzer analyzer
   :delete_doc delete_doc
   :dir dir 
   :get_reader get_reader
   :get_tokens get_tokens
   :optimize #(.optimize writer)
   :query query
   :version version
   :writer writer
   } )

