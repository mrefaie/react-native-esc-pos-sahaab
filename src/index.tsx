import { NativeModules } from 'react-native';

type EscPosSahaabType = {
  multiply(a: number, b: number): Promise<number>;
};

const { EscPosSahaab } = NativeModules;

export default EscPosSahaab as EscPosSahaabType;
