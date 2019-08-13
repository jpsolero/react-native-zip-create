# react-native-zip-create

## Getting started

`$ npm install react-native-zip-create --save`

### Mostly automatic installation

`$ react-native link react-native-zip-create`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-zip-create` and add `ZipCreate.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libZipCreate.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.ZipCreatePackage;` to the imports at the top of the file
  - Add `new ZipCreatePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-zip-create'
  	project(':react-native-zip-create').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-zip-create/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-zip-create')
  	```


## Usage
```javascript
import ZipCreate from 'react-native-zip-create';

// TODO: What to do with the module?
ZipCreate;
```
