import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import EscPosSahaab from 'react-native-esc-pos-sahaab';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    console.log(EscPosSahaab.PRINTING_SIZE_58_MM);
    (async () => {
      try {
        await EscPosSahaab.connect('192.168.1.7', 9100);
        const design = `
D0004           {<>}           Table #: A1
------------------------------------------
[ ] Espresso
    - No sugar, Regular 9oz, Hot
                              {H3} {R} x 1
------------------------------------------
[ ] Blueberry Cheesecakex تشيز كيك
    - Slice
                              {H3} {R} x 1
 
{QR[Where are the aliens?]}
{BC[Your barcode here]}
`;

        EscPosSahaab.setCharCode('CP850');
        EscPosSahaab.setCharCodePage(50);

        // Once connected, you can setup your printing size, either `PRINTING_SIZE_58_MM`, `PRINTING_SIZE_76_MM` or `PRINTING_SIZE_80_MM`
        EscPosSahaab.setPrintingSize(EscPosSahaab.PRINTING_SIZE_80_MM);
        // 0 to 8 (0-3 = smaller, 4 = default, 5-8 = larger)
        EscPosSahaab.setTextDensity(8);
        // Test Print
        // await EscPosSahaab.printSample();
        // Cut half!
        await EscPosSahaab.cutPart();
        // You can also print image! eg. "file:///longpath/xxx.jpg"
        // await EscPosSahaab.printImage(file.uri);
        // You can also print image with a specific width offset (scale down image by offset pixels)! eg. "file:///longpath/xxx.jpg"
        // await EscPosSahaab.printImageWithOffset(file.uri, offset);
        // Print your design!
        await EscPosSahaab.printDesign(design);
        // Print QR Code, you can specify the size
        // await EscPosSahaab.printQRCode("Proxima b is the answer!", 200);
        // Print Barcode
        // printBarCode({code}, {type}, {width}, {height}, {font}, {fontPosition})
        // type: 65=UPC-A; 66=UPC-E; 67=EAN13; 68=EAN8; 69=CODE39; 70=ITF; 71=CODABAR; 72=CODE93; 73=CODE128}
        // width: 2-6
        // height: 0-255
        // font: 0=FontA; 1=FontB
        // fontPosition: 0=none; 1=top; 2=bottom; 3=top-bottom
        // await EscPosSahaab.printBarcode("Your barcode here", 73, 3, 100, 0, 2);
        // Cut full!
        await EscPosSahaab.cutFull();
        // Beep!
        await EscPosSahaab.beep();
        // Kick the drawer! Can be either `kickCashDrawerPin2` or `kickCashDrawerPin5`
        await EscPosSahaab.kickCashDrawerPin2();
        // Disconnect
        await EscPosSahaab.disconnect();
      } catch (e) {
        console.log(e);
      }
    })();

    EscPosSahaab.multiply(3, 7).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
