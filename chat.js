// LAB 4: listener separat, fara a suprascrie window.onload din validation.js.
document.addEventListener("DOMContentLoaded", () => {
    const chat = document.getElementById("chat");
    const toggle = document.getElementById("chat-toggle");
    const panel = document.getElementById("chat-panel");
    const authForm = document.getElementById("chat-form");
    const username = document.getElementById("chat-username");
    const password = document.getElementById("chat-password");
    const result = document.getElementById("chat-message");
    const processing = document.getElementById("chat-processing");
    let pending = false;
    let authenticated = false;

    function closeChat() {
        chat.classList.remove("is-open", "is-opening");
        panel.hidden = true;
        panel.inert = true;
        toggle.setAttribute("aria-expanded", "false");
        toggle.setAttribute("aria-label", "Open chat");
        toggle.focus();
    }

    toggle.addEventListener("click", () => {
        if (chat.classList.contains("is-open")) {
            closeChat();
            return;
        }

        panel.hidden = false;
        chat.classList.add("is-open", "is-opening");
        toggle.setAttribute("aria-expanded", "true");
        toggle.setAttribute("aria-label", "Close chat");
    });

    chat.addEventListener("animationend", (event) => {
        if (event.animationName !== "chat-open") return;
        chat.classList.remove("is-opening");
        panel.inert = false;
        if (!pending && !authenticated) username.focus();
    });

    chat.addEventListener("keydown", (event) => {
        if (event.key === "Escape") closeChat();
    });

    authForm.addEventListener("submit", (event) => {
        event.preventDefault();
        if (pending || authenticated) return;

        const accepted = username.value === "admin" && password.value === "admin1234";
        pending = true;
        for (const input of authForm.elements) input.disabled = true;
        authForm.setAttribute("aria-busy", "true");
        processing.hidden = false;

        // Simulare locala: rezultatul apare numai dupa cele 3 secunde.
        window.setTimeout(() => {
            pending = false;
            authenticated = accepted;
            processing.hidden = true;
            authForm.setAttribute("aria-busy", "false");
            result.classList.toggle("error", !accepted);
            result.querySelector("i").className = accepted
                ? "fa fa-check" : "fa fa-exclamation-triangle";
            result.querySelector("span").textContent = accepted
                ? "Authentication successful !" : "Invalid username or password";

            // Ca in exemplul video, dupa succes campurile raman dezactivate.
            for (const input of authForm.elements) input.disabled = accepted;
            if (!accepted && chat.classList.contains("is-open") && !panel.inert) password.focus();
        }, 3000);
    });
});
