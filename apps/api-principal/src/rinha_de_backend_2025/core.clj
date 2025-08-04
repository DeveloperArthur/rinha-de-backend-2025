(ns rinha-de-backend-2025.core
  (:require [io.pedestal.http :as http]
            [rinha-de-backend-2025.routes :as routes]
            [rinha-de-backend-2025.service :as service]))

(def service-map {::http/routes routes/endpoints
                  ::http/port   8080
                  ::http/type   :jetty
                  ::http/join?  false})                     ; deixo false mesmo???

(defn start-monitoring-thread []
  (future
    (while true
      (do
        (println "Starting Monitoring Thread at " (.toString (java.time.Instant/now)))
        (service/monitoring-thread)
        (Thread/sleep 10000)))))

(start-monitoring-thread)
(http/start (http/create-server service-map))
(println "Started server http")