const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "../pages/login.html";
}

function getAuthHeader() {

    return {
        "Content-Type": "application/json",
        "Authorization": token
    };

}

const logoutButton = document.getElementById("logoutButton");

if (logoutButton) {

    logoutButton.addEventListener("click", () => {

        localStorage.removeItem("token");

        window.location.href = "/pages/login.html";

    });

}