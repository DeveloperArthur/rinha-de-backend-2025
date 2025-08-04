(ns service-worker.database
  (:require [next.jdbc :as jdbc]))

(def db-config
  {:dbtype   "postgresql"
   :dbname   "root"
   :host     "localhost"
   :user     "root"
   :password "root"})

(def db (jdbc/get-datasource db-config))

(defn get-pendents-payments []
  (jdbc/execute! db ["SELECT * FROM sync_pendents_payments"]))

(defn save-processed-payment [payment]
  (jdbc/execute! db [(str "INSERT INTO processed_payments (processor_name, processed_at, amount)
                           VALUES ('" (:processor_name payment) "',
                           '" (:requestedAt payment) "',
                           " (:amount payment) ")")])
  (println "Saving processed payment to the database..."))

(defn delete-pendent-payment [id]
  (println "Deleting pendent payment with ID:" id)
  (jdbc/execute! db [(str "DELETE FROM sync_pendents_payments WHERE id = " id)]))