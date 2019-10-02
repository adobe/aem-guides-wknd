var jQuery = require("jquery");

jQuery(function($) {
    "use strict";

    (function() {
        const currentUserUrl = $('.wknd-sign-in-buttons').data('current-user-url'),
            signIn = $('[href="#sign-in"]'),
            signOut = $('[href="#sign-out"]');

        $.get(currentUserUrl + "?_ck=" + new Date().getTime(), function(data) {
            const anonymous = 'anonymous' === data;

            signIn.toggle(anonymous);
            signOut.toggle(!anonymous);
        });
    })();
});
