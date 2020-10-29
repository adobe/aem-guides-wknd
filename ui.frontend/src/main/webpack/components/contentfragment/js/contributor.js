"use strict";

(function() { 

    function applyComponentStyles() {
        document.querySelectorAll(".cmp-contentfragment--contributor .cmp-contentfragment[data-cmp-contentfragment-model=\"wknd/models/contributors\"]:not([data-cmp-contributor-processed='true'])").forEach(function (cf) {
            cf.setAttribute("data-cmp-contributor-processed", true);

            var cfEls = cf.querySelector('.cmp-contentfragment__elements');
            var assetPath = cfEls.querySelector(".cmp-contentfragment__element--pictureReference .cmp-contentfragment__element-value").innerText.trim();

            if (assetPath && assetPath.indexOf("/content/dam/") === 0) {
                var pictureElement = document.createElement("img"); 

                pictureElement.setAttribute("class","cmp-contentfragment__picture"); 
				pictureElement.setAttribute("src", assetPath);

                cfEls.insertBefore(pictureElement, cfEls.querySelector(".cmp-contentfragment__element--fullName"));
            }
        });
    }

    applyComponentStyles();

    document.querySelectorAll(".responsivegrid").forEach(function(el) { el. addEventListener("DOMNodeInserted", applyComponentStyles); });
})();
