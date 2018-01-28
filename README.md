# status-desktop

Status Desktop (Based on react-native-desktop)

## Requirements

* leiningen 2.6.x +
* node v8.x.x (important to have exactly 8 version not 7 and not 9)
* Qt 5.9 (https://www.qt.io/download-qt-installer)


## Prepare project directory

#### Create folder for code:
```
mkdir status-dev-folder
cd status-dev-folder/
```

#### Clone react-native-desktop
`git clone https://github.com/status-im/react-native-desktop.git`


#### Clone status-react repository, `feature/status-electron` branch:
```
git clone https://github.com/status-im/status-react.git
cd status-react
git checkout feature/status-electron
```

#### Clone status-electron repository, `master-react-native-qt` branch:
```
cd ..
git clone https://github.com/status-im/status-electron.git
cd status-electron
git checkout master-react-native-qt
```

#### Now your directory structure is:

  ```
+-- status-dev-folder
|   +-- react-native-desktop   // `react-native-qt` branch
|   +-- status-react           // `feature/status-electron` branch
|   +-- status-electron        // `master-react-native-qt` branch

```

## Prepare project for compilation

#### Install `react-native-cli`
React native desktop uses extended version of `react-native-cli` and you need to install it.

Uninstall previous version (if needed)
`npm uninstall -g react-native-cli`

Install desktop version
In `status-electron` folder:
```
cd react-native-desktop/react-native-cli
npm install -g
```

#### Install node modules for `status-electron`
```
cd ../../status-electron
npm install
```

#### Generate desktop project
`react-native desktop`


#### Specify Qt location
Now you have `status-electron/desktop` project that contains files necessary for building desktop app.
To build qt project you have to specify where your Qt installed. You can make this in file `status-electron/desktop/build.sh` by updating cmake invokation like this:

`cmake -DCMAKE_BUILD_TYPE=Debug -DCMAKE_PREFIX_PATH=/PATH_TO_QT_INSTALLATION/5.9/clang_64/ -DEXTERNAL_MODULES_DIR="$externalModulesPaths" . && make && cp ./bin/StatusDesktop click/`

#### Compile project
Result of compilation is `index.desktop.js` file. After previous command you already have basic `index.desktop.js`, so we need to remove it and invoke compilation: 
```
rm index.desktop.js
lein desktop-prod-qt
```

## Run app

To run the desktop app you need 3 terminal windows (assuming all are open at `status-dev-folder/status-electron`):
1) In the first you need to run react-native bundler:

`npm start`

2) In the second you need to run `ubuntu-server.js`. It is a process where runs javascript code invoked by desktop project.

`cd node_modules/react-native`
`./ubuntu-server.js`

3) In third terminal we are running the desktop app:

`react-native run-desktop`

The desktop app will start. It will request javascript code from bundler, so please wait until bundler is ready.
When you modified clojurescript code and recompiled it to javascript, you can reload app without restarting. Call developer menu `Cmd+R` and select `Reload`


### Contact us
 
 Feel free to email us at [support@status.im](mailto:support@status.im) or better yet, [join our Riot](http://chat.status.im/#/register).
 
## License
 
Licensed under the [Mozilla Public License v2.0](https://github.com/status-im/status-react/blob/develop/LICENSE.md)
