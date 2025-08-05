(ns service-worker.core
  (:require [service-worker.service :as service]
            [io.pedestal.http :as http]))

(def service-map {::http/routes []
                  ::http/port   8083
                  ::http/type   :jetty
                  ::http/join?  false})

(defn start-scheduler []
  (future
    (while true
      (try
        (println "Starting pendents payments processing at " (.toString (java.time.Instant/now)))
        (service/process-pendents-payments)
        (Thread/sleep 120000)
        (catch Exception e
          (println "Error processing pendents payments:" (.getMessage e))
          (Thread/sleep 120000))))))

(http/start (http/create-server service-map))
(println "Started server http")
(start-scheduler)