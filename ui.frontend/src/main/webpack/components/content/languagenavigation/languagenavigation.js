/*
 *  Copyright 2018 Adobe Systems Incorporated
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

var jQuery = require("jquery");

// Wrap bindings in anonymous namespace to prevent collisions
jQuery(function ($) {
    "use strict";

    function displayCurrentLanguage() {
        var CMP_SELECTOR = '.cmp-languagenavigation--header',
            CMP_PROCESSED = 'data-lang-nav-processed',
            ACTIVE_LINK_SELECTOR = '.cmp-languagenavigation__item--active > .cmp-languagenavigation__item-link',
            ACTIVE_COUNTRY_SELECTOR = '.cmp-languagenavigation__item--level-0.cmp-languagenavigation__item--active',
            langNav = $(CMP_SELECTOR).not('[' + CMP_PROCESSED + '=\'true\']'),
            activeCountryImg,
            activeLanguage,
            toggleButton,
            displayPosition;

        //Top Level Navigation (expected to only be one of these)
        if (langNav != undefined && langNav.length == 1) {

            //insert current lnaguage in header
            $(langNav).attr(CMP_PROCESSED, true);
            activeLanguage = $(CMP_SELECTOR + ' ' + ACTIVE_LINK_SELECTOR).attr('lang');
            activeLanguage = activeLanguage !== undefined ? activeLanguage : 'Language';

            activeCountryImg = $(CMP_SELECTOR + ' ' + ACTIVE_COUNTRY_SELECTOR)
                               .css('background-image');
            activeCountryImg = activeCountryImg !== undefined ? activeCountryImg.replace("\"", "\'").replace("\"", "\'") : 'none';

            toggleButton = '<div class="cmp-languagenavigation--langnavtoggle">' +
                                '<a id="langNavToggleHeader" style="background-image:' + activeCountryImg + 
                                '" href="#langNavToggle" aria-label="Toggle Language">' + activeLanguage + '</a></div>';
            $(langNav).prepend(toggleButton);

            //attach toggle to change languages
            $('#langNavToggleHeader').click(function () {
                displayPosition = $(this).position().left - 240;
                $(CMP_SELECTOR + ' .cmp-languagenavigation').css({ left: displayPosition });
                $(CMP_SELECTOR + ' .cmp-languagenavigation').toggleClass('showMenu');
                $('#langNavToggleHeader').toggleClass('open');
            });

            //allow users to click anywhere to close language switcher
            window.onclick = function (event) {
                if (!event.target.matches('#langNavToggleHeader') && $('#langNavToggleHeader').hasClass('open')) {
                    $(CMP_SELECTOR + ' .cmp-languagenavigation').removeClass('showMenu');
                    $('#langNavToggleHeader').removeClass('open');
                }
            }

        }
    }

    displayCurrentLanguage();
});
