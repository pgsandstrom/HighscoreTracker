(function () {
    "use strict";

    window.onload = function () {

        var entry = document.getElementById('register-player-form');
        entry.addEventListener("submit", register);

//        window.setTimeout(function () {
//            entry.submit();
//        }, 15000);

        function register(evt) {
            try {
                evt.preventDefault();

                var name = document.getElementById("register-player-form:name").value;
                var password = document.getElementById("register-player-form:password").value;
                var passwordRepeat = document.getElementById("register-player-form:password-repeat").value;
                var mail = document.getElementById("register-player-form:mail").value;
                var description = document.getElementById("register-player-form:description").value;

                if (password !== passwordRepeat) {
                    $('#password-dont-match').css('display', 'inline');
                    $('#register-error').text('');
                    return false;
                } else {
                    $('#password-dont-match').css('display', 'none');
                }

                $.post(contextPath + '/api/player/create/try', {
                    name: name,
                    password: password,
                    mail: mail,
                    description: description
                }).done(function (data, textStatus, jqXHR) {
                        // submit the form!
                        // entry.submit() does not work, jsf requires the submit-button as a parameter as well...
                        entry.removeEventListener("submit", register);
                        $('#register-player-form\\:submit-button').click();
                    }).fail(function (data, textStatus, jqXHR) {
                        console.log("error");
                        console.log("data: " + data);
                        console.log("textStatus: " + textStatus);
                        if (data.status === 400) {
                            try {
                                var json = $.parseJSON(data.responseText);
                                $('#register-error').text(json.message);
                            } catch (e) {
                                console.log(e);
                                $('#register-error').text('Failed to parse error');
                            }
                        } else {
                            $('#register-error').text('unknown error');
                        }
                    });

            } catch (error) {
                console.log("Caught: " + error);
            }
//            return false;
        }


    };

})();