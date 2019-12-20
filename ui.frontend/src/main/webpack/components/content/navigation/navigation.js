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

    var HEADER_CONTAINER_SELECTOR          = '.cmp-experiencefragment--header',
        FOOTER_CONTAINER_SELECTOR          = '.cmp-experiencefragment--footer',
        HEADER_NAVIGATION_SELECTOR         = '.cmp-navigation--header .cmp-navigation',
        ROOT_LEVEL_NAVIGATION_SELECTOR     = '.cmp-navigation__item--level-0.cmp-navigation__item--active > .cmp-navigation__item-link',
        LOGO_SELECTOR                      = '.cmp-image--logo .cmp-image__link',
        DATA_PROCESSED                     = 'data-processed';

    function createMobileNavigation() {

        var $nav = $(HEADER_CONTAINER_SELECTOR + ' ' + HEADER_NAVIGATION_SELECTOR),
            $body = $('body');

        //Top Level Navigation (expected to only be one of these)
        if ($nav !== undefined && $nav.length === 1 && !$($nav)[0].hasAttribute(DATA_PROCESSED)) {
            // Mark the component element as processed to avoid the cyclic processing (see .not(..) above).
            $($nav).attr(DATA_PROCESSED, true)

            // Toggle Nav
            $('<div id="toggleNav">' +
                '<a href="#mobileNav" aria-label="Open hidden mobile navigation" class="toggle"><i class="wknd__icon wkndicon-menu" aria-hidden="true"></i></a>' +
                '</div>'
            ).appendTo($body);

            // Navigation Panel.
            $(
                    '<div id="mobileNav" class="cmp-navigation--mobile">' +
                    '<nav class="cmp-navigation">' +
                    $($nav).html() +
                    '</nav>' +
                    '</div>'
                )
                .appendTo($body)
                .panel({ //panel defined in uti.js
                    delay: 500,
                    hideOnClick: true,
                    hideOnSwipe: true,
                    resetScroll: true,
                    resetForms: true,
                    side: 'left',
                    target: $body,
                    visibleClass: 'navPanel-visible'
                });
        }
    }

    /**
     * Update the logo with a "home" url based on the root link from the navigation
     */
    function setHomeLink(navigationSelector, logoSelector) {
        var $nav = $(navigationSelector),
            $logo = $(logoSelector);
        
            //Expect to update only a single logo
            if($nav !== undefined && 
                $nav.length === 1 && 
                $logo !== undefined && 
                $logo.length === 1 && 
                !$($logo)[0].hasAttribute(DATA_PROCESSED)) {
                    //set the logo href attribute based on the root navigation target
                    $($logo).attr('href', $($nav).attr('href'));
                }
    }

    createMobileNavigation();
    setHomeLink(HEADER_NAVIGATION_SELECTOR + ' ' + ROOT_LEVEL_NAVIGATION_SELECTOR, HEADER_CONTAINER_SELECTOR + ' ' + LOGO_SELECTOR );
    setHomeLink(FOOTER_CONTAINER_SELECTOR + ' ' + ROOT_LEVEL_NAVIGATION_SELECTOR, FOOTER_CONTAINER_SELECTOR + ' ' + LOGO_SELECTOR );

});