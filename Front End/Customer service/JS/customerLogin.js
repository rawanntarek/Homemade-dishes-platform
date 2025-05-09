function loginCustomer(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        document.getElementById('message').innerText = 'Please enter both username and password.';
        return false;
    }

    const customer = { username, password };

    fetch('http://localhost:8083/customer-service/api/customers/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer)
    })
    .then(response => {
        if (response.ok) {
            alert("Login successful!");
            document.getElementById('message').innerText = 'Login successful!';
            localStorage.setItem('username', username);  
            window.location.href = "customerDashboard.html";  
        } else {
            document.getElementById('message').innerText = 'Login failed. Invalid username or password.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').innerText = 'Login failed. Please try again.';
    });

    return false;  
}
