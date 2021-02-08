/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import EscPos from '@sahaab/react-native-esc-pos';

const App = () => {
  useEffect(() => {
    const design = `
D0004           {<>}           Table #: A1
------------------------------------------
[ ] Espresso
    - No sugar, Regular 9oz, Hot (وسخن)
      من غير سكر وعادى وسخن
                              {H3} {R} x 1
------------------------------------------
[ ] Blueberry Cheesecake
    - Slice
                              {H3} {R} x 1
`;

    async function testPrinter() {
      try {
        // Can be `network` or `bluetooth`
        EscPos.setConfig({type: 'network'});

        console.log('connecting to printer!');
        // Connects to your printer
        // If you use `bluetooth`, second parameter is not required.
        await EscPos.connect('192.168.1.87', 9100);

        // Once connected, you can setup your printing size, either `PRINTING_SIZE_58_MM`, `PRINTING_SIZE_76_MM` or `PRINTING_SIZE_80_MM`
        EscPos.setPrintingSize(EscPos.PRINTING_SIZE_80_MM);
        // 0 to 8 (0-3 = smaller, 4 = default, 5-8 = larger)
        EscPos.setTextDensity(8);
        // Test Print
        // await EscPos.printSample();
        // Cut half!
        // await EscPos.cutPart();
        // You can also print image! eg. "file:///longpath/xxx.jpg"
        // await EscPos.printImage(file.uri);
        // You can also print image with a specific width offset (scale down image by offset pixels)! eg. "file:///longpath/xxx.jpg"
        // await EscPos.printImageWithOffset(file.uri, offset);
        // Print your design!
        await EscPos.setCodePage(22);
        await EscPos.printDesign(design);
        // Print QR Code, you can specify the size
        // await EscPos.printQRCode('Proxima b is the answer!', 200);
        // Print Barcode
        // printBarCode({code}, {type}, {width}, {height}, {font}, {fontPosition})
        // type: 65=UPC-A; 66=UPC-E; 67=EAN13; 68=EAN8; 69=CODE39; 70=ITF; 71=CODABAR; 72=CODE93; 73=CODE128}
        // width: 2-6
        // height: 0-255
        // font: 0=FontA; 1=FontB
        // fontPosition: 0=none; 1=top; 2=bottom; 3=top-bottom
        // await EscPos.printBarcode('Your barcode here', 73, 3, 100, 0, 2);
        // Cut full!
        await EscPos.cutFull();
        // Beep!
        await EscPos.beep();
        // Kick the drawer! Can be either `kickCashDrawerPin2` or `kickCashDrawerPin5`
        await EscPos.kickCashDrawerPin2();
        // Disconnect
        await EscPos.disconnect();
      } catch (error) {
        console.error(error);
      }
    }
    testPrinter();
  }, []);

  return (
    <>
      <View>
        <Text>Hello</Text>
      </View>
      {/* <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          {global.HermesInternal == null ? null : (
            <View style={styles.engine}>
              <Text style={styles.footer}>Engine: Hermes</Text>
            </View>
          )}
          <View style={styles.body}>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Step One</Text>
              <Text style={styles.sectionDescription}>
                Edit <Text style={styles.highlight}>App.js</Text> to change this
                screen and then come back to see your edits.
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>See Your Changes</Text>
              <Text style={styles.sectionDescription}>
                <ReloadInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Debug</Text>
              <Text style={styles.sectionDescription}>
                <DebugInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Learn More</Text>
              <Text style={styles.sectionDescription}>
                Read the docs to discover what to do next:
              </Text>
            </View>
            <LearnMoreLinks />
          </View>
        </ScrollView>
      </SafeAreaView> */}
    </>
  );
};

// const styles = StyleSheet.create({
//   scrollView: {
//     backgroundColor: Colors.lighter,
//   },
//   engine: {
//     position: 'absolute',
//     right: 0,
//   },
//   body: {
//     backgroundColor: Colors.white,
//   },
//   sectionContainer: {
//     marginTop: 32,
//     paddingHorizontal: 24,
//   },
//   sectionTitle: {
//     fontSize: 24,
//     fontWeight: '600',
//     color: Colors.black,
//   },
//   sectionDescription: {
//     marginTop: 8,
//     fontSize: 18,
//     fontWeight: '400',
//     color: Colors.dark,
//   },
//   highlight: {
//     fontWeight: '700',
//   },
//   footer: {
//     color: Colors.dark,
//     fontSize: 12,
//     fontWeight: '600',
//     padding: 4,
//     paddingRight: 12,
//     textAlign: 'right',
//   },
// });

export default App;
