(ns rinha-de-backend-2025.core
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [rinha-de-backend-2025.routes :as routes]
            [rinha-de-backend-2025.service :as service]))

(def service-map {::http/routes routes/endpoints
                  ::http/port   8080
                  ::http/host   "0.0.0.0"
                  ::http/type   :jetty
                  ::http/join?  false})

(defn start-monitoring-thread []
  (future
    (while true
      (do
        (println "Starting Monitoring Thread at " (.toString (java.time.Instant/now)))
        (service/monitoring-thread)
        (Thread/sleep 5000)))))

(defn -main [& args]
  (http/start (http/create-server service-map))
  (println "Started server http")
  (start-monitoring-thread))