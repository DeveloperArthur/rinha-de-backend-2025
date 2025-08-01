(ns service-worker.core
  (:require [service-worker.service :as service]))

(defn start-cron []
  (future
    (loop []
      (println "Starting pendents payments processing at " (.toString (java.time.Instant/now)))
      (service/process-pendents-payments)
      (Thread/sleep 120000)                                 ; 2 minutes
      (recur))))

(start-cron)