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

(defn get-payments-summary []
  ; filtrando por processed_at tras todos os que tem processor_name "default", conta todos os registros, e soma todos os amount
  ; repete a mesma query com fallback

  ;monta o json

  ;HTTP 200 - Ok
  ;{
  ; "default" : {
  ;              "totalRequests": 43236,
  ;              "totalAmount": 415542345.98
  ;              },
  ; "fallback" : {
  ;               "totalRequests": 423545,
  ;               "totalAmount": 329347.34
  ;               }
  ; }

  )