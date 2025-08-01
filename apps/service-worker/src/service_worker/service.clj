(ns service-worker.service
  (:require [service-worker.gateway :as gateway]))

(defn process-pendents-payments []
  (if (= true (gateway/check-default-health))
    (println "veio true")
    (println "veio false")))

(process-pendents-payments)