(import 
  (org.apache.lucene.analysis Analyzer StopAnalyzer SimpleAnalyzer WhitespaceAnalyzer)
  (org.apache.lucene.analysis.standard StandardAnalyzer StandardTokenizer StandardFilter)
  (org.apache.lucene.util Version))
   
(proxy [Analyzer] []
       (tokenStream [fieldName,reader]
                    (StandardTokenizer. (. Version LUCENE_33) reader)))
