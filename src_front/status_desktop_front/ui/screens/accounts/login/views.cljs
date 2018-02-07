(ns status-desktop-front.ui.screens.accounts.login.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require
    [status-im.ui.screens.accounts.login.styles :as st]
    [status-im.ui.screens.accounts.styles :as ast]
    [status-desktop-front.react-native-web :as react]
    [status-desktop-front.ui.components.views :as components]
    [re-frame.core :as re-frame]
    [status-desktop-front.ui.screens.accounts.views :as accounts.views]
    [clojure.string :as string]))

(defview login []
  (letsubs [{:keys [address photo-path name password error processing]} [:get :accounts/login]]
    [react/view {:style (merge ast/accounts-container {:align-items :center :justify-content :center})}
     [components/back-button #(re-frame/dispatch [:navigate-back])]
     [react/view {:style {:align-items :center}}
      [react/view {:style {:width 290 :align-items :center}}]
      [react/image {:source {:uri (if (string/blank? photo-path) :avatar photo-path)}
                    :style  ast/photo-image}]
      [react/view {:style {:margin-vertical 22}}
        [react/text {:style {:color :white :font-size 18}}
         name]]
      [react/view {:style {:height 52 :width 290 :background-color "#FFFFFF4D"
                           :border-radius 8 :justify-content :center}}
       [react/text-input {:value       (or password "")
                          :placeholder "Password"
                          :auto-focus true
                          :style {:padding-horizontal 17 :color :white}
                          :on-key-press (fn [e]
                                          (let [native-event (.-nativeEvent e)
                                                key (.-key native-event)]
                                            (when (= key "Enter")
                                              (re-frame/dispatch [:login-account address password]))))
                          :secure-text-entry true
                          :on-change   (fn [e]
                                         (let [native-event (.-nativeEvent e)
                                               text (.-text native-event)]
                                           (re-frame/dispatch [:set-in [:accounts/login :password] text])
                                           (re-frame/dispatch [:set-in [:accounts/login :error] ""])))}]]
      [react/view {:style {:margin-top 30}}
       [components/button
        "Sign in"
        (> (count password) 6)
        #(re-frame/dispatch [:login-account address password])]]]]))
