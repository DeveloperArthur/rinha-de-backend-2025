(ns service-worker.service
  (:require [service-worker.gateway :as gateway]
            [service-worker.database :as database]))

(defn build-payment-map [record-payment processor-name]
  (let [requestedAt (.toString (java.time.Instant/now))]
    {:id             (:sync_pendents_payments/id record-payment)
     :correlation_id (str (:sync_pendents_payments/correlation_id record-payment))
     :amount         (:sync_pendents_payments/amount record-payment)
     :requestedAt    requestedAt
     :processor-name processor-name}))

(defn handle-payments [send-payment processor-name]
  (let [pendent-payments (database/get-pendents-payments)]
    (when (seq pendent-payments)
      (doseq [record-payment pendent-payments]
        (let [payment (build-payment-map record-payment processor-name)]
          (println payment)
          ;(println (:sync_pendents_payments/id payment))
          ;(println (str (:sync_pendents_payments/correlation_id payment)))
          ;(println (:sync_pendents_payments/amount payment))
          (send-payment payment)                ; correlation_id, amount, requestedAt
          (database/save-processed-payment payment) ; processor_name, requestedAt , amount
          (database/delete-pendent-payment (:id payment))))))) ; id

(handle-payments gateway/send-payment-to-default "default")

(defn process-pendents-payments []
  (if (= true (gateway/check-default-health))
    (handle-payments gateway/send-payment-to-default "default")
    ;else
    (do
      (if (= true (gateway/check-fallback-health))
        (handle-payments gateway/send-payment-to-fallback "fallback")))))