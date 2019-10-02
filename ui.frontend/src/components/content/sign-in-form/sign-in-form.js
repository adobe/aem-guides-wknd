var jQuery = require("jquery");

jQuery(function($) {
    "use strict";

    /* Sign In form submission */
    $('body').on('submit', '#wknd-sign-in-form', function(e) {
        e.preventDefault();

        const form = $(this);

        // Clear any form submission errors
        form.find('[name=j_username][name=j_password]').removeClass('cmp-form-text__text--error');

        $.ajax({
            type: "POST",
            url: form.attr('action'),
            data: form.serialize(),
            success: function(data) {
                signIn(form.find('input[name="sling.auth.redirect"]').val() || false);
            }, error: function(data) {
                form.find('[name=j_username],[name=j_password]').addClass('cmp-form-text__text--error');
            }
        });

        return false;
    });

    function signIn(redirectUrl) {
        if (!redirectUrl) {
            window.location.reload();
        } else {
            window.location = redirectUrl;
        }
    }
});