(ns rinha-de-backend-2025.service)

(defn process-payment [request]
  (println (:body request))
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body   "Payment processed successfully"})

(defn get-payments-summary [request]
  (println (:body request))
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body   "Payments summary retrieved successfully"})