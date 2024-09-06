document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search');
    const beginDateInput = document.getElementById('begin_date');
    const endDateInput = document.getElementById('end_date');
    const searchByDateButton = document.getElementById('search-by-date');
    const tableBody = document.getElementById('orderTableBody');

    // Function to get the current user's username
    function getCurrentUserName() {
        return sessionStorage.getItem('userName');  // Assuming username is stored in sessionStorage
    }

    function fetchOrderHistory() {
        const userName = getCurrentUserName();
        if (!userName) {
            console.error('No user is logged in.');
            tableBody.innerHTML = '<tr><td colspan="8">Please log in to view order history.</td></tr>';
            return;
        }

        const searchQuery = searchInput.value.trim();
        const beginDate = beginDateInput.value;
        const endDate = endDateInput.value;

        let url = `http://localhost:8081/api/orders/history?userName=${encodeURIComponent(userName)}`;

        // Adding search query if it exists
        if (searchQuery) {
            url += `&search=${encodeURIComponent(searchQuery)}`;
        }

        // Adding date range if dates are provided
        if (beginDate) {
            url += `&begin_date=${beginDate}`;
        }
        if (endDate) {
            url += `&end_date=${endDate}`;
        }

        console.log('Fetching data from:', url);

        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    populateOrderTable(data);
                } else {
                    tableBody.innerHTML = '<tr><td colspan="8">No orders found.</td></tr>';
                }
            })
            .catch(error => console.error('Error fetching order history:', error));
    }

    function populateOrderTable(orders) {
        tableBody.innerHTML = ''; // Clear existing rows

        orders.forEach(order => {
            const row = document.createElement('tr');

            // Format the data
            const transactionId = order.orderId || 'N/A';
            const transactionType = order.orderType || 'N/A';
            const companyName = order.companyName || 'N/A';
            const date = new Date(order.date).toLocaleDateString() || 'N/A';
            const sharePrice = order.price ? `$${order.price.toFixed(2)}` : 'N/A';
            const quantity = order.quantity || 'N/A';
            const totalPrice = order.totalPrice ? `$${order.totalPrice.toFixed(2)}` : 'N/A';
            const status = order.status || 'N/A';

            row.innerHTML = `
                <td>${transactionId}</td>
                <td>${transactionType}</td>
                <td>${companyName}</td>
                <td>${date}</td>
                <td>${sharePrice}</td>
                <td>${quantity}</td>
                <td>${totalPrice}</td>
                <td>${status}</td>
            `;

            tableBody.appendChild(row);
        });
    }

    // Initial fetch
    fetchOrderHistory();

    // Fetch data when search input changes
    searchInput.addEventListener('input', fetchOrderHistory);

    // Fetch data when date inputs or search by date button is clicked
    searchByDateButton.addEventListener('click', fetchOrderHistory);
    beginDateInput.addEventListener('change', fetchOrderHistory);
    endDateInput.addEventListener('change', fetchOrderHistory);
});
