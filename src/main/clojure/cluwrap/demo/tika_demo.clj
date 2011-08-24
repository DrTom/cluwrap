(ns cluwrap.demo.tika_demo
    (:gen-class)
    (:require [clargon.core :as cli])
    (:import 
     (java.io File FileInputStream) 
     (org.apache.tika.parser Parser AutoDetectParser ParseContext)
     (org.apache.tika.metadata Metadata) 
     (org.apache.lucene.index IndexReader) 
     (org.apache.lucene.analysis Analyzer StopAnalyzer SimpleAnalyzer WhitespaceAnalyzer) 
     (org.apache.lucene.analysis.standard StandardAnalyzer StandardTokenizer StandardFilter)
     (org.apache.lucene.util Version) 
     (org.xml.sax ContentHandler) 
     (org.xml.sax.ext DefaultHandler2) 
     )
    (:use 
     [cluwrap.filehelper] 
     [cluwrap.core] ))

(def handler
       (proxy [DefaultHandler2] []
              (characters [ch start length]
                          (println (str "CONTENT: "  (String. ch start length))))
              (startElement [uri localName qName attributes]
                            (println "START ELEMENT ---------------------")
                            (println (str "URI: " uri))
                            (println (str "LOCALNAME: " localName))
                            (println (str "QNAME: " qName))
                            )
              (endElement [uri localName qName ]
                            (println "END ELEMENT -----------------------")
                            (println (str "URI: " uri))
                            (println (str "LOCALNAME: " localName))
                            (println (str "QNAME: " qName))
                            )))


  (defn parse_file [file]

    (def metadata (Metadata.))
    (do (.set metadata Metadata/RESOURCE_NAME_KEY (.getName file)))
    (println metadata)
    (.parse
      (AutoDetectParser.)
      (FileInputStream. file)
      handler
      metadata
      (ParseContext.)))   

(defn main [args]
  (println  "Tika") 


  (let 
    [ opts 
      (cli/clargon 
        args 
        (cli/optional ["--list-supported-types" "show all supported mimetypes" ])
        (cli/optional ["--parse-file" "read a file"]))]

      (if (:list-supported-types opts)  
         (doseq [supported_type (.getSupportedTypes (AutoDetectParser.) (ParseContext.))]
                (println supported_type)
                ))
      
      (if (:parse-file opts)
        (parse_file (File. (:parse-file opts)))
        )))
