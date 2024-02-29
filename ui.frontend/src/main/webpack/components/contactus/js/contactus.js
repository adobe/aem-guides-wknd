import jQuery from "jquery";

jQuery(function($) {
    "use strict";

    $( "#wknd-contact-us-form" ).on( "submit", function( event ) {
        event.preventDefault();
        const username = $( "#wknd-contact-us-form-username" ).val();
        const message = $( "#wknd-contact-us-form-message" ).val();
        const time = Date.now();
        $.get( `/us/en/about-us.html?username=${username}&time=${time}`, function(data) {
            console.log(data);
            window.location.href = `/us/en/about-us.html?username=${username}&time=${time}`;
          })
        .fail(function(err) {
            console.log(err);
            $( "#cmp-contactus-error" ).html(`Error: ${err.status}`);
        });
    });
});
