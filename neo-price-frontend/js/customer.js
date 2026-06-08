const API_URL = "http://localhost:8080/customers";

const form = document.getElementById("customerForm");
const tableBody = document.getElementById("customerTableBody");

const nameInput = document.getElementById("name");
const companyNameInput = document.getElementById("companyName");

let editingCustomerId = null;

document.addEventListener("DOMContentLoaded", () => {
    loadCustomers();
});

form.addEventListener("submit", async (event) => {

    event.preventDefault();

    const customer = {
        name: nameInput.value,
        companyName: companyNameInput.value
    };

    try {

        if (editingCustomerId === null) {

            await fetch(API_URL, {
                method: "POST",
                headers: getAuthHeader(),
                body: JSON.stringify(customer)
            });

            alert("Customer created successfully.");

        } else {

            await fetch(`${API_URL}/${editingCustomerId}`, {
                method: "PATCH",
                headers: getAuthHeader(),
                body: JSON.stringify(customer)
            });

            alert("Customer updated successfully.");

            editingCustomerId = null;
        }

        form.reset();

        loadCustomers();

    } catch (error) {

        console.error(error);

        alert("Error while saving customer.");

    }

});

async function loadCustomers() {

    try {

        const response = await fetch(API_URL, {
            headers: getAuthHeader()
        });

        console.log("Response:", response);

        const customers = await response.json();

        console.log("Customers:", customers);

        tableBody.innerHTML = "";

        customers.forEach(customer => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${customer.name}</td>
                <td>${customer.companyName ?? "-"}</td>

                <td>
                    <button onclick="editCustomer(${customer.customerId})">
                        Editar
                    </button>

                    <button onclick="deleteCustomer(${customer.customerId})">
                        Excluir
                    </button>
                </td>
            `;

            tableBody.appendChild(row);

        });

    } catch (error) {

        console.error(error);

        alert("Error while loading customers.");

    }

}

async function editCustomer(id) {

    try {

        const response = await fetch(`${API_URL}/${id}`, {
            headers: getAuthHeader()
        });

        const customer = await response.json();

        nameInput.value = customer.name;
        companyNameInput.value = customer.companyName;

        editingCustomerId = customer.customerId;

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    } catch (error) {

        console.error(error);

        alert("Error while loading customer.");

    }

}

async function deleteCustomer(id) {

    const confirmed = confirm(
        "Do you really want to delete this customer?"
    );

    if (!confirmed) {
        return;
    }

    try {

        await fetch(`${API_URL}/${id}`, {
            method: "DELETE",
            headers: getAuthHeader()
        });

        loadCustomers();

    } catch (error) {

        console.error(error);

        alert("Error while deleting customer.");

    }

}