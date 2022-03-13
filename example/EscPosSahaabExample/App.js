/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';
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

import RNFetchBlob from 'rn-fetch-blob';

import EscPos from '@sahaab/react-native-esc-pos';

const App = () => {
  const [devices, setDevices] = useState([]);

  useEffect(() => {
    const design = `
{B}{H2}{C} 15
{C} Invoice No.:27022021852015
{C}test
{C} Restaurant sahaab
{C}TRN 000287764
{C} Phone 068869081
--------------------------------------------
#15 - kassim - 02/27/2021 - 8:20 pm
Drivethru - DRIVETHRU
=========================================
ITEM{<>}QTY - TOTAL
=========================================
Pizza Beef{<>}1 x 22
PIzza بيتزا لحم
حواوشى50 #70 حمص$50
محمره{<>}1 x222
محمرة هيييه
احلي مسى عليك
------------------------------------------
Krisby{<>}1 x 23
كرسبي
  salad{<>}1 x 8
=========================================
SubTotal{<>}53
Grand Total{B}{<>}53
Cash(cash){<>} 53
      Payed Amount        {<>}         55
      Remainder Amount      {<>}          2
{C}{B}<Customer Copy>
{C}Thank You .. Visit Again
لوريم ايبسوم دولار سيت أميت ,كونسيكتيتور أدايبا يسكينج أليايت,سيت دو أيوسمود تيمبور

أنكايديديونتيوت لابوري ات دولار ماجنا أليكيوا . يوت انيم أد مينيم فينايم,كيواس نوستريد

أكسير سيتاشن يللأمكو لابورأس نيسي يت أليكيوب أكس أيا كوممودو كونسيكيوات . ديواس

أيوتي أريري دولار إن ريبريهينديرأيت فوليوبتاتي فيلايت أيسسي كايلليوم دولار أيو فيجايت

نيولا باراياتيور. أيكسسيبتيور ساينت أوككايكات كيوبايداتات نون بروايدينت ,سيونت ان كيولبا

كيو أوفيسيا ديسيريونتموليت انيم أيدي ايست لابوريوم.

سيت يتبيرسبايكياتيس يوندي أومنيس أستي ناتيس أيررور سيت فوليبتاتيم أكيسأنتييوم

دولاريمكيو لايودانتيوم,توتام ريم أبيرأم,أيكيو أبسا كيواي أب أللو أنفينتوري فيرأتاتيس ايت

كياسي أرشيتيكتو بيتاي فيتاي ديكاتا سيونت أكسبليكابو. نيمو أنيم أبسام فوليوباتاتيم كيواي

فوليوبتاس سايت أسبيرناتشر أيوت أودايت أيوت فيوجايت, سيد كيواي كونسيكيونتشر ماجناي

دولارس أيوس كيواي راتاشن فوليوبتاتيم سيكيواي نيسكايونت. نيكيو بوررو كيوايسكيوم

ايست,كيواي دولوريم ايبسيوم كيوا دولار سايت أميت, كونسيكتيتيور,أديبايسكاي فيلايت, سيد

كيواي نون نيومكيوام ايايوس موداي تيمبورا انكايديونت يوت لابوري أيت دولار ماجنام

ألايكيوام كيوايرات فوليوبتاتيم. يوت اينايم أد مينيما فينيام, كيواس نوستريوم أكسيركايتاشيم

يلامكوربوريس سيوسكايبيت لابورايوسام, نايساي يوت ألايكيوايد أكس أيا كوموداي

كونسيكيواتشر؟ كيوايس أيوتيم فيل أيوم أيوري ريبريهينديرايت كيواي ان إيا فوليوبتاتي

فيلايت ايسسي كيوم نايهايل موليستايا كونسيكيواتيو,فيلايليوم كيواي دولوريم أيوم فيوجايات كيو

فوليوبتاس نيولا باراياتيور؟

أت فيرو ايوس ايت أكيوساميوس ايت أيوستو أودايو دايجنايسسايموس ديوكايميوس كيواي

بلاندايتاييس برايسينتايوم فوليوبتاتيوم ديلينايتاي أتكيوي كورريوبتاي كيوأوس دولوريس أيت

سيما يليكيوسيونت ان كيولبا كيواي أوفايكيا ديسيريونت موللايتايا انايماي, أيدي ايست لابوريوم

دايستا ينستايو. نام لايبيرو تيمبور, كيوم سوليوتا نوبايس ايست ايلاجينداي أوبتايو كيومكيوي

نايهايل ايمبيدايت كيو ماينيوس ايدي كيوود ماكسهيمي بلاسايت فاسيري بوسسايميوس,أومنايس

فوليوبتاس ايت ايوت أسسيو ميندايست, أومنيوس دولار ريبيللينديوس. تيمبورايبيوس أيوتيم

كيواس موليستاياس أكسكيبتيوراي ساينت أوككايكاتاي كيبايدايتات نون بروفايدنت

أيت دولوريوم فيوجا.ايت هاريوم كيوايديم ريريوم فاكايلايسايست ايت أكسبيدايتا

كيوايبيوسدام ايت أوت

أوففايكايس ديبايتايس أيوت ريريوم نيسيسسايتاتايبيوس سايبي ايفينايت يوت ايت فوليبتاتيس 

ريبيودايايانداي ساينت ايت موليسفاياي نون ريكيوسانداي.اتاكيوي ايريوم ريريوم هايس تينيتور

أ ساباينتي ديليكتيوس, يوت أيوت رياسايندايس فوليوبتاتايبص مايوريس ألايس

كونسيكيواتور أيوت بيرفيريندايس دولورايبيوس أسبيرايوريس ريبيللات .
`;

    async function testPrinter() {
      try {
        // Can be `network` or `bluetooth`
        EscPos.setConfig({type: 'network'});
        console.log('connecting to printer!');
        // Connects to your printer
        // If you use `bluetooth`, second parameter is not required.
        await EscPos.connect('192.168.1.7', 9100);
        await EscPos.setCodePage(32);
        EscPos.setPrintingSize(EscPos.PRINTING_SIZE_80_MM);

        // EscPos.write([27, 97, 1]);
        const config = RNFetchBlob.config({
          fileCache: true,
        });
        const res = await config.fetch(
          'GET',
          'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQh3mstrq4LV_rFJTBHvZNA6xMvVUpN_9nDzg&usqp=CAU',
        );
        if (res) {
          await EscPos.printImage(res.path());
        }
        // EscPos.print('\n');
        // EscPos.write([27, 97, 0]);
        // Once connected, you can setup your printing size, either `PRINTING_SIZE_58_MM`, `PRINTING_SIZE_76_MM` or `PRINTING_SIZE_80_MM`
        // EscPos.setPrintingSize(EscPos.PRINTING_SIZE_80_MM);
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
        await EscPos.setCodePage(14);
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
    EscPos.scanDevices();
    EscPos.addListener('bluetoothDeviceFound', (event) => {
      // if (event.state === EscPos.BLUETOOTH_DEVICE_FOUND) {
      console.log('Device Found!');
      console.log('Device Name : ' + event.deviceInfo.name);
      console.log('Device MAC Address : ' + event.deviceInfo.macAddress);
      setDevices((devices) => [...devices, ...event.deviceInfo]);
      // }
    });
    EscPos.addListener('bluetoothStateChanged', (event) => {
      if (event.state === EscPos.BLUETOOTH_CONNECTED) {
        console.log('Device Connected!');
        console.log('Device Name : ' + event.deviceInfo.name);
        console.log('Device MAC Address : ' + event.deviceInfo.macAddress);
        setDevices((devices) => [...devices, ...event.deviceInfo]);
      }
    });
  }, []);

  return (
    <>
      <View>
        <Text>Hello</Text>
        {devices.map((d) => (
          <Text key={d.macAddress}>{d.name}</Text>
        ))}
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
