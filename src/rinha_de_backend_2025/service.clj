(ns rinha-de-backend-2025.service
  (:require [rinha-de-backend-2025.gateway :as gateway]
            [rinha-de-backend-2025.database :as database]
            [cheshire.core :as cheshire]))

(def DEFAULT_IS_UP (atom false))

(defn json-to-map [request]
  (cheshire/parse-string (slurp (:body request)) true))

;try send-payment catch savependentdb abaixo , abaixo do try (tem como?) save-processed-db;
(defn try-send-payment [send-payment request requestedAt processor_name]
  (let [body (json-to-map request)]
    (try
      (let [response (send-payment body requestedAt)]
        (database/save-processed-payment response processor_name)
        (println "Payment processed successfully"))
      {:status  200
       :headers {"Content-Type" "application/json"}
       :body    (str "Payment processed successfully by " processor_name)}
      (catch Exception e
        (do
          (println "Error processing payment" (.getMessage e))
          (database/save-pendent-payment body)
          {:status  200
           :headers {"Content-Type" "application/json"}
           :body    (str "Saving pendent payment to sync background...")})))))

(defn process-payment [request]
  (println "Processing payment request. DEFAULT_IS_UP =" @DEFAULT_IS_UP)
  (let [requestedAt (java.time.Instant/now)]
    (if (= @DEFAULT_IS_UP true)
      (try-send-payment gateway/send-payment-to-default request (.toString requestedAt) "default")
      (try-send-payment gateway/send-payment-to-fallback request (.toString requestedAt) "fallback"))))

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