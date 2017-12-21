(ns status-desktop-front.ui.screens.accounts.styles
  (:require-macros [status-im.utils.styles :refer [defnstyle defstyle]]))

(def account-title-text
  {:color          :white
   :margin-top     22
   :text-align     :center
   :font-size      18
   :line-height    24
   :letter-spacing -0.2})

(def account-item-view
  {:width            290
   :height           64
   :border-radius    8
   :justify-content  :center
   :background-color :white
   :box-shadow       "0 2px 6px 0 rgba(0, 0, 0, 0.25)"})