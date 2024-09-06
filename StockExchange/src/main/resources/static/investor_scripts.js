document.addEventListener('DOMContentLoaded', () => {
    // Function to fetch stock data from the API
    async function fetchStockData() {
        try {
            const response = await fetch('http://localhost:8081/stocks/list');
            const data = await response.json();

            if (data.statusCode === 200) {
                populateStocksTable(data.data);
            } else {
                console.error('Error fetching data:', data.message);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }

    // Function to populate the stocks table with fetched data
    function populateStocksTable(stocks) {
        const tableBody = document.querySelector('#stocksTableBody');
        tableBody.innerHTML = ''; // Clear existing data

        stocks.forEach(stock => {
            const row = document.createElement('tr');

            // Add data-symbol and data-company-name to the row for easy access
            row.setAttribute('data-symbol', stock.symbol);
            row.setAttribute('data-company-name', stock.name);

            row.innerHTML = `
                <td>${stock.symbol}</td>
                <td>${stock.name}</td>
                <td>$${stock.price.toFixed(2)}</td>
                <td>${stock.volume.toLocaleString()}</td>
                <td>${stock.percentageChange.toLocaleString()}</td>
                <td>
                    <button class="buy-btn" onclick="handleAction('buy', '${stock.symbol}')">Buy</button>
                    <button class="sell-btn" onclick="handleAction('sell', '${stock.symbol}')">Sell</button>
                </td>
            `;

            tableBody.appendChild(row);
        });
    }

    // Function to handle Buy/Sell actions
    window.handleAction = async function(action, symbol) {
        // Prompt user for quantity
        console.log("Handling action for symbol:",symbol);

        const quantity = prompt("Enter the quantity:");
        if (!quantity || isNaN(quantity) || quantity <= 0) {
            alert("Please enter a valid quantity.");
            return;
        }

        // Get the current user's username (from localStorage)
        const userName = sessionStorage.getItem('userName');
        if (!userName) {
            alert("User is not authenticated. Please log in.");
            return;
        }

        // Get the company name from the row's data attribute
        const row = document.querySelector(`tr[data-symbol="${symbol.trim()}"]`);
        console.log("selected row:", row);
        if(!row){
            alert("Row for the selected stock not found");
        }
        const companyName = document.querySelector(`tr[data-symbol="${symbol}"]`).getAttribute('data-company-name');
        console.log("Company Name is sent:", companyName);
        const priceCell = row.querySelector('td:nth-child(3)');

        if(!priceCell){
            console.log("Error: Price cell not found.");
            alert("Error fetching the price.");
            return;
        }

        const price = priceCell ?  parseFloat(row.children[2].innerText.replace('$', '')) : null;
        
        if (!companyName || !price) {
            alert("Company name or price is not defined.");
            return;
        }

        // Prepare the order data
        const orderData = {
            "companyName": companyName,
            "orderType": action.toUpperCase(),  // BUY or SELL
            "quantity": parseInt(quantity),
            "userName": sessionStorage.getItem("userName"),  // Pass the username in the request
            "price": price,
            "totalPrice": (price * parseInt(quantity)).toFixed(2)

        };

        console.log("Order data being sent ", JSON.stringify(orderData, null, 2));

        try {
            const response = await fetch('http://localhost:8081/api/orders/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });

            if (response.ok) {
                alert("Order placed successfully!");
            } else {
                const errorMsg = await response.text();  // Read error message
                //console.log("Error:",error)
                alert(`Failed to place order. Server response: ${errorMsg}`);
            }
        } catch (error) {
            console.error('Error placing order:', error);
        }
    };

    // Handle logout
    document.getElementById('logoutButton').addEventListener('click', () => {
        alert('Logging out...');
        // Implement logout logic here
        sessionStorage.removeItem('userName'); // Clear username on logout
        window.location.href = './login.html';
    });

    // Fetch and display stock data when the page loads
    fetchStockData();
// Display investor's name
function displayInvestorName() {
    const investorNameElement = document.getElementById('investorName');
    const userName = sessionStorage.getItem('userName'); // Fetch username from sessionStorage
    if (userName) {
        investorNameElement.textContent = `Welcome, ${userName}`;
    } else {
        investorNameElement.textContent = 'Welcome, Guest';
    }
}

// Display the investor's name when the page loads
displayInvestorName();
});

// Function to get the current user's username
function getCurrentUserName() {
const userName = sessionStorage.getItem('userName'); // Fetch username from sessionStorage
console.log("Fetched userName: ", userName);
return userName; // This assumes you store the username in sessionStorage
}