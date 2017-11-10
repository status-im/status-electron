(ns status-desktop-front.protocol
  (:require [status-im.protocol.core :as protocol]
            [status-im.utils.random :as random]))

(def identity-1 "0x0493c4c7f79dab83651e85695bf9fc0c236cb0ec8070108dc929eabe6d5a5f1c6dd6835264990de710a887a53bc0306c53b2421e1dda9cc2a0c2e3ec855b7e8e81")
(def identity-2 "0x047071d8929ad79d9312870fbd74736541ba443fd96e7b374a85b0cd9bcf7a2ab673e55e632d2b1d09cb460169049013210c83846370cf3d9adc8045cba288216f")

(def rpc-url "http://localhost:8645")
(def Web3 (js/require "web3"))
(def web3 (Web3. (Web3.providers.HttpProvider. rpc-url)))

(defn make-callback [identity]
  (fn [& args]
    (.log js/console (str :post-callback " " identity "\n" args))))

(defn post-error-callback [identity]
  (fn [& args]
    (.log js/console (str :post-error " " identity "\n" args))))

(defn test-post-shh [])


(defn init-whisper! []
  (let [
        {:keys [private public]} (protocol/new-keypair!)
        common-config {:web3                        web3
                       :identity                    identity-1
                       :groups                      []
                       :callback                    (make-callback identity-1)
                       :ack-not-received-s-interval 125
                       :default-ttl                 120
                       :send-online-s-interval      180
                       :ttl-config                  {:public-group-message 2400}
                       :max-attempts-number         1
                       :delivery-loop-ms-interval   500
                       :profile-keypair             {:public  public
                                                     :private private}
                       :hashtags                    []
                       :pending-messages            []
                       :contacts                    []
                       :post-error-callback         (post-error-callback identity-1)}]
    (protocol/init-whisper! common-config)
    (protocol/contact-request!
      {:web3    web3
       :message {:from       identity-1
                 :to         identity-2
                 :message-id (random/id)
                 :payload    {:contact {:name          "testname"
                                        :profile-image ""
                                        :address       "0"
                                        :status        "teststatus"}
                              :keypair {:public  public
                                        :private private}}}})))

(defn send-message [text]
  (let [mess-id (random/id)]
    (protocol/send-message!
      {:web3    web3
       :message {:message-id mess-id
                 :from       identity-1
                 :to         identity-2
                 :payload    {:message-id    mess-id
                              :requires-ack? false,
                              :type          :message,
                              :timestamp     1498723691404,
                              :content       text,
                              :content-type  "text/plain",
                              :clock-value   1,
                              :show?         true}}})))