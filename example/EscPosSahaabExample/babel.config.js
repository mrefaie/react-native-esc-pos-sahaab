const path = require('path');
const pak = require('../../package.json');

module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: [
    [
      'module-resolver',
      {
        alias: {
          [pak.name]: (() => {
            console.log(path.join(__dirname, '/../../', pak.source));
            return path.join(__dirname, '/../../', pak.source);
          })(),
        },
      },
    ],
  ],
};
