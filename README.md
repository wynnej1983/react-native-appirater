# react-native-appirater
react-native wrapper around ios app rating component

##Installation

First you need to install react-native-appirater:

```javascript
npm install react-native-appirater --save
```

### Installation (iOS)

In XCode, in the project navigator, right click Libraries ➜ Add Files to [your project's name] ➜ Go to node_modules ➜ react-native-appirater and add the .xcodeproj file

In XCode, in the project navigator, select your project. Add the lib*.a from the appirater project to your project's Build Phases ➜ Link Binary With Libraries. Then, click on the .xcodeproj file you added before in the project navigator, and go the Build Settings tab. Make sure 'All' is toggled on (instead of 'Basic'). Look for Header Search Paths and make sure it contains both $(SRCROOT)/../react-native/React and $(SRCROOT)/../../React - mark both as recursive.

### Installation (Android)
* android impl taken mostly from https://github.com/drewjw81/appirater-android

* In `android/settings.gradle`

```gradle
...
include ':RNAppirater'
project(':RNAppirater').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-appirater/android')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
  ...
  compile project(':RNAppirater')
}
```

* Register the module (in MainActivity.java)

```java
import com.wynnej1983.RNAppirater.*; // <--- import

public class MainActivity extends Activity {
  ...

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new RNAppirater()            //  <--- add here
      );
    }
    
    @Override  // <--- add this method, the next 4 lines
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      RNAppirater.appLaunched(this);
    }
  }
}
```

## Usage

### Usage (iOS)


### Usage (Android)

Copy the /res/values/appirater-settings.xml from the AppiraterAndroid library in to your projects /res/values/ folder and adjust the settings to your preference.

### Note on Usage

### Example

```javascript

//import module
import Appirater from 'react-native-appirater';

//set app id
`Appirater.setAppId(id);`
//register user significant event
`Appirater.userDidSignificantEvent();`

```
