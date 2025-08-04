(ns service-worker.service
  (:require [service-worker.gateway :as gateway]
            [service-worker.database :as database]))

(defn build-payment-map [record-payment processor-name]
  (let [requestedAt (.toString (java.time.Instant/now))]
    {:id             (:sync_pendents_payments/id record-payment)
     :correlation_id (str (:sync_pendents_payments/correlation_id record-payment))
     :amount         (:sync_pendents_payments/amount record-payment)
     :requestedAt    requestedAt
     :processor_name processor-name}))

(defn handle-payments [send-payment processor-name]
  (println "Get pendents payments from database")
  (let [pendents-payments (database/get-pendents-payments)]
    (println "Quantity of pendents payments:" (.length pendents-payments))
    (when (seq pendents-payments)
      (doseq [record-payment pendents-payments]
        (let [payment (build-payment-map record-payment processor-name)]
          (send-payment payment)
          (database/save-processed-payment payment)
          (database/delete-pendent-payment (:id payment)))))))

(defn process-pendents-payments []
  (if (= true (gateway/check-default-health))
    (handle-payments gateway/send-payment-to-default "default")
    ;else
    (do
      (if (= true (gateway/check-fallback-health))
        (handle-payments gateway/send-payment-to-fallback "fallback")))))