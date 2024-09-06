async function handleSignup() {
  // Retrieve form data
  const firstName = document.getElementById('first-name').value;
  const lastName = document.getElementById('last-name').value;
  const email = document.getElementById('email').value;
  const phone = document.getElementById('phone').value;
  const pan = document.getElementById('pan').value;
  const city = document.getElementById('city').value;
  const username = document.getElementById('username-signup').value;
  const password = document.getElementById('password-signup').value;



  // Simple validation
  if (!firstName || !lastName || !email || !phone || !pan || !city || !username || !password) {
      alert('Please fill in all fields.');
      return;
  }

  // Prepare the request
  const response = await fetch('http://localhost:8081/user/register', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({
          userName: username,
          fullName: `${firstName} ${lastName}`,
          city: city, // Adjust if needed
          phoneNumber: phone,
          password: password,
          emailAddress: email,
          role:"investor",
          panNo: pan,
          
      }),
  });

  const result = await response.json();

  if (result.statusCode === 200) {
      alert('Registration successful! You can now login.');
      window.location.href = 'login1.html'; // Redirect to login page
  } else {
      alert(result.message || 'Registration failed. Please try again.');
  }
}

