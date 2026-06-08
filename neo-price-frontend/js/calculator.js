const PRICING_URL = "http://localhost:8080/pricing";
const CUSTOMER_URL = "http://localhost:8080/customers";

const calculateButton =
    document.getElementById("calculateButton");

const saveButton =
    document.getElementById("saveButton");

document.addEventListener("DOMContentLoaded", () => {

    loadCustomers();

    calculateButton.addEventListener(
        "click",
        calculatePricing
    );

    saveButton.addEventListener(
        "click",
        savePricing
    );

});

async function loadCustomers() {

    try {

        const response = await fetch(CUSTOMER_URL, {
            headers: {
                Authorization:
                    localStorage.getItem("token")
            }
        });

        const customers = await response.json();

        const select =
            document.getElementById("customerId");

        customers.forEach(customer => {

            select.innerHTML += `
                <option value="${customer.customerId}">
                    ${customer.name}
                </option>
            `;

        });

    } catch (error) {

        console.error(error);

    }

}

function buildRequest() {

    return {
        cost: Number(
            document.getElementById("cost").value
        ),

        profitMarginPercentage: Number(
            document.getElementById("profitMargin").value
        ),

        taxPercentage: Number(
            document.getElementById("taxPercentage").value
        ),

        customerId: Number(
            document.getElementById("customerId").value
        )
    };

}

async function calculatePricing() {

    try {

        const response = await fetch(
            `${PRICING_URL}/calculate`,
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json",

                    Authorization:
                        localStorage.getItem("token")
                },

                body: JSON.stringify(
                    buildRequest()
                )
            }
        );

        if (!response.ok) {
            throw new Error(
                "Calculation error."
            );
        }

        const pricing =
            await response.json();

        renderPricing(pricing);

    } catch (error) {

        console.error(error);

        alert(
            "Error while calculating pricing."
        );

    }

}

async function savePricing() {

    try {

        if (!customerId.value) {
            alert("Selecione um cliente antes de salvar.");
            return;
        }

        const response = await fetch(
            PRICING_URL,
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json",

                    Authorization:
                        localStorage.getItem("token")
                },

                body: JSON.stringify(
                    buildRequest()
                )
            }
        );

        if (!response.ok) {
            throw new Error(
                "Error while saving pricing."
            );
        }

        const pricing =
            await response.json();

        renderPricing(pricing);

        alert(
            "Pricing saved successfully."
        );

        clearForm();

    } catch (error) {

        console.error(error);

        alert(
            "Error while saving pricing."
        );

    }

}

function renderPricing(pricing) {

    document.getElementById(
        "priceWithoutTax"
    ).textContent =
        `R$ ${Number(
            pricing.priceWithoutTax
        ).toFixed(2)}`;

    document.getElementById(
        "salePriceWithTax"
    ).textContent =
        `R$ ${Number(
            pricing.salePriceWithTax
        ).toFixed(2)}`;

    document.getElementById(
        "taxAmount"
    ).textContent =
        `R$ ${Number(
            pricing.taxAmount
        ).toFixed(2)}`;

    document.getElementById(
        "profitAmount"
    ).textContent =
        `R$ ${Number(
            pricing.profitAmount
        ).toFixed(2)}`;

    document.getElementById(
        "profitMarginResult"
    ).textContent =
        `${Number(
            pricing.profitMarginPercentage
        ).toFixed(2)}%`;

    document.getElementById(
        "taxPercentageResult"
    ).textContent =
        `${Number(
            pricing.taxPercentage
        ).toFixed(2)}%`;

}

function clearForm() {
    cost.value = "";
    profitMargin.value = "";
    taxPercentage.value = "";
    customerId.value = "";

    document.getElementById("priceWithoutTax").textContent = "R$ 0,00";
    document.getElementById("salePriceWithTax").textContent = "R$ 0,00";
    document.getElementById("taxAmount").textContent = "R$ 0,00";
    document.getElementById("profitAmount").textContent = "R$ 0,00";
    document.getElementById("profitMarginResult").textContent = "0%";
    document.getElementById("taxPercentageResult").textContent = "0%";
}