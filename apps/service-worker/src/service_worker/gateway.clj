(ns service-worker.gateway
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def PAYMENT_PROCESSOR_DEFAULT_ENDPOINT "http://localhost:8001/payments/service-health")
(def PAYMENT_PROCESSOR_FALLBACK_ENDPOINT "http://localhost:8002/payments/service-health")
(def PAYMENT_PROCESSOR_DEFAULT_TIMEOUT 1500)
(def PAYMENT_PROCESSOR_FALLBACK_TIMEOUT 2000)

(defn check-default-health []
  (let [response (client/get PAYMENT_PROCESSOR_DEFAULT_ENDPOINT
                             {:socket-timeout PAYMENT_PROCESSOR_DEFAULT_TIMEOUT
                              :conn-timeout   PAYMENT_PROCESSOR_DEFAULT_TIMEOUT})]

    (let [response-body (cheshire/parse-string (:body response) true)]
      (:failing response-body))))