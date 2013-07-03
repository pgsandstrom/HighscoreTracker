(function () {
    "use strict";

    var NUMER_OF_HASHES = 1000;

    $(document).ready(function () {

        var loginButtonText;
        var entry = document.getElementById('login-form');

//        $.getScript("/resources/js/sha512.js", setup);
//        function setup() {
//            entry.addEventListener("submit", login);
//        }

        entry.addEventListener("submit", login);

        function login(evt) {

            evt.preventDefault();
            var loginButton = $('#submit-login');
            loginButtonText = loginButton.prop('value');
            loginButton.prop('value', 'LOADING');
            loginButton.prop('disabled', true);

            var username = $('#j_username').val();
            var password = $('#password-visible').val();

            //let slow crap like ff "unpress" the button
            setTimeout(function () {
                multiHash(password, 0);
            }, 50);

            //recursive calls with setTimeout to let the UI update:
            function multiHash(hashedPassword, count) {
                for (var i = count; i < NUMER_OF_HASHES; i++) {
                    var shaObj = new jsSHA(hashedPassword, "TEXT");
                    hashedPassword = shaObj.getHash("SHA-512", "HEX");

                    //let the ui update every 200 iterations:
                    if (i !== 0 && i % 200 === 0) {
                        i++;
                        setTimeout(function () {
                            multiHash(hashedPassword, i);
                        }, 0);
                        return;
                    }
                }

                validateLogin(hashedPassword);
            }


            function validateLogin(hashedPassword) {
                $.post(contextPath + '/api/player/login/try', {
                    name: username,
                    password: hashedPassword
                }).done(function (data, textStatus, jqXHR) {
                        // Prepare the form!
                        $('#j_password').val(hashedPassword);
                        entry.submit();
                    }).fail(function (data, textStatus, jqXHR) {
                        console.log("error: " + data.status);
                        if (data.status === 404) {
                            $('#login-error').text('User does not exist');
                        } else if (data.status === 400) {
                            $('#login-error').text('invalid password');
                        } else {
                            $('#login-error').text('unknown error');
                        }
                        loginButton.prop('disabled', false);
                        loginButton.prop('value', loginButtonText);
                    }).always(function () {
                    });
            }


            return false;
        }
    });

})();