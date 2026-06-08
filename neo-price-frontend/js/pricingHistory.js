const API_URL = "http://localhost:8080/pricing";

const tableBody = document.getElementById("pricingTableBody");

document.addEventListener("DOMContentLoaded", () => {
    loadPricings();
});

async function loadPricings() {

    try {

        const response = await fetch(API_URL, {
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error("Error while loading pricing history.");
        }

        const pricings = await response.json();

        tableBody.innerHTML = "";

        pricings.forEach(pricing => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${pricing.customerName}</td>
                <td>R$ ${Number(pricing.cost).toFixed(2)}</td>
                <td>${Number(pricing.profitMarginPercentage).toFixed(2)}%</td>
                <td>R$ ${Number(pricing.priceWithoutTax).toFixed(2)}</td>
                <td>${Number(pricing.taxPercentage).toFixed(2)}%</td>
                <td>R$ ${Number(pricing.salePriceWithTax).toFixed(2)}</td>
                <td>${formatDate(pricing.createdAt)}</td>
                <td>
                    <button onclick="deletePricing(${pricing.pricingHistoryId})">
                        Excluir
                    </button>
                </td>
            `;

            tableBody.appendChild(row);

        });

    } catch (error) {

        console.error(error);
        alert("Error while loading pricing history.");

    }

}

async function deletePricing(id) {

    const confirmed = confirm(
        "Are you sure you want to delete this pricing?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(`${API_URL}/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error("Error while deleting pricing.");
        }

        await loadPricings();

    } catch (error) {

        console.error(error);
        alert("Error while deleting pricing.");

    }

}

function formatDate(dateString) {
    return new Date(dateString)
        .toLocaleDateString("pt-BR");
}