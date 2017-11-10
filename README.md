# status-desktop

This project build by descjop v0.7.0

Status Desktop (React Native Web and Electron)

You should have status-react and status-electron in the same directory

```
+-- status-dev-folder
|   +-- status-react // status-react repo
|   +-- status-electron // this repo

```


## Requirements

* leiningen 2.6.x +
* node v0.12.x +
* grunt v0.1.13 +

### (if you don't install grunt yet.)

```
$ npm install -g grunt-cli
```


## Project Directory

  see your app dir. looks like

```
.
+-- Gruntfile.js
+-- README.md
+-- app
|   +-- dev // deveropment mode dir
|   |   +-- index.html // entry html file
|   |   +-- js
|   |   |   +-- externs_front.js
|   |   |   +-- externs.js
|   |   |   +-- main.js
|   |   +-- package.json // for Desktop app
|   +-- prod // production mode dir
|       +-- index.html // entry html file
|       +-- js
|       |   +-- externs_front.js
|       |   +-- externs.js
|       |   +-- main.js
|       +-- package.json // for Desktop app
+-- package.json // for Compile
+-- project.clj // compile settings desktop app
+-- resources
+-- src
|   +-- status_desktop
|       +-- core.cljs // ClojureScript for Electron in here
+-- src_front
|   +--status_desktop_front
|      +-- core.cljs //  Status ClojureScript enter point in here
+-- src_front_profile
    +--status_desktop_front
       +-- dev
       |   +-- conf.cljs
       |   +-- init.cljs
       +-- prod
           +-- conf.cljs
           +-- init.cljs
```

## Usage

### step 1

run `descjop-init` (windows user should use `descjop-init-win`) alias below.

#### OSX/Linux user

```
$ lein descjop-init
 ...
 
Running "download-electron" task
 
Done, without errors.
```

#### Windows user

```
$ lein descjop-init-win
 ...
 
Running "download-electron" task
 
Done, without errors.
```


### step 2

and run extern alias `descjop-externs`,

```
$ lein descjop-externs
```

run cljsbuild `lein descjop-once`.

```
$ lein descjop-once

Compiling ClojureScript.
Compiling "app/js/cljsbuild-main.js" from ["src"]...
Successfully compiled "app/js/cljsbuild-main.js" in 10.812 seconds.
...
Successfully compiled "app/dev/js/front.js" in 10.588 seconds.
...
Successfully compiled "app/prod/js/cljsbuild-main.js" in 19.333 seconds.
...
Successfully compiled "app/prod/js/front.js" in 29.94 seconds.
```

### step 3

You can run Desctop application.

#### development mode

development mode use figwheel. run alias `descjop-figwheel`.  before run application.
Open other terminal window.

```
$ lein descjop-figwheel
```

and you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\electron\electron.exe app/dev
```

On Linux:

```
$ ./electron/electron app/dev
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app/dev
```

#### production mode

you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\electron\electron.exe app/prod
```

On Linux:

```
$ ./electron/electron app/prod
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app/prod
```

## Package App

### (If not already installed Electron-packager.)

```
$ npm install -g electron-packager
```

### run command

#### for OSX

```
$ lein descjop-uberapp-osx
```

#### for OSX app store

```
$ descjop-uberapp-app-store
```

#### for windows 32bit app

```
$ descjop-uberapp-win32
```

#### for windows 64bit app

```
$ descjop-uberapp-win64
```

#### for linux

```
$ descjop-uberapp-linux
```

## How to Upgrade to new Electron version

You can change Electron version in Gruntfile.js.

```
module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        "download-electron": {
            version: "1.3.2", // change Electron version 1.3.2 -> 1.3.3
            outputDir: "./electron", 
            rebuild: true
        }
    });

    grunt.loadNpmTasks('grunt-download-electron');

};
```

and re-run

for linux / mac

```
$ lein descjop-init
```

for windows

```
$ lein descjop-init-win
```

## Aliases

you can use aliases in project directory.

```
$ lein descjop-version       # descjop version
$ lein descjop-help          # descjop help
$ lein descjop-init          # init project
$ lein descjop-init-win      # init project for windows user
$ lein descjop-externs       # output externs for develop and production
$ lein descjop-externs-dev   # output externs for develop
$ lein descjop-externs-prod  # output externs for production
$ lein descjop-figwheel      # start figwheel
$ lein descjop-once          # build JavaScript for develop and production
$ lein descjop-once-dev      # build JavaScript for develop
$ lein descjop-once-prod     # build JavaScript for production
```

## License

Copyright ©  FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
