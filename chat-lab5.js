$(document).ready(function () {
    var api = "http://193.226.5.99/chat-piu/";
    var token = "";
    var displayName = "";
    var readTimer;

    // Pastram animatia din lab 4 si inlocuim doar autentificarea simulata.
    $("#chat-form").off("submit").on("submit", function (event) {
        event.preventDefault();
        var username = $("#chat-username").val().trim();
        var password = $("#chat-password").val();

        if (!username || !password) {
            loginMessage("Enter username and password.", true);
            return;
        }

        $("#chat-form input").prop("disabled", true);
        $("#chat-processing").show();

        $.ajax({
            url: api + "authenticate.php",
            method: "POST",
            contentType: "application/json",
            dataType: "json",
            timeout: 15000,
            data: JSON.stringify({ username: username, password: password }),
            success: function (data) {
                token = data.token;
                displayName = data.display;
                $("#chat-password").val("");
                $("#chat-form").hide();
                $("#chat-display").text(displayName);
                $("#chat-messages").empty();
                $("#chat-status").text("");
                $("#chat-conversation").show();
                $("#chat-text").trigger("focus");
                scheduleRead();
            },
            error: function (xhr) {
                loginMessage(errorMessage(xhr), true);
            },
            complete: function () {
                $("#chat-processing").hide();
                $("#chat-form input").prop("disabled", false);
            }
        });
    });

    function loginMessage(message, error) {
        $("#chat-message").toggleClass("error", error);
        $("#chat-message i").attr("class", error ? "fa fa-exclamation-triangle" : "fa fa-lock");
        $("#chat-message span").text(message);
    }

    function errorMessage(xhr) {
        if (xhr.responseJSON && xhr.responseJSON.message) {
            return xhr.responseJSON.message;
        }
        return "Cannot connect to the server. Please try again.";
    }

    function authorization(xhr) {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
    }

    // setTimeout evita suprapunerea cererilor de citire.
    function scheduleRead() {
        clearTimeout(readTimer);
        if (token) readTimer = setTimeout(readMessages, 3000);
    }

    function readMessages() {
        $("#chat-send, #chat-logout").prop("disabled", true);
        $.ajax({
            url: api + "readmessages.php",
            method: "GET",
            dataType: "json",
            timeout: 15000,
            beforeSend: authorization,
            success: function (data) {
                $.each(data.messages, function (index, message) {
                    var entry = $("<div>").addClass("chat-entry");
                    if (message.sender === displayName) entry.addClass("chat-own");
                    $("<strong>").text(message.sender).appendTo(entry);
                    $("<small>").text(message.timestamp).appendTo(entry);
                    $("<div>").text(message.text).appendTo(entry);
                    entry.appendTo("#chat-messages");
                });
                if (data.messages.length) {
                    var messages = $("#chat-messages")[0];
                    messages.scrollTop = messages.scrollHeight;
                }
                $("#chat-status").text("");
            },
            error: chatError,
            complete: function () {
                $("#chat-send, #chat-logout").prop("disabled", false);
                scheduleRead();
            }
        });
    }

    $("#chat-send-form").on("submit", function (event) {
        event.preventDefault();
        var text = $("#chat-text").val().trim();
        if (!token || !text || $("#chat-send").prop("disabled")) return;

        clearTimeout(readTimer);
        $("#chat-send, #chat-logout, #chat-text").prop("disabled", true);
        $("#chat-status").text("Sending...");
        $.ajax({
            url: api + "sendmessage.php",
            method: "PUT",
            contentType: "application/json",
            dataType: "json",
            timeout: 15000,
            beforeSend: authorization,
            data: JSON.stringify({ message: text }),
            success: function () {
                $("#chat-text").val("");
                $("#chat-status").text("Message sent.");
            },
            error: chatError,
            complete: function () {
                $("#chat-send, #chat-logout, #chat-text").prop("disabled", false);
                if (token) $("#chat-text").trigger("focus");
                scheduleRead();
            }
        });
    });

    $("#chat-logout").on("click", function () {
        if (!token || $(this).prop("disabled")) return;
        clearTimeout(readTimer);
        $("#chat-send, #chat-logout").prop("disabled", true);
        $.ajax({
            url: api + "logout.php",
            method: "DELETE",
            contentType: "application/json",
            dataType: "json",
            timeout: 15000,
            beforeSend: authorization,
            success: function () {
                endSession("Authentication required", false);
            },
            error: chatError,
            complete: function () {
                $("#chat-send, #chat-logout").prop("disabled", false);
                scheduleRead();
            }
        });
    });

    function chatError(xhr) {
        if (xhr.status === 401) {
            endSession("Session expired. Please log in again.", true);
        } else {
            $("#chat-status").text(errorMessage(xhr));
        }
    }

    function endSession(message, error) {
        clearTimeout(readTimer);
        token = "";
        displayName = "";
        $("#chat-conversation").hide();
        $("#chat-text").val("");
        $("#chat-form").show();
        loginMessage(message, error);
    }
});
