import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-instant-clip-sync' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const InstantClipSync = NativeModules.InstantClipSync
  ? NativeModules.InstantClipSync
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function save(key: string, value: string) {
  return InstantClipSync.storeStringInInstantAppCookie(`${key}:${value}`);
}

export function get(key?: string) {
  return new Promise(async (resolve, reject) => {
    try {
      const raw =
        (await InstantClipSync.retrieveStringInInstantAppCookie()) as string;
      if (!key) return resolve(raw);
      resolve(
        raw
          .split(';')
          .find((pair) => pair.indexOf(`${key}:`) !== -1)
          ?.replace(`${key}:`, '')
      );
    } catch (error) {
      reject(error);
    }
  });
}
