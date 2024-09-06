// Handle File Upload Form Submission
document.querySelector('.file-upload form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent the default form submission

    const fileInput = document.getElementById('file');
    if (!fileInput.files.length) {
        alert('Please select a file to upload.');
        return; // Exit the function if no file is selected
    }

    const file = fileInput.files[0];

    try {
        // Create a FormData object and append the file
        const formData = new FormData();
        formData.append('file', file);

        // Send the file to the server
        const response = await fetch('http://localhost:8081/admin/upload', {
            method: 'POST',
            body: formData // Use FormData as the body of the request
        });

        const result = await response.text(); // Read the response from the server
        if (response.ok) {
            // File uploaded successfully
            alert('File uploaded successfully: ' + result);
        } else {
            // File upload failed
            alert('File upload failed: ' + result);
        }
    } catch (error) {
        // Handle any errors during the fetch operation
        console.error('Error uploading file:', error);
        alert('Error uploading file');
    }
});

document.getElementById("processOrdersBtn").addEventListener("click", async () => {

    console.log("Compute button is clicked!");
    try {
        const response = await fetch('http://localhost:8081/admin/process-orders', {
            method: 'POST',
        });

        if (response.ok) {
            alert("Pending orders processed successfully!");
        } else {
            const errorMsg = await response.text();  // Read error message from server
            alert("Failed to process orders: " + errorMsg);
        }
    } catch (error) {
        console.error("Error processing orders:", error);
        alert("Error processing orders.");
    }
});


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
                
            `;

            tableBody.appendChild(row);
        });
    }
});
  