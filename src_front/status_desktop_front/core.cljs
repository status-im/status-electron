(ns status-desktop-front.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frisk-remote.core :refer [enable-re-frisk-remote!]]
            [re-frame.core :as re-frame]
            [status-desktop-front.ui.screens.chat.view :refer [chat]]
            [status-desktop-front.protocol :as protocol]
            status-desktop-front.ui.screens.subs
            status-desktop-front.ui.screens.events))

(defn mount-root []
  (reagent/render [chat]
                  (.getElementById js/document "app")))

(re-frame/reg-event-db :init-app-db
                       (fn [db]
                         ;(test-send-message!)
                         (assoc db :view-id :chat-list :messages [{:text "At least i hope so"} {:text "That's what i thought"} {:text "a minute ago"}])))

(defn init []
  (re-frame/dispatch-sync [:init-app-db])
  (mount-root)
  (protocol/init-whisper!))

(defn init! [setting]
  (init))
  ;(enable-re-frisk-remote! {:on-init init}))
