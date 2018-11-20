(defproject dga-pub "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [cheshire "5.8.1"]
                 [org.clojure/tools.reader "1.3.2"]
                 [clj-http "3.9.1"]]
  :ring {:handler dga-pub.core/handler}
  )
