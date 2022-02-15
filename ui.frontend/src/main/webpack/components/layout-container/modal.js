import jQuery from "jquery";

jQuery(function($) {
    "use strict";

    let visible = false;

    /**
     * Handle clicking of the Sign In button
     */
    $('body').on('click', '[data-modal-url]', showModal);


    /**
     * Handle clicking "off-modal" to hide it
     */
    $(document).on('click', (hideModal));


    function showModal(e) {
        e.preventDefault();

        const xfUrl = $(this).data('modal-url');

        if (visible || !xfUrl) { return; }
        const showModalEvt = new Event('wknd-modal-show');
        const body = document.querySelector('body');

        $.get(xfUrl, function (data) {
            const modal = $('<div id="wknd-modal"/>');
            $('body').append(modal.append(data));
            modal.fadeIn(300, function() { visible = true; });
            visible = true;
            // dispatch event to indicate that the modal has been shown
            // used by sign-in-form.js to dynamically update a successful sign-in redirect to the current page
            body.dispatchEvent(showModalEvt);
        });

        return false;
    }

    function hideModal(e) {
        const modal = $('#wknd-modal');
        // if the target of the click isn't the modal nor a descendant of the modal
        if (visible && modal && !modal.is(e.target) && modal.has(e.target).length === 0) {
            e.preventDefault();

            modal.fadeOut(200, function(){
                modal.remove();
                visible = false;
            });

            return false;
        }

    }
});
