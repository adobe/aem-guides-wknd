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
                resources: {
                    cwd: "./dist/clientlib-site/resources",
                    flatten: false,
                    files: ["**/*.*"]
                }
            }
        },
        {
            name: "clientlib-amp",
            allowProxy: true,
            categories: ["wknd.site.amp"],
            serializationFormat: "xml",
            cssProcessor : ["default:none", "min:none"],
            jsProcessor: ["default:none", "min:none"],
            assets: {
                js: [
                    "dist-amp/clientlib-amp/js/vendors~site.*.js",
                    "dist-amp/clientlib-amp/js/site.*.js"
                ],
                css: [
                    "dist-amp/clientlib-amp/css/vendors~site.*.css",
                    "dist-amp/clientlib-amp/css/site.*.css"
                ],
                resources: {
                    cwd: "./dist-amp/clientlib-amp/resources",
                    flatten: false,
                    files: ["**/*.*"]
                }
            }
        }
    ]
};