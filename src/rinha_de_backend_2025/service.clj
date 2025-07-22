(ns rinha-de-backend-2025.service
  (:require [rinha-de-backend-2025.gateway :as gateway]
            [rinha-de-backend-2025.database :as database]))

(def DEFAULT_IS_UP (atom false))

(defn try-send-payment [send-payment request requestedAt]
  (try
    (let [response (send-payment request requestedAt)]
      (database/save-processed-payment))
    (catch Exception e
      (do
        (println "Error processing payment" (.getMessage e))
        (database/save-pendent-payment)))))

(defn process-payment [request]
  (println "Processing payment request. DEFAULT_IS_UP =" @DEFAULT_IS_UP)
  (let [requestedAt (java.time.Instant/now)]
    (if (= @DEFAULT_IS_UP true)
      (try-send-payment gateway/send-payment-to-default request requestedAt)
      (try-send-payment gateway/send-payment-to-fallback request requestedAt))))

(defn get-payments-summary [request]
  (println (:body request))
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    "Payments summary retrieved successfully"})

(defn background-sync []
  ;(reset! DEFAULT_IS_UP true)
  ;(swap! DEFAULT_IS_UP not)
  )