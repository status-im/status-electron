(ns status-desktop-front.ui.screens.accounts.login.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require
    [status-im.ui.screens.accounts.login.styles :as st]
    [status-im.ui.screens.accounts.styles :as ast]
    [status-desktop-front.react-native-web :as react]
    [status-desktop-front.ui.components.views :as components]
    [re-frame.core :as re-frame]
    [status-desktop-front.ui.screens.accounts.views :as accounts.views]))

(defview login []
  (letsubs [{:keys [address photo-path name password error processing]} [:get :accounts/login]]
    [react/view {:style (merge ast/accounts-container {:align-items :center})}
     [react/view {:style {:width 300}}]
     [react/view {:style ast/accounts-container}
      [react/view {:style ast/account-title-conatiner}
       [react/text {:style ast/account-title-text}
        "Sign in to Status"]]
      [react/view {:style st/login-view}
       [react/view {:style st/login-badge-container}
        [accounts.views/account-badge address photo-path name]
        [react/view {:style {:height 8}}]
        [react/text-input {:value       (or password "")
                           :placeholder "Type your password"
                           :auto-focus true
                           :style {:margin-top 10
                                   :margin-left 30
                                   :background-color :white
                                   :border-radius 2}
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
       (let [enabled? (pos? (count password))]
         [react/view {:style {:margin-top 16}}
          [react/touchable-highlight (if enabled? {:on-press #(re-frame/dispatch [:login-account address password])})
           [react/view {:style st/sign-in-button}
            [react/text {:style (if enabled? st/sign-it-text st/sign-it-disabled-text)} "Sing In"]]]])]]]))