var jQuery = require("jquery");

jQuery(function($) {
    "use strict";

    // Add error indicator style to the Sign In form
    (function() {
        const form = $('#wknd-sign-in-form');
        if(form) {
            const urlParams = new URLSearchParams(window.location.search);
            if(urlParams && urlParams.has('j_reason') && urlParams.get('j_reason') == 'invalid_login') {
                form.find('[name=j_username],[name=j_password]').addClass('cmp-form-text__text--error');
            }
        }
    })();

    
    /* Add redirect to current page on the login  */
    $('body').on('wknd-modal-show', function(e) {
        const slingRedirectInput = $('#wknd-sign-in-form input[name="sling.auth.redirect"]');
        if(slingRedirectInput) {
            slingRedirectInput.val(window.location.pathname);
        }
    });

});