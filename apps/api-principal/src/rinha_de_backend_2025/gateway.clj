(ns rinha-de-backend-2025.gateway
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def PAYMENT_PROCESSOR_DEFAULT_ENDPOINT "http://payment-processor-default:8080")
(def PAYMENT_PROCESSOR_FALLBACK_ENDPOINT "http://payment-processor-fallback:8080")
(def PAYMENT_PROCESSOR_DEFAULT_TIMEOUT 1500)
(def PAYMENT_PROCESSOR_FALLBACK_TIMEOUT 2000)

(defn build-body [body, requestedAt]
  (cheshire/generate-string {:correlationId (:correlationId body)
                             :amount        (:amount body)
                             :requestedAt   requestedAt}))

(defn send-payment [endpoint, timeout, body, requestedAt]
  (let [request-json (build-body body requestedAt)]
    (println "Processing payment with Request JSON:" request-json)
    (let [response (client/post (str endpoint "/payments")
                                {:headers        {"Content-Type" "application/json"}
                                 :body           request-json
                                 :socket-timeout timeout
                                 :conn-timeout   timeout})]

      (println "Status:" (:status response))
      (println "Body:" (cheshire/parse-string (:body response) true))
      {:correlationId (:correlationId body)
       :amount        (:amount body)
       :requestedAt   requestedAt})))

(defn send-payment-to-default [body, requestedAt]
  (println "Using default payment processor")
  (send-payment PAYMENT_PROCESSOR_DEFAULT_ENDPOINT
                PAYMENT_PROCESSOR_DEFAULT_TIMEOUT body requestedAt))

(defn send-payment-to-fallback [body, requestedAt]
  (println "Using fallback payment processor")
  (send-payment PAYMENT_PROCESSOR_FALLBACK_ENDPOINT
                PAYMENT_PROCESSOR_FALLBACK_TIMEOUT body requestedAt))

(defn verify-payment-processor-default-health []
  (println "Verifying health of payment processor default")
  (try
    (let [response (client/get (str PAYMENT_PROCESSOR_DEFAULT_ENDPOINT "/payments/service-health")
                               {:socket-timeout PAYMENT_PROCESSOR_DEFAULT_TIMEOUT
                                :conn-timeout   PAYMENT_PROCESSOR_DEFAULT_TIMEOUT})]

      (let [response-body (cheshire/parse-string (:body response) true)]
        (println "Default payment processor default health check response:" (:failing response-body))
        ; se failing = true, o serviço está down
        (not (:failing response-body))))
    (catch Exception e
      (println "Error processing pendents payments:" (.getMessage e))
      false)))