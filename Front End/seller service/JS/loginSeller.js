function loginSeller(event) {
    event.preventDefault();

    const companyName = document.getElementById('companyName').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!companyName || !password) {
        document.getElementById('message').innerText = 'Please enter both company name and password.';
        return false;
    }

    const seller = { companyName, password };

    fetch('http://localhost:8082/seller-service/api/sellers/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(seller)
    })
        .then(response => response.json())
        .then(result => {
            // Check for specific success or error messages
            if (result.success) {
                alert("Login successful!");
                document.getElementById('message').innerText = 'Login successful!';
                // Redirect or update UI
            } else {
                document.getElementById('message').innerText = result.message || 'Login failed. Please try again.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('message').innerText = 'Login failed. Please try again.';
        });

    return false;
}
