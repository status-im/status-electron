(defproject status-desktop "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/clojurescript "1.9.908"]
                 [org.clojure/core.async "0.3.443"]
                 [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server cljsjs/create-react-class]]
                 [re-frame "0.10.1"]
                 [com.andrewmcveigh/cljs-time "0.5.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [ring/ring-core "1.5.1"]
                 [figwheel "0.5.14"]
                 [hickory "0.7.1"]
                 [re-frisk-remote "0.5.1"]]
  :plugins [[lein-re-frisk "0.5.2"]
            [lein-cljsbuild "1.1.5"]
            [lein-externs "0.1.6"]
            [lein-shell "0.5.0"]
            [lein-figwheel "0.5.14" :exclusions [org.clojure/core.cache]]]
  :source-paths ["src_tools"]
  :aliases {"descjop-help" ["new" "descjop" "help"]
            "descjop-version" ["new" "descjop" "version"]
            "descjop-init" ["do"
                            ["shell" "npm" "install"]
                            ["shell" "grunt" "download-electron"]]
            "descjop-init-win" ["do"
                                ["shell" "cmd.exe" "/c" "npm" "install"]
                                ["shell" "cmd.exe" "/c" "grunt" "download-electron"]]
            "descjop-externs" ["do"
                               ["externs" "dev-main" "app/dev/js/externs.js"]
                               ["externs" "dev-front" "app/dev/js/externs_front.js"]
                               ["externs" "prod-main" "app/prod/js/externs.js"]
                               ["externs" "prod-front" "app/prod/js/externs_front.js"]]
            "descjop-externs-dev" ["do"
                                   ["externs" "dev-main" "app/dev/js/externs.js"]
                                   ["externs" "dev-front" "app/dev/js/externs_front.js"]]
            "descjop-externs-prod" ["do"
                                    ["externs" "prod-main" "app/prod/js/externs.js"]
                                    ["externs" "prod-front" "app/prod/js/externs_front.js"]]
            "descjop-figwheel" ["trampoline" "figwheel" "dev-front"]
            "descjop-once" ["do"
                            ["cljsbuild" "once" "dev-main"]
                            ["cljsbuild" "once" "dev-front"]
                            ["cljsbuild" "once" "prod-main"]
                            ["cljsbuild" "once" "prod-front"]]
            "descjop-once-dev" ["do"
                                ["cljsbuild" "once" "dev-main"]
                                ["cljsbuild" "once" "dev-front"]]
            "descjop-once-prod" ["do"
                                 ["cljsbuild" "once" "prod-main"]
                                 ["cljsbuild" "once" "prod-front"]]
            ;; electron packager for production
            "descjop-uberapp-osx" ["shell" "electron-packager" "./app/prod" "status-desktop" "--platform=darwin" "--arch=x64" "--electron-version=1.6.6"]
            "descjop-uberapp-app-store" ["shell" "electron-packager" "./app/prod" "status-desktop" "--platform=mas" "--arch=x64" "--electron-version=1.6.6"]
            "descjop-uberapp-linux" ["shell" "electron-packager" "./app/prod" "status-desktop" "--platform=linux" "--arch=x64" "--electron-version=1.6.6"]
            "descjop-uberapp-win64" ["shell" "cmd.exe" "/c" "electron-packager" "./app/prod" "status-desktop" "--platform=win32" "--arch=x64" "--electron-version=1.6.6"]
            "descjop-uberapp-win32" ["shell" "cmd.exe" "/c" "electron-packager" "./app/prod" "status-desktop" "--platform=win32" "--arch=ia32" "--electron-version=1.6.6"]}

  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds {:dev-main {:source-paths ["src"]
                                  :incremental true
                                  :jar true
                                  :assert true
                                  :compiler {:output-to "app/dev/js/cljsbuild-main.js"
                                             :externs ["app/dev/js/externs.js"
                                                       "node_modules/closurecompiler-externs/path.js"
                                                       "node_modules/closurecompiler-externs/process.js"]
                                             :warnings true
                                             :elide-asserts true
                                             :target :nodejs

                                             ;; no optimize compile (dev)
                                             :optimizations :none
                                             :output-dir "app/dev/js/out_main"

                                             ;; simple compile (dev)
                                             ;;:optimizations :simple

                                             ;; advanced compile (prod)
                                             ;;:optimizations :advanced

                                             ;;:source-map "app/dev/js/test.js.map"
                                             :pretty-print true
                                             :output-wrapper true}}
                       :dev-front {:source-paths ["src_front" "src_front_profile/status_desktop_front/dev"
                                                  "../status-react/src"]
                                   :incremental true
                                   :jar true
                                   :assert true
                                   :compiler {:output-to "app/dev/js/front.js"
                                              :externs ["app/dev/js/externs_front.js"]
                                              :warnings true
                                              :elide-asserts true
                                              ;; :target :nodejs

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
                                              :externs ["app/prod/js/externs.js"
                                                        "node_modules/closurecompiler-externs/path.js"
                                                        "node_modules/closurecompiler-externs/process.js"]
                                              :warnings true
                                              :elide-asserts true
                                              :target :nodejs

                                              ;; no optimize compile (dev)
                                              :optimizations :none
                                              :output-dir "app/prod/js/out_main"

                                              ;; simple compile (dev)
                                              ;;:optimizations :simple

                                              ;; advanced compile (prod)
                                              ;;:optimizations :advanced

                                              ;;:source-map "app/prod/js/test.js.map"
                                              :pretty-print true
                                              :output-wrapper true}}
                       :prod-front {:source-paths ["src_front" "src_front_profile/status_desktop_front/prod"
                                                   "../status-react/src"]
                                    :incremental true
                                    :jar true
                                    :assert true
                                    :compiler {:output-to "app/prod/js/front.js"
                                               :externs ["app/prod/js/externs_front.js"]
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
                                               :output-wrapper true}}}}
  :figwheel {:http-server-root "public"
             :ring-handler figwheel-middleware/app
             :server-port 3449})
