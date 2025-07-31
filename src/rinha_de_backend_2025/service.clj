(ns rinha-de-backend-2025.service
  (:require [rinha-de-backend-2025.gateway :as gateway]
            [rinha-de-backend-2025.database :as database]))

(def DEFAULT_IS_UP (atom false))

(defn try-send-payment [send-payment request requestedAt processor_name]
  (try
    (let [response (send-payment request requestedAt)]
      (database/save-processed-payment response processor_name)
      (println "Payment processed successfully. Response:" response))
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    "Payment processed successfully by " processor_name}
    (catch Exception e
      (do
        (println "Error processing payment" (.getMessage e))
        (database/save-pendent-payment request)
        {:status  200
         :headers {"Content-Type" "application/json"}
         :body    "Payment processed successfully by " processor_name}))))

(defn process-payment [request]
  (println "Processing payment request. DEFAULT_IS_UP =" @DEFAULT_IS_UP)
  (let [requestedAt (java.time.Instant/now)]
    (if (= @DEFAULT_IS_UP true)
      (try-send-payment gateway/send-payment-to-default request requestedAt "default")
      (try-send-payment gateway/send-payment-to-fallback request requestedAt "fallback"))))

(defn get-payments-summary [request]
  (println (:body request))
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    "Payments summary retrieved successfully"})

; Thread de Monitoramento
(defn background-sync []
  ;(reset! DEFAULT_IS_UP true)
  ;(swap! DEFAULT_IS_UP not)
  )