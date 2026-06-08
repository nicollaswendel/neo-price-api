const API_URL = "http://localhost:8080";

const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        const response = await fetch(`${API_URL}/users/login`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email,
                password
            })

        });

        if (!response.ok) {

            alert("Invalid email or password.");
            return;

        }

        const data = await response.json();

        localStorage.setItem("token", data.token);

        window.location.href = "../index.html";

    } catch (error) {

        console.error(error);

        alert("Unable to connect to server.");

    }

});