/*
 *  Copyright 2020 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
