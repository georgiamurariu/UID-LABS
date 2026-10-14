// LAB 3: validare in JavaScript simplu, fara framework-uri sau biblioteci.
"use strict";

// Verifica si existenta datei: de exemplu, 31/02/2024 nu este o data valida.
function isValidBirthDate(value) {
    if (!/^\d{2}\/\d{2}\/\d{4}$/.test(value)) return false;

    const [day, month, year] = value.split("/").map(Number);
    if (year < 1 || month < 1 || month > 12 || day < 1) return false;
    const leapYear = year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0);
    const daysInMonth = [31, leapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    return day <= daysInMonth[month - 1];
}

// Fiecare regula contine verificarea si explicatia afisata la Submit.
const validationRules = {
    surname: {
        test: value => value.trim().length >= 3,
        description: "introduceți cel puțin 3 caractere."
    },
    "first-name": {
        test: value => value.trim().length >= 3,
        description: "introduceți cel puțin 3 caractere."
    },
    address: {
        test: value => value.trim().length >= 3 && /\d/.test(value) && !/[@#$%^&*]/.test(value),
        description: "introduceți cel puțin 3 caractere și o cifră, fără @#$%^&*."
    },
    "birth-date": {
        test: isValidBirthDate,
        description: "introduceți o dată existentă în formatul dd/mm/yyyy."
    },
    phone: {
        test: value => /^\d{3}-\d{9}$/.test(value),
        description: "folosiți formatul 123-123456789 (3 cifre, cratimă, 9 cifre)."
    },
    email: {
        test: value => /^[^\s@<>(),;:\\"\[\]]+@[^\s@<>(),;:\\"\[\]]+\.[^\s@<>(),;:\\"\[\].]+$/.test(value),
        description: "introduceți o adresă de email validă, de exemplu nume@exemplu.ro."
    }
};

const form = document.getElementById("contact-form");
const message = document.getElementById("form-message");
const fields = Array.from(form.getElementsByTagName("input"))
    .filter(input => Object.hasOwn(validationRules, input.id));

// Aceeasi functie este folosita la pierderea focusului si la Submit.
function validateField(input) {
    const valid = validationRules[input.id].test(input.value);
    input.classList.toggle("invalid", !valid);
    input.setAttribute("aria-invalid", String(!valid));
    return valid;
}

// LAB 3: evenimentele blur se ataseaza dinamic; HTML-ul nu contine onblur.
fields.forEach(input => {
    input.required = true;
    input.addEventListener("blur", function () {
        validateField(this);
    });
});

form.addEventListener("submit", function (event) {
    // Pastreaza pagina si mesajul vizibile; laboratorul nu trimite date unui server.
    event.preventDefault();
    const invalidFields = fields.filter(input => !validateField(input));
    message.replaceChildren();
    message.hidden = false;
    message.classList.toggle("success", invalidFields.length === 0);

    if (invalidFields.length === 0) {
        message.textContent = "Formularul a fost completat corect. Datele au fost validate!";
        return;
    }

    message.append("Verificați următoarele câmpuri (toate sunt obligatorii):");
    invalidFields.forEach(input => {
        // LAB 3: denumirea este citita din label, nu scrisa separat in JavaScript.
        const label = input.labels[0].textContent.trim();
        message.append(document.createElement("br"));
        message.append(label + ": " + validationRules[input.id].description);
    });
});

form.addEventListener("reset", function () {
    // Browserul goleste campurile; aici eliminam starea vizuala si mesajul.
    fields.forEach(input => {
        input.classList.remove("invalid");
        input.removeAttribute("aria-invalid");
    });
    message.hidden = true;
    message.replaceChildren();
    message.classList.remove("success");
});
