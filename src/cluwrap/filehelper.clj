(ns cluwrap.filehelper
  (:import [java.io File]) 
  (:import [])
  )


(defn get_canonical_dir_or_throw [s]
  (def cp (-> s File. .getCanonicalPath))
  (if (.isDirectory(File. cp ))
    cp
    (throw (IllegalArgumentException. (str s " is not a directory")))
    ))  


(defn walkdir [start_dir,pattern,fun] 
  ( let [canonical_start_dir (get_canonical_dir_or_throw start_dir)]

    (doseq [file (-> canonical_start_dir File. file-seq)]
      (if (re-matches pattern (.getCanonicalPath file))
        (fun {:canonical_path (.getCanonicalPath file) 
          :relative_path ( .substring ( -> file .getCanonicalPath) (.length canonical_start_dir) )
          :is_file? (.isFile file)
          })))
    ))

