let input_surname;
let input_first_name;
let input_address;
let input_birth_date;
let input_phone;
let input_email;
let form;
let message;

window.onload = () => {
    input_surname = document.getElementById("surname");
    input_first_name = document.getElementById("first-name");
    input_address = document.getElementById("address");
    input_birth_date = document.getElementById("birth-date");
    input_phone = document.getElementById("phone");
    input_email = document.getElementById("email");
    form = document.getElementById("contact-form");
    message = document.getElementById("form-message");

    input_surname.addEventListener("blur", surname_validation);
    input_first_name.addEventListener("blur", first_name_validation);
    input_address.addEventListener("blur", address_validation);
    input_birth_date.addEventListener("blur", birth_date_validation);
    input_phone.addEventListener("blur", phone_validation);
    input_email.addEventListener("blur", email_validation);

    form.addEventListener("submit", form_validation);
    form.addEventListener("reset", form_reset);
};

function surname_validation() {
    if (input_surname.value.trim().length >= 3) {
        input_surname.classList.remove("invalid_input_color");
        input_surname.style.backgroundColor = "white";
        input_surname.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_surname.style.removeProperty("background-color");
        input_surname.classList.add("invalid_input_color");
        input_surname.setAttribute("aria-invalid", "true");
        return false;
    }
}

function first_name_validation() {
    if (input_first_name.value.trim().length >= 3) {
        input_first_name.classList.remove("invalid_input_color");
        input_first_name.style.backgroundColor = "white";
        input_first_name.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_first_name.style.removeProperty("background-color");
        input_first_name.classList.add("invalid_input_color");
        input_first_name.setAttribute("aria-invalid", "true");
        return false;
    }
}

function address_validation() {
    if (input_address.value.trim().length >= 3 && /\d/.test(input_address.value) && !/[@#$%^&*]/.test(input_address.value)) {
        input_address.classList.remove("invalid_input_color");
        input_address.style.backgroundColor = "white";
        input_address.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_address.style.removeProperty("background-color");
        input_address.classList.add("invalid_input_color");
        input_address.setAttribute("aria-invalid", "true");
        return false;
    }
}

function birth_date_validation() {
    let valid_date = false;

    if (/^\d{2}\/\d{2}\/\d{4}$/.test(input_birth_date.value)) {
        const parts = input_birth_date.value.split("/");
        const day = Number(parts[0]);
        const month = Number(parts[1]);
        const year = Number(parts[2]);
        const date = new Date();
        date.setFullYear(year, month - 1, day);

        valid_date = year > 0 && date.getFullYear() === year &&
            date.getMonth() === month - 1 && date.getDate() === day;
    }

    if (valid_date) {
        input_birth_date.classList.remove("invalid_input_color");
        input_birth_date.style.backgroundColor = "white";
        input_birth_date.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_birth_date.style.removeProperty("background-color");
        input_birth_date.classList.add("invalid_input_color");
        input_birth_date.setAttribute("aria-invalid", "true");
        return false;
    }
}

function phone_validation() {
    if (/^\d{3}-\d{9}$/.test(input_phone.value)) {
        input_phone.classList.remove("invalid_input_color");
        input_phone.style.backgroundColor = "white";
        input_phone.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_phone.style.removeProperty("background-color");
        input_phone.classList.add("invalid_input_color");
        input_phone.setAttribute("aria-invalid", "true");
        return false;
    }
}

function email_validation() {
    if (/^[^\s@<>(),;:\\"\[\]]+@[^\s@<>(),;:\\"\[\]]+\.[^\s@<>(),;:\\"\[\].]+$/.test(input_email.value)) {
        input_email.classList.remove("invalid_input_color");
        input_email.style.backgroundColor = "white";
        input_email.setAttribute("aria-invalid", "false");
        return true;
    } else {
        input_email.style.removeProperty("background-color");
        input_email.classList.add("invalid_input_color");
        input_email.setAttribute("aria-invalid", "true");
        return false;
    }
}

function form_validation(event) {
    event.preventDefault();
    let errors = "";

    if (!surname_validation()) {
        errors += "Surname: introduceți cel puțin 3 caractere.\n";
    }

    if (!first_name_validation()) {
        errors += "First name: introduceți cel puțin 3 caractere.\n";
    }

    if (!address_validation()) {
        errors += "Address: introduceți cel puțin 3 caractere și o cifră, fără @#$%^&*.\n";
    }

    if (!birth_date_validation()) {
        errors += "Birth date: introduceți o dată existentă în formatul dd/mm/yyyy.\n";
    }

    if (!phone_validation()) {
        errors += "Phone number: folosiți formatul 123-123456789 (3 cifre, cratimă, 9 cifre).\n";
    }

    if (!email_validation()) {
        errors += "Email: introduceți o adresă de email validă, de exemplu nume@exemplu.ro.\n";
    }

    message.hidden = false;

    if (errors === "") {
        message.classList.add("success");
        message.textContent = "Formularul a fost completat corect. Datele au fost validate!";
    } else {
        message.classList.remove("success");
        message.textContent = "Verificați următoarele câmpuri (toate sunt obligatorii):";
        const error_lines = errors.trim().split("\n");
        for (let i = 0; i < error_lines.length; i++) {
            message.append(document.createElement("br"));
            message.append(error_lines[i]);
        }
    }
}

function form_reset() {
    input_surname.classList.remove("invalid_input_color");
    input_surname.style.removeProperty("background-color");
    input_surname.removeAttribute("aria-invalid");
    input_first_name.classList.remove("invalid_input_color");
    input_first_name.style.removeProperty("background-color");
    input_first_name.removeAttribute("aria-invalid");
    input_address.classList.remove("invalid_input_color");
    input_address.style.removeProperty("background-color");
    input_address.removeAttribute("aria-invalid");
    input_birth_date.classList.remove("invalid_input_color");
    input_birth_date.style.removeProperty("background-color");
    input_birth_date.removeAttribute("aria-invalid");
    input_phone.classList.remove("invalid_input_color");
    input_phone.style.removeProperty("background-color");
    input_phone.removeAttribute("aria-invalid");
    input_email.classList.remove("invalid_input_color");
    input_email.style.removeProperty("background-color");
    input_email.removeAttribute("aria-invalid");
    message.hidden = true;
    message.textContent = "";
    message.classList.remove("success");
}
