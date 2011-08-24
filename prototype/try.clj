
(type myanalyzer)



(:import org.apache.tika.parser Parser)

(:require clojure.contrib.str-utils)

(re-gsub #"f#" "fsharp" "asdf f#.")


;;;;;

(doc use)
(doc refer)

(["a" "b" "c"] 3)

(first["a" "b"])
(first (rest ["a" "b"]))

(use 'clojure.xml) 

(doc Parser)

(def parsed (parse "/home/u204013/tmp/MVP Modelle/EARoot/EA1/EA1/EA10.htm") )

(println parsed)

(list \a \b \c)
'(\a \b \c)


(main nil)

(str "abc" "xyz")
