(ns rinha-de-backend-2025.core
  (:require [io.pedestal.http :as http]
            [rinha-de-backend-2025.routes :as routes]))

(def service-map {::http/routes routes/endpoints
                  ::http/port   8080
                  ::http/type   :jetty
                  ::http/join?  false})                     ; deixo false mesmo???

(http/start (http/create-server service-map))
(println "Started server http")