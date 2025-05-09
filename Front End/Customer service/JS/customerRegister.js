function registerCustomer(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        document.getElementById('message').innerText = 'Please enter both username and password.';
        return false;
    }

    const customer = { username, password };

    fetch('http://localhost:8083/customer-service/api/customers/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer)
    })
    .then(response => {
        if (response.ok) {
            alert("Register successful!");
            document.getElementById('message').innerText = 'Register successful!';
            localStorage.setItem('username', username);  
            window.location.href = "CustomerLogin.html";  
        } else {
            document.getElementById('message').innerText = 'Register failed. username already exists.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').innerText = 'Login failed. Please try again.';
    });

    return false;  
}
