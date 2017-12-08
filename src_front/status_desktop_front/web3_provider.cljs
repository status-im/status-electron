(ns status-desktop-front.web3-provider
  (:require [status-desktop-front.status-go :as status-go]))

(def Web3 (js/require "web3"))

(defn make-web3 []
  (Web3. #js {:sendAsync (fn [payload callback]
                           (let [response (status-go/call-web3 (.stringify js/JSON payload))]
                             (if (= "" response)
                               (.log js/console :web3-response-error)
                               (callback nil (.parse js/JSON response)))))}))