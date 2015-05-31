(defproject avatar "0.1.0-SNAPSHOT"
  :description "This is my avatar, and how I made it."
  :url "https://github.com/auxiliary-character/avatar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot avatar.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
