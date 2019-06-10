(defproject oauth-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [cheshire "5.8.1"]
                 [org.clojure/tools.reader "1.3.2"]
                 [org.clojure/tools.logging "0.5.0-alpha.1"]
                 [ring-logger "1.0.1"]
                                        ; [org.slf4j/slf4j-log4j12 "1.7.25"]
                 [hiccup-table "0.2.0"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [clj-http "3.9.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler oauthsample.core/handler
         :nrepl {:start? true :host "0.0.0.0" :port 47480}})

                                        ;:profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
