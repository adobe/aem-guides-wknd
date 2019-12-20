module.exports = {
    // default working directory (can be changed per 'cwd' in every asset option)
    context: __dirname,

    // path to the clientlib root folder (output)
    clientLibRoot: "./../ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs",

    libs: [
        {
            name: "clientlib-site",
            allowProxy: true,
            categories: ["wknd.site"],
            serializationFormat: "xml",
            cssProcessor : ["default:none", "min:none"],
            jsProcessor: ["default:none", "min:none"],
            assets: {
                js: [
                    "dist/clientlib-site/js/vendors~site.*.js",
                    "dist/clientlib-site/js/site.*.js"
                ],
                css: [
                    "dist/clientlib-site/css/vendors~site.*.css",
                    "dist/clientlib-site/css/site.*.css"
                ],
                resources: [
                    {
                        src: "dist/clientlib-site/resources/fonts/*.{eot,svg,ttf,woff,woff2}",
                        dest: "fonts"
                    },
                    {
                        src: "dist/clientlib-site/resources/images/country-flags/*.svg",
                        dest: "images/country-flags"
                    }
                ]
            }
        }
    ]
};