$(document).ready(function () {
    // Deschidem chatul: intai latimea, apoi inaltimea.
    $("#chat-toggle").on("click", function () {
        if ($("#chat").is(":animated")) return;

        if ($("#chat").hasClass("is-open")) {
            $("#chat").removeClass("is-open").css({ width: 40, height: 40 });
            $("#chat-panel").hide();
            $(this).attr("aria-expanded", "false").attr("aria-label", "Open chat");
        } else {
            $("#chat").addClass("is-open")
                .animate({ width: 300 }, 500)
                .animate({ height: 300 }, 500, function () {
                    $("#chat-panel").show();
                });
            $(this).attr("aria-expanded", "true").attr("aria-label", "Close chat");
        }
    });

    $("#chat-form").on("submit", function (event) {
        event.preventDefault(); // Nu reincarcam pagina.
        if ($("#chat-authenticate").prop("disabled")) return;

        const valid = $("#chat-username").val() === "admin" &&
            $("#chat-password").val() === "admin1234";

        $("#chat-form input").prop("disabled", true);
        $("#chat-processing").show();

        // Simulam verificarea datelor timp de 3 secunde.
        setTimeout(function () {
            $("#chat-processing").hide();
            if (valid) {
                $("#chat-message").removeClass("error");
                $("#chat-message i").attr("class", "fa fa-check");
                $("#chat-message span").text("Authentication successful !");
            } else {
                $("#chat-message").addClass("error");
                $("#chat-message i").attr("class", "fa fa-exclamation-triangle");
                $("#chat-message span").text("Invalid username or password");
                $("#chat-form input").prop("disabled", false);
            }
        }, 3000);
    });
});
