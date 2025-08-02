(ns service-worker.gateway
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def PAYMENT_PROCESSOR_DEFAULT_ENDPOINT "http://localhost:8001/payments/service-health")
(def PAYMENT_PROCESSOR_FALLBACK_ENDPOINT "http://localhost:8002/payments/service-health")
(def PAYMENT_PROCESSOR_DEFAULT_TIMEOUT 1500)
(def PAYMENT_PROCESSOR_FALLBACK_TIMEOUT 2000)

(defn verify-payment-processor-health [endpoint timeout]
  (let [response (client/get endpoint
                             {:socket-timeout timeout
                              :conn-timeout   timeout})]

    (let [response-body (cheshire/parse-string (:body response) true)]
      (:failing response-body))))

(defn check-default-health []
  (let [response (verify-payment-processor-health PAYMENT_PROCESSOR_DEFAULT_ENDPOINT
                                                  PAYMENT_PROCESSOR_DEFAULT_TIMEOUT)]
    response))

(defn check-fallback-health []
  (let [response (verify-payment-processor-health PAYMENT_PROCESSOR_FALLBACK_ENDPOINT
                                                  PAYMENT_PROCESSOR_FALLBACK_TIMEOUT)]
    response))

(defn build-body [body]
  (cheshire/generate-string {:correlationId (:correlation_id body)
                             :amount        (:amount body)
                             :requestedAt   (:requestedAt body)}))

(defn send-payment [endpoint, timeout, body]
  (let [request-json (build-body body)]
    (println "Processing payment with Request JSON:" request-json)
    (let [response (client/post endpoint
                                {:headers        {"Content-Type" "application/json"}
                                 :body           request-json
                                 :socket-timeout timeout
                                 :conn-timeout   timeout})]

      (println "Status:" (:status response))
      (println "Body:" (cheshire/parse-string (:body response) true)))))

(defn send-payment-to-default [body]
  (println "Using default payment processor")
  (send-payment PAYMENT_PROCESSOR_DEFAULT_ENDPOINT
                PAYMENT_PROCESSOR_DEFAULT_TIMEOUT body))

(defn send-payment-to-fallback [body, requestedAt]
  (println "Using fallback payment processor")
  (send-payment PAYMENT_PROCESSOR_FALLBACK_ENDPOINT
                PAYMENT_PROCESSOR_FALLBACK_TIMEOUT body))