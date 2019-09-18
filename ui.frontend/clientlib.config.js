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
                    "dist/vendors~site.*.js",
                    "dist/site.*.js"
                ],
                css: [
                    "dist/vendors~site.*.css",
                    "dist/site.*.css"
                ],
                resources: [
                    {
                        src: "dist/resources/*.{eot,svg,ttf,woff,woff2}"
                    },
                    {
                        src: "dist/resources/images/country-flags/*.svg",
                        dest: "images/country-flags"
                    }
                ]
            }
        }
    ]
};