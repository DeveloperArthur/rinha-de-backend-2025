(defproject service-worker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [cheshire "6.0.0"]
                 [clj-http "3.13.1"]
                 [com.github.seancorfield/next.jdbc "1.3.1048"]
                 [org.postgresql/postgresql "42.2.10"]]
  :repl-options {:init-ns service-worker.core})
