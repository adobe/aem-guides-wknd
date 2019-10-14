var jQuery = require("jquery");

jQuery(function($) {
    "use strict";

    (function() {
        const currentUserUrl = $('.wknd-sign-in-buttons').data('current-user-url'),
            signIn = $('[href="#sign-in"]'),
            signOut = $('[href="#sign-out"]');

        $.getJSON(currentUserUrl + "?nocache=" + new Date().getTime(), function(currentUser) {
            const anonymous = 'anonymous' === currentUser.authorizableId;

            signIn.toggle(anonymous);
            signOut.toggle(!anonymous);
        });
    })();
});
