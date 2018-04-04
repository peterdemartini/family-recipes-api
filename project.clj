(defproject family-recipes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring-middleware-format "0.7.2"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-core "1.6.3"]
                 [com.novemberain/monger "3.1.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler family-recipes.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
