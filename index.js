import ReactNative from 'react-native'

const {
  NativeEventEmitter,
  NativeModules
} = ReactNative

const { ZipCreate } = NativeModules;

export const zip = (source, target) => {
  return ZipCreate.zip(source, target)
}

const zipCreateEmitter = new NativeEventEmitter(ZipCreate);


export const subscribe = callback => {
  return calendarManagerEmitter.addListener(
    'EventReminder', callback
  );
}
