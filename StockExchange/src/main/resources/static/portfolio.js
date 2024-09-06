document.addEventListener('DOMContentLoaded', async () => {
    const tableBody = document.querySelector('table tbody');
    const searchInput = document.getElementById('search');

    // Function to fetch current stock prices
    async function fetchStockPrices() {
        console.log("-------from stock price--------")
        try {

            const urlEncodedData = new URLSearchParams({
                username: sessionStorage.getItem("userName"),
                beginDate: "",
                endDate: ""
              }).toString();

            //   ?userName=${encodeURIComponent(userName)}
            const response = await fetch('http://localhost:8081/api/orders/history',{
                method: "POST",
                headers: {
                  "Content-Type": "application/x-www-form-urlencoded",
                },
                body: urlEncodedData,
              });
            const data = await response.json();

            if (data.statusCode === 200) {
                // Return the stock data as an array
                return data.data;
            } else {
                console.error('Error fetching stock prices:', data.message);
                return [];
            }
        } catch (error) {
            console.error('Error:', error);
            return [];
        }
    }

    // Function to fetch portfolio data
    async function fetchPortfolioData() {
        try {
            const response = await fetch('http://localhost:8081/holdings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"userName":sessionStorage.getItem("userName"), "role": "investor"})
            });
            const portfolio = await response.json();

            console.log(portfolio);
            if (portfolio.statusCode === 200) {
                const stockPrices = await fetchStockPrices();
                populatePortfolioTable(portfolio, stockPrices);
            } else {
                console.error('No portfolio data found.');
                tableBody.innerHTML = '<tr><td colspan="8">No portfolio data available.</td></tr>';
            }
        } catch (error) {
            console.error('Error fetching portfolio data:', error);
            alert('Failed to load portfolio data.');
        }
    }

    // Function to populate the table with portfolio data and calculated values
    function populatePortfolioTable(portfolio, stockPrices) {
        const stockPriceMap = new Map(stockPrices.map(stock => [stock.symbol, stock.currentPrice]));
        tableBody.innerHTML = ''; // Clear existing data

        portfolio.forEach((item, index) => {
            const currentPrice = stockPriceMap.get(item.companyName) || 0;
            const currentValue = (currentPrice * item.noOfShares).toFixed(2);

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${item.companyName}</td>
                <td>${item.noOfShares}</td>
                <td>${item.averagePrice.toFixed(2)}</td>
                <td>${(item.averagePrice * item.noOfShares).toFixed(2)}</td>
                <td>$${currentValue}</td>
                <td>${(((currentPrice - item.averagePrice) / item.averagePrice) * 100).toFixed(2)}%</td>
                <td>
                    <button class="action-btn" data-action="Buy">Buy</button>
                    <button class="action-btn" data-action="Sell">Sell</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
        
    }

    // Search functionality
    searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const tableRows = document.querySelectorAll('table tbody tr');

        tableRows.forEach(row => {
            const cells = row.getElementsByTagName('td');
            let match = false;

            for (let i = 1; i < cells.length - 1; i++) { // Exclude the first cell (Serial No.) and the last cell (Action column)
                if (cells[i].textContent.toLowerCase().includes(searchTerm)) {
                    match = true;
                    break;
                }
            }

            row.style.display = match ? '' : 'none';
        });
    });

    // Handle Buy and Sell button clicks
    tableBody.addEventListener('click', async (event) => {
        const button = event.target;

        if (button.classList.contains('action-btn')) {
            const action = button.dataset.action; // Get button action (Buy/Sell)
            const row = button.closest('tr');
            const cells = row.getElementsByTagName('td');

            const transaction = {
                companyName: cells[1].textContent,
                sharePrice: parseFloat(cells[4].textContent.replace('$', '').trim()),
                quantity: parseInt(cells[2].textContent),
                totalPrice: parseFloat(cells[5].textContent.replace('$', '').trim())
            };

            try {
                const endpoint = action === 'Buy' ? '/api/portfolio/buy' : '/api/portfolio/sell';
                await fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(transaction),
                });
                alert(`${action} ${transaction.quantity} shares of ${transaction.companyName} at $${transaction.sharePrice} each.`);
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred while processing the transaction.');
            }
        }
    });

    // Fetch and display portfolio data when the page loads
    fetchPortfolioData();
});
