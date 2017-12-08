(ns status-desktop-front.ui.screens.accounts.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.screens.accounts.styles :as st]
            [status-desktop-front.react-native-web :as react]
            [status-desktop-front.ui.components.views :as components]
            [re-frame.core :as re-frame]))

(defn account-badge [address photo-path name]
  [react/view {:style st/account-badge}
   [react/view {:style st/account-badge-text-view}
    [react/text {:style st/account-badge-text}
     (or name address)]]])

(defn account-view [{:keys [address photo-path name] :as account}]
  [react/view {:style {:margin-top 10}}
   [react/touchable-highlight {:on-press #(re-frame/dispatch [:open-login address photo-path name])}
    [react/view {:style st/account-view}
     [account-badge address photo-path name]]]])

(defview accounts []
  (letsubs [accounts [:get-accounts]]
    [react/view {:style (merge  st/accounts-container {:align-items :center})}
     [react/view {:style {:width 300  :flex 1}}
      [react/view {:style st/account-title-conatiner}
       [react/text {:style st/account-title-text}
        "Sign in to Status"]];(i18n/label :t/sign-in-to-status)]]
      [react/view {:style st/accounts-list-container}
       [react/view {:style {:flex 1}} ;:background-color "#eef2f5"}}
        [react/scroll-view
         [react/view
          (for [[index account] (map-indexed vector (vals accounts))]
            ^{:key index} [account-view account])]]]]
      [react/view {:style st/bottom-actions-container}
       [components/action-button (merge
                                   {:label     "Create new account" ;(i18n/label :t/create-new-account)
                                    :icon      :icons/add
                                    :icon-opts {:color :white}
                                    :on-press  #(re-frame/dispatch [:navigate-to-clean :create-account])}
                                   st/accounts-action-button)]
       [components/separator st/accounts-separator st/accounts-separator-wrapper]
       [components/action-button (merge
                                   {:label     "Recover access";(i18n/label :t/recover-access)
                                    :icon      :icons/dots-horizontal
                                    :icon-opts {:color :white}}
                                    ;:on-press  #(re-frame/dispatch [:navigate-to :recover])}
                                   st/accounts-action-button)]]]]))

(defview create-account []
  (letsubs [password [:get-in [:accounts/create :password]]
            password-confirm [:get-in [:accounts/create :password-confirm]]]
    [react/view {:style (merge  st/accounts-container {:align-items :center})}
     [react/view {:style {:width 300}}
      [react/view {:style st/account-title-conatiner}
       [react/text {:style st/account-title-text}
        "Create Account"]];(i18n/label :t/sign-in-to-status)]]
      [react/view {:style st/accounts-list-container}
       [react/text {:style st/account-title-text}
        "Password"]
       [react/text-input {:value       (or password "")
                          :auto-focus  true
                          :placeholder "Type your password"
                          :style {:margin-top 10
                                  :background-color :white
                                  :border-radius 2}
                          :secure-text-entry true
                          :on-change   (fn [e]
                                         (let [native-event (.-nativeEvent e)
                                               text (.-text native-event)]
                                           (re-frame/dispatch [:set-in [:accounts/create :password] text])))}]
       [react/text {:style (merge st/account-title-text {:margin-top 20})}
        "Confirm"]
       [react/text-input {:value       (or password-confirm "")
                          :placeholder "Type your password"
                          :style {:margin-top 10
                                  :background-color :white
                                  :border-radius 2}
                          :on-key-press (fn [e]
                                          (let [native-event (.-nativeEvent e)
                                                key (.-key native-event)]
                                            (when (= key "Enter")
                                              (re-frame/dispatch [:create-desktop-account password]))))
                          :secure-text-entry true
                          :on-change   (fn [e]
                                         (let [native-event (.-nativeEvent e)
                                               text (.-text native-event)]
                                           (re-frame/dispatch [:set-in [:accounts/create :password-confirm] text])))}]
       [react/view {:style (merge st/bottom-actions-container {:margin-top 40})}
        (when (and (> (count password) 6) (= password password-confirm))
          [components/sticky-button "Create"  #(re-frame/dispatch [:create-desktop-account password])])]]]]))
