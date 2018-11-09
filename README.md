# !!! THIS REPO IS OUTDATED AND NOT SUPPORTED 
You can find Status Desktop sources here https://github.com/status-im/react-native-desktop and here https://github.com/status-im/status-react



status-electron

Status ELECTRON (React Native Web and Electron)

Checkout `https://github.com/status-im/status-react/tree/feature/status-electron` branch into `status-react` folder 

You should have `status-react` and `status-electron` in the same directory

```
+-- status-dev-folder
|   +-- status-react // status-react repo (https://github.com/status-im/status-react) `feature/status-electron` branch
|   +-- status-electron // this repo

```

Make simlink to resources folder 

```
ln -s /Users/*/status-dev-folder/status-react/resources /Users/*/status-dev-folder/status-electron/resources
```

## Requirements

* leiningen 2.6.x +
* node v8.x.x (important to have exactly 8 version not 7 and not 9)
* electron v1.8.2-beta.3 +


## Project Directory

  see your app dir. looks like

```
.
+-- README.md
+-- app
|   +-- dev // development mode dir
|   |   +-- index.html // entry html file
|   |   +-- js
|   |   |   +-- main.js
|   |   +-- package.json // for Desktop app
|   +-- prod // production mode dir
|       +-- index.html // entry html file
|       +-- js
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
       |   +-- init.cljs
       +-- prod
           +-- init.cljs
```

## Usage

### step 1

Install electron If not already installed

`npm install -g electron@beta`

Install npm modules

`npm install`

Check node version

`node -v` it should be 8.x.x if not switch to 8.x.x version `n 8.9.1`

### step 2

run cljsbuild `lein desktop-once`.


```
$ lein desktop-once

Compiling ClojureScript.
Compiling "app/js/cljsbuild-main.js" from ["src"]...
Successfully compiled "app/js/cljsbuild-main.js" in 10.812 seconds.
...
Successfully compiled "app/dev/js/front.js" in 10.588 seconds.
```


### step 3

You can run Desktop application.

#### development mode

development mode use figwheel. run alias `desktop-figwheel`.  before run application.
Open other terminal window.

```
$ lein desktop-figwheel
```

and you can run Electron(Atom-Shell) app.

On OS X:

```
$ electron app/dev
```

On Linux:

```
$ ./electron/electron app/dev
```

On Windows:

```
$ .\electron\electron.exe app/dev
```


#### production mode

you can run Electron(Atom-Shell) app.

On OS X:

```
$ electron app/prod
```

On Linux:

```
$ ./electron/electron app/prod
```

On Windows:

```
$ .\electron\electron.exe app/prod
```


## Package App

### (If not already installed Electron-packager.)

```
$ npm install -g electron-packager
```

### run command

#### for OSX

```
$ lein desktop-app-osx
```

#### for OSX app store

```
$ desktop-app-store
```

#### for windows 32bit app

```
$ desktop-app-win32
```

#### for windows 64bit app

```
$ desktop-app-win64
```

#### for linux

```
$ desktop-app-linux
```


## Aliases

you can use aliases in project directory.

```
$ lein desktop-figwheel      # start figwheel
$ lein desktop-once          # build JavaScript for develop 
$ lein desktop-prod          # build JavaScript for production
```

### Contact us
 
 Feel free to email us at [support@status.im](mailto:support@status.im) or better yet, [join our Riot](http://chat.status.im/#/register).
 
## License
 
Licensed under the [Mozilla Public License v2.0](https://github.com/status-im/status-react/blob/develop/LICENSE.md)
