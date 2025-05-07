function createDish(event) {
    event.preventDefault(); 
    
    const dishName = document.getElementById('dishName').value;
    const amount = document.getElementById('amount').value;
    const price = document.getElementById('price').value;

    if (!dishName || !amount || !price) {
        document.getElementById('message').innerText = 'All fields are required!';
        return;
    }

    const dish = {
        name: dishName,
        amount: parseInt(amount),
        price: parseFloat(price)
    };

    fetch('http://localhost:8082/seller-service/api/sellers/addDish', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', 
        },
        body: JSON.stringify(dish)  
    })
    .then(response => {
        if (response.ok) {  
            document.getElementById('message').innerText = 'Dish added successfully!';
            document.getElementById('createDishForm').reset();  
        } else {
            document.getElementById('message').innerText = 'Failed to add dish. Please try again.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').innerText = 'Failed to add dish. Please try again.';
    });
}
