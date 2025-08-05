(ns rinha-de-backend-2025.database
  (:require [next.jdbc :as jdbc]))

(def db-config
  {:dbtype   "postgresql"
   :dbname   "root"
   :host     "localhost"
   :user     "root"
   :password "root"})

(def db (jdbc/get-datasource db-config))

(defn save-processed-payment [response processor_name]
  (jdbc/execute! db [(str "INSERT INTO processed_payments (processor_name, processed_at, amount)
                           VALUES ('" processor_name "',
                           '" (:requestedAt response) "',
                           " (:amount response) ")")])
  (println "Saving processed payment to the database..."))

(defn save-pendent-payment [body]
  (jdbc/execute! db [(str "INSERT INTO sync_pendents_payments (correlation_id, amount)
                            VALUES ('" (:correlationId body) "'," (:amount body) ")")])
  (println "Saving pendent payment to sync background..."))

(defn blank-or-nil? [s]
  (or (nil? s) (= s "")))

;; Se `from` for null ou "", assume 5 minutos antes do `now`.
;; Se `to` for null ou "", assume o instante atual (`now`).
(defn get-payments-summary [from to]
  (let [now (java.time.Instant/now)
        from (if (blank-or-nil? from)
               (.toString (.minus now (java.time.Duration/ofMinutes 5)))
               from)
        to (if (blank-or-nil? to)
             (.toString (.minus now (java.time.Duration/ofMinutes 5)))
             to)
        query (str "SELECT processor_name,
                      COUNT(*) AS total_requests,
                      ROUND(SUM(amount)::numeric, 2) AS total_amount
                    FROM processed_payments
                    WHERE processed_at BETWEEN '" from "' AND '" to "'
                    GROUP BY processor_name")]
    (let [payments-summary (jdbc/execute! db [query])]
      payments-summary)))