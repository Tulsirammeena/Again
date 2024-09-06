async function login(role) {
    // Get form values
    const userName = document.getElementById('userName').value;
    const password = document.getElementById('password').value;

    // Create the request payload
    const loginPayload = {
        userName: userName,
        password: password,
        role: role
    };

    try {
        // Send a POST request to the /user/login endpoint
        const response = await fetch('http://localhost:8081/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginPayload)
        });

        // Parse the JSON response
        const result = await response.json();

        // Check if the login was successful
        if (result.statusCode === 200 && result.data) {
            // Redirect based on the role
            sessionStorage.setItem('userName', userName);    
            //localStorage.setItem('userName', userName);
            if (role === 'investor') {
                window.location.href = "./investor_home.html";
            } else if (role === 'admin') {
                window.location.href = "./admin.html";
            }
        } else {
            // Show error message
            alert(result.message || 'Invalid username or password');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}
