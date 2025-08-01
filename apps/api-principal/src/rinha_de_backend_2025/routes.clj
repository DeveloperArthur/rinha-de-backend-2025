(ns rinha-de-backend-2025.routes
  (:require [io.pedestal.http.route :as route]
            [rinha-de-backend-2025.service :as service]))

(def endpoints (route/expand-routes
                 #{["/payments" :post service/process-payment
                    :route-name :process-payment]
                   ["/payments-summary" :get service/get-payments-summary
                    :route-name :get-payments-summary]}))