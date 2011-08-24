(ns cluwrap.extract
    (require [clojure.java.io :as io])
    (:import
     (java.io File FileInputStream) 
     (org.apache.tika.parser Parser AutoDetectParser ParseContext)
     (org.apache.tika.metadata Metadata) 
     (org.xml.sax.ext DefaultHandler2) 
     ))


(defn extract_filecontent_with_tika [file]
  "Returns the indexable content of the file as a string.  This function
  uses the apache tika AutoDetectParser under the hood. All benefits
  and gotchas of tika parsing apply."

  (def buffer (StringBuffer.))

  (def handler
       (proxy [DefaultHandler2] []
              (characters [ch start length]
                          (.append buffer (String. ch start length)))))

  (def metadata (Metadata.))
  (do (.set metadata Metadata/RESOURCE_NAME_KEY (.getName file)))

  (with-open [is (io/input-stream file)]
             (.parse
               (AutoDetectParser.)
               is
               handler
               metadata
               (ParseContext.))) 

  (.toString buffer))   




