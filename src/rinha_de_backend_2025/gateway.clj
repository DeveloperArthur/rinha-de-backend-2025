(ns rinha-de-backend-2025.gateway
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def PAYMENT_PROCESSOR_DEFAULT_ENDPOINT "http://localhost:8001/payments")
(def PAYMENT_PROCESSOR_FALLBACK_ENDPOINT "http://localhost:8002/payments")
(def PAYMENT_PROCESSOR_DEFAULT_TIMEOUT 1500)
(def PAYMENT_PROCESSOR_FALLBACK_TIMEOUT 2000)

(defn build-body [body, requestedAt]
  (cheshire/generate-string {:correlationId (:correlationId body)
                             :amount        (:amount body)
                             :requestedAt   (.toString requestedAt)}))

(defn json-to-map [request]
  (cheshire/parse-string (slurp (:body request)) true))

(defn send-payment [endpoint, timeout, request, requestedAt]
  (let [body (json-to-map request)]
    (let [request-json (build-body body requestedAt)]
      (println "Processing payment with Request JSON:" request-json)
      (let [response (client/post endpoint
                                  {:headers        {"Content-Type" "application/json"}
                                   :body           request-json
                                   :socket-timeout timeout
                                   :conn-timeout   timeout})]

        (println "Status:" (:status response))
        (println "Body:" (cheshire/parse-string (:body response) true))))))

(defn send-payment-to-default [request, requestedAt]
  (println "Using default payment processor")
  (send-payment PAYMENT_PROCESSOR_DEFAULT_ENDPOINT
                PAYMENT_PROCESSOR_DEFAULT_TIMEOUT request requestedAt))

(defn send-payment-to-fallback [request, requestedAt]
  (println "Using fallback payment processor")
  (send-payment PAYMENT_PROCESSOR_FALLBACK_ENDPOINT
                PAYMENT_PROCESSOR_FALLBACK_TIMEOUT request requestedAt))