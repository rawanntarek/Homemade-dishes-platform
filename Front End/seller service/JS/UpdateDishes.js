document.addEventListener("DOMContentLoaded", function () {
    const updateDishForm = document.getElementById('updateDishForm');
    const messageElement = document.getElementById('message');

    updateDishForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevents the form from submitting normally (page reload)

        const dishName = document.getElementById('dishName').value;
        const newName = document.getElementById('newName').value;
        const newPrice = document.getElementById('newPrice').value;
        const newAmount = document.getElementById('newAmount').value;

        // Validation: Ensure the dish name is entered
        if (!dishName) {
            messageElement.innerText = 'Dish name is required.';
            return;
        }

        const companyName = localStorage.getItem('companyName');  // Ensure the user is logged in

        if (!companyName) {
            messageElement.innerText = 'Please log in first.';
            return;
        }

        // Prepare the request body with the updated fields
        const dish = {
            name: newName || undefined,
            price: newPrice ? parseFloat(newPrice) : undefined,
            amount: newAmount ? parseInt(newAmount) : undefined
        };

        // Send the PUT request to update the dish
        fetch(`http://localhost:8082/seller-service/api/sellers/updateDish?companyName=${companyName}&dishName=${dishName}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dish)
        })
            .then(response => {
                if (response.ok) {
                    // If the response is OK (status 200-299)
                    messageElement.innerText = 'Dish updated successfully.';
                    updateDishForm.reset();
                } else {
                    // If the response is not OK (status 4xx or 5xx)
                    messageElement.innerText = 'Failed to update dish. Please try again.';
                }
            })
            .catch(error => {
                messageElement.innerText = 'Error updating the dish. Please try again.';
                console.error('Error:', error);
            });
    });
});
