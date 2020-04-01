const merge                   = require('webpack-merge');
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const TerserPlugin            = require('terser-webpack-plugin');
const ReplaceInFilePlugin     = require('replace-in-file-webpack-plugin');
const common                  = require('./webpack.amp.js');

module.exports = merge(common, {
   mode: 'production',
   optimization: {
      minimizer: [
          new TerserPlugin(),
          new OptimizeCSSAssetsPlugin({
              cssProcessorPluginOptions: {
                  cssProcessor: require('cssnano'),
                  preset: ['default', {
                      calc: true,
                      convertValues: true,
                      discardComments: {
                          removeAll: true
                      },
                      discardDuplicates: true,
                      discardEmpty: true,
                      mergeRules: true,
                      normalizeCharset: true,
                      reduceInitial: true, // This is since IE11 does not support the value Initial
                      svgo: true
                  }],
              },
              canPrint: false
          })
      ]
   },
   plugins: [
       new ReplaceInFilePlugin([{
           dir: 'dist-amp/clientlib-amp/css/',
           files: ['site.bundle.css'],
           rules: [{
               search: '@charset "utf-8";',
               replace: ''
           }]
       }])
   ],
   devtool: 'none',
   performance: {hints: false}
});