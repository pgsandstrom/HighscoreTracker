(function () {
    "use strict";

    $(document).ready(function () {

        var entry = document.getElementById('create-game-form');
        entry.addEventListener("submit", create);

        function create(evt) {
            console.log('create game script');
            try {
                var name = $('#create-game-form\\:name').val();
                var description = $('#create-game-form\\:description').val();

                $.post(contextPath + '/api/game/create/try', {
                    name: name,
                    description: description,
                    skip_auth: true
                }).done(function (data, textStatus, jqXHR) {
                        // submit the form!
                        // entry.submit() does not work, jsf requires the submit-button as a parameter as well...
                        entry.removeEventListener("submit", create);
                        $('#create-game-form\\:submit-button').click();
                    }).fail(function (data, textStatus, jqXHR) {
                        console.log("error: " + data.status);
                        if ((data.status === 400 || data.status === 404)) {
                            try {
                                var json = $.parseJSON(data.responseText);
                                $('#create-game-error').text(json.message);
                            } catch (e) {
                                $('#create-game-error').text('unknown error: ' + data.status);
                                console.log("response: " + data.responseText);
                            }
                        } else {
                            $('#create-game-error').text('unknown error: ' + data.status);
                            console.log("response: " + data.responseText);
                        }
                    });


            } catch (error) {
                console.log("Caught: " + error);
            }

            evt.preventDefault();
            return false;
        }


    });

})();