(defproject status-desktop "0.0.1-SNAPSHOT"
  :description "Status Desktop (React Native Web and Electron)"
  :url "https://github.com/status-im/status-electron"
  :license {:name "Mozilla Public License v2.0"
            :url "https://github.com/status-im/status-react/blob/develop/LICENSE.md"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/clojurescript "1.9.908"]
                 [org.clojure/core.async "0.3.443"]
                 [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server cljsjs/create-react-class]]
                 [re-frame "0.10.1"]
                 [alandipert/storage-atom "2.0.1"]
                 [com.andrewmcveigh/cljs-time "0.5.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [ring/ring-core "1.5.1"]
                 [figwheel "0.5.14"]
                 [hickory "0.7.1"]
                 [re-frisk-remote "0.5.3"]]
  :plugins [[lein-re-frisk "0.5.5"]
            [lein-cljsbuild "1.1.5"]
            [lein-externs "0.1.6"]
            [lein-shell "0.5.0"]
            [lein-figwheel "0.5.14" :exclusions [org.clojure/core.cache]]]
  :source-paths ["src_tools"]
  :aliases {"desktop-figwheel" ["trampoline" "figwheel" "dev-front"]
            "desktop-once" ["do"
                            ["cljsbuild" "once" "dev-main"]
                            ["cljsbuild" "once" "dev-front"]]
            "desktop-prod" ["do"
                            ["cljsbuild" "once" "prod-main"]
                            ["cljsbuild" "once" "prod-front"]]
            "desktop-prod-qt" ["do"
                            ["cljsbuild" "once" "prod-front-qt"]]
            "desktop-qt-dev-figwheel" ["trampoline" "figwheel" "dev-front-qt"]

            ;; electron packager for production
            "desktop-app-osx"   ["shell" "electron-packager" "./app/prod" "Status" "--platform=darwin" "--arch=x64" "--electron-version=1.8.2-beta.3" "--extraResource=./node_modules" "--icon=assets/icon1024.icns"]
            "desktop-app-linux" ["shell" "electron-packager" "./app/prod" "Status" "--platform=linux" "--arch=x64" "--electron-version=1.8.2-beta.3" "--extraResource=./node_modules" "--icon=assets/icon1024.png"]

            "desktop-app-store" ["shell" "electron-packager" "./app/prod" "Status" "--platform=mas" "--arch=x64" "--electron-version=1.8.2-beta.3" "--extraResource=./node_modules"]
            "desktop-app-win64" ["shell" "cmd.exe" "/c" "electron-packager" "./app/prod" "Status" "--platform=win32" "--arch=x64" "--electron-version=1.8.2-beta.3" "--extraResource=./node_modules"]
            "desktop-app-win32" ["shell" "cmd.exe" "/c" "electron-packager" "./app/prod" "Status" "--platform=win32" "--arch=ia32" "--electron-version=1.8.2-beta.3" "--extraResource=./node_modules"]}

  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds {:dev-main {:source-paths ["src"]
                                  :incremental true
                                  :jar true
                                  :assert true
                                  :compiler {:output-to "app/dev/js/cljsbuild-main.js"
                                             :warnings true
                                             :elide-asserts true
                                             :target :nodejs

                                             ;; no optimize compile (dev)
                                             ;;:optimizations :none
                                             :output-dir "app/dev/js/out_main"

                                             ;; simple compile (dev)
                                             :optimizations :simple

                                             ;; advanced compile (prod)
                                             ;;:optimizations :advanced

                                             ;;:source-map "app/dev/js/test.js.map"
                                             :pretty-print true
                                             :output-wrapper true}}
                       :dev-front {:source-paths ["src_front" "src_front_profile/dev"
                                                  "../status-react/src"]
                                   :incremental true
                                   :jar true
                                   :assert true
                                   :compiler {:output-to "app/dev/js/front.js"
                                              :warnings true
                                              :elide-asserts true

                                              ;; no optimize compile (dev)
                                              :optimizations :none
                                              :output-dir "app/dev/js/out_front"

                                              ;; simple compile (dev)
                                              ;;:optimizations :simple

                                              ;; advanced compile (prod)
                                              ;;:optimizations :advanced

                                              ;;:source-map "app/dev/js/test.js.map"
                                              :pretty-print true
                                              :output-wrapper true}}
                       :prod-main {:source-paths ["src"]
                                   :incremental true
                                   :jar true
                                   :assert true
                                   :compiler {:output-to "app/prod/js/cljsbuild-main.js"
                                              :warnings true
                                              :elide-asserts true
                                              :target :nodejs

                                              ;; no optimize compile (dev)
                                              ;;:optimizations :none
                                              :output-dir "app/prod/js/out_main"
                                              ;; simple compile (dev)
                                              :optimizations :simple

                                              ;; advanced compile (prod)
                                              ;;:optimizations :advanced

                                              ;;:source-map "app/prod/js/test.js.map"
                                              :pretty-print true
                                              :output-wrapper true}}
                       :prod-front {:source-paths ["src_front" "src_front_profile/prod"
                                                   "../status-react/src"]
                                    :incremental true
                                    :jar true
                                    :assert true
                                    :compiler {:output-to "app/prod/js/front.js"
                                               :warnings true
                                               :elide-asserts true
                                               ;; :target :nodejs

                                               ;; no optimize compile (dev)
                                               :optimizations :none
                                               :output-dir "app/prod/js/out_front"

                                               ;; simple compile (dev)
                                               ;;:optimizations :simple

                                               ;; advanced compile (prod)
                                               ;;:optimizations :advanced

                                               ;;:source-map "app/prod/js/test.js.map"
                                               :pretty-print true
                                               :output-wrapper true}}
                      :dev-front-qt {:source-paths ["src_front" "src_front_profile/dev_qt"  "../status-react/src"]
                                   :compiler {:output-to "target/desktop/not-used.js"
                                               :main     "status-desktop-front.init"
                                               ;; no optimize compile (dev)
                                               :output-dir "target/desktop"

                                               :optimizations :none}}
                       :prod-front-qt {:source-paths ["src_front" "src_front_profile/prod_qt"
                                                   "../status-react/src"]
                                    :compiler {:output-to     "index.desktop.js"
                                               ;:warnings true
                                               ;:elide-asserts true
                                               :optimizations :simple
                                               :output-dir "app/prod-qt/out_front"
                                               ;:pretty-print true
                                               ;:output-wrapper true
                                               :main          "status-desktop-front.init"
                                               :static-fns    true
                                               :optimize-constants true
                                               :closure-defines {"goog.DEBUG" false}
                                               }}}}
  :figwheel {:http-server-root "public"
             :ring-handler figwheel-middleware/app
             :server-port 3449})
