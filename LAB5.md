# Laboratorul 5 — Chat cu AJAX

Deschide `lab1.html` si apasa pe iconita chatului din stanga.

## Fisiere

- `chat-lab5.js`: autentificare, citire, trimitere si deconectare prin `$.ajax`.
- `chat-lab5.css`: reguli noi care suprascriu stilurile necesare din laboratoarele anterioare.
- `lab1.html`: incarca jQuery local din exemplul profesorului si contine formularul pentru mesaje.

Fisierele CSS si JavaScript din laboratoarele anterioare nu au fost modificate pentru lab 5. Animatia din `chat.js` este reutilizata. In fisierul nou, `.off("submit")` elimina autentificarea simulata, apoi `.on("submit", ...)` leaga formularul la API.

## Testare

1. Creeaza un cont la http://193.226.5.99/chat-piu/register/ daca nu mai ai unul activ.
2. Deschide pagina in doua taburi si autentifica-te cu acelasi cont in ambele.
3. Scrie un mesaj si apasa Send sau Enter. Verifica primirea lui in celalalt tab.
4. Apasa Logout in fiecare tab pentru a elibera sesiunile.

API-ul permite maximum doua sesiuni pentru acelasi cont. Conturile diferite nu pot comunica intre ele. Contul se sterge dupa doua ore de inactivitate.

Mesajele se citesc la 3 secunde dupa terminarea cererii precedente. Pe durata unei cereri, butoanele Send si Logout sunt dezactivate pentru a evita cereri simultane. Inchiderea panoului pastreaza sesiunea; Logout o inchide pe server.

Datele sunt trimise ca JSON. Cererile protejate folosesc antetul `Authorization: Bearer ...`. Tokenul ramane doar in memoria paginii. Mesajele sunt adaugate cu `.text()`, ca sa fie afisate ca text, inclusiv cand contin etichete HTML.

Documentatie jQuery: https://api.jquery.com/category/ajax/
