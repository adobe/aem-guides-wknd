var jQuery = require("jquery");

jQuery(function($) {
    "use strict";

    (function() {
        const form = $('#wknd-sign-in-form');
        if(form) {
            const urlParams = new URLSearchParams(window.location.search);
            if(urlParams && urlParams.has('j_reason') && urlParams.get('j_reason') == 'invalid_login') {
                form.find('[name=j_username],[name=j_password]').addClass('cmp-form-text__text--error');
            }
        }
    })();
});