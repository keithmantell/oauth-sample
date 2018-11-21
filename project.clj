(defproject oauth-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [cheshire "5.8.1"]
                 [org.clojure/tools.reader "1.3.2"]
                 [org.clojure/tools.logging "0.4.1"]
                ; [org.slf4j/slf4j-log4j12 "1.7.25"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [clj-http "3.9.1"]]
  :plugins [[lein-ring "0.12.4"]]
  :ring {:handler oauthsample.core/handler}
  )
