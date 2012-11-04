(defproject cluwrap "1.2.0"
  :description "A lightweight wrapper around lucene written in clojure."
  :license {:name "WTFPL - Do What The Fuck You Want To Public License"
            :url "http://sam.zoy.org/wtfpl/"}
  :dependencies (
                 [org.apache.lucene/lucene-core "3.6.1"]
                 [org.apache.lucene/lucene-queries "3.6.1"]
                 [org.apache.tika/tika-parsers "1.2"] 
                 [org.clojure/clojure "1.4.0"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.clojure/tools.logging "0.2.3"]
                 )

  :profiles {:dev {
                   :dependencies [
                                  [log4j "1.2.15" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]
                                  [org.apache.tika/tika-app "1.2"] 
                                  ]}}

  :aot [cluwrap.demo.main] 
  :main cluwrap.demo.main
  )
