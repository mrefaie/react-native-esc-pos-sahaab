import { DeviceEventEmitter, NativeModules } from 'react-native';

type EscPosSahaabType = {
  PRINTING_SIZE_58_MM: string;
  PRINTING_SIZE_76_MM: string;
  PRINTING_SIZE_80_MM: string;
  BLUETOOTH_CONNECTED: string;
  BLUETOOTH_DISCONNECTED: string;
  BLUETOOTH_DEVICE_FOUND: string;
  cutPart(): void;
  cutFull(): void;
  print(text: string): void;
  printLn(text: string): void;
  printBarcode(
    code: string,
    bc: string,
    width: number,
    height: number,
    pos: string,
    font: string
  ): void;
  printBarcode(
    str: string,
    nType: number,
    nWidthX: number,
    nHeight: number,
    nHriFontType: number,
    nHriFontPosition: number
  ): void;
  printDesign(text: string): void;
  printImage(filePath: string): void;
  printImageWithOffset(filePath: string, widthOffet: number): void;
  printQRCode(value: string, size: number): void;
  printSample(): void;
  write(command: object): void;
  setCharCode(code: string): void;
  setCharCodePage(page: number): void;
  setTextDensity(density: number): void;
  setPrintingSize(printingSize: string): void;
  beep(): void;
  kickCashDrawerPin2(): void;
  kickCashDrawerPin5(): void;
  connectBluetoothPrinter(address: string): void;
  connectNetworkPrinter(address: string, port: number): void;
  disconnect(): void;
  scanDevices(): void;
  stopScan(): void;
  addListener(eventName: string, cb: any): void;
  connect(address: string, port: number): void;
  multiply(a: number, b: number): Promise<number>;
};

const EscPosSahaab = {
  ...NativeModules.EscPosSahaab,
  addListener(eventName: string, cb: any) {
    switch (eventName) {
      case 'bluetoothStateChanged':
        EscPosSahaab.initBluetoothConnectionListener();
        DeviceEventEmitter.addListener('bluetoothStateChanged', cb);
        break;

      case 'bluetoothDeviceFound':
        DeviceEventEmitter.addListener('bluetoothDeviceFound', cb);
        break;

      default:
        throw new Error(`${eventName} is not a valid event name.`);
    }
  },
  // TODO: What is the best way to add optional arguments to @ReactMethod? overloading doesn seem to be working??
  connect(address: string, port: number) {
    if (!port) {
      NativeModules.EscPosSahaab.setConfig({ type: 'bluetooth' });
      return NativeModules.EscPosSahaab.connectBluetoothPrinter(address);
    } else {
      NativeModules.EscPosSahaab.setConfig({ type: 'network' });
      return NativeModules.EscPosSahaab.connectNetworkPrinter(address, port);
    }
  },
};

export default EscPosSahaab as EscPosSahaabType;
