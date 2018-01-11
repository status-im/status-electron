(ns status-desktop-front.react-native-hyperlink
  (:require [reagent.core :as reagent]))

(def react-native-hyperlink (js/require "react-native-hyperlink"))

(def hyperlink (reagent/adapt-react-class (.-default react-native-hyperlink)))