document.addEventListener("DOMContentLoaded", function () {
    const companyName = localStorage.getItem('companyName');
    const container = document.getElementById("soldDishesContainer");

    // Add grid styling for better layout
    container.style.display = 'grid';
    container.style.gridTemplateColumns = 'repeat(auto-fit, minmax(320px, 1fr))';
    container.style.gap = '32px';
    container.style.marginTop = '32px';

    if (!companyName) {
        container.innerHTML = '<p>Please log in first.</p>';
        return;
    }

    fetch(`http://localhost:8082/seller-service/api/sellers/soldDishes?companyName=${companyName}`)
        .then(response => {
            if (!response.ok) {
                container.innerHTML = '<p>No sold dishes found or error occurred.</p>';
                return [];
            }
            return response.json();
        })
        .then(orders => {
            if (!orders || orders.length === 0) {
                container.innerHTML = '<p>No sold dishes found.</p>';
                return;
            }
            orders.forEach(order => {
                const orderDiv = document.createElement("div");
                orderDiv.className = "card";
                orderDiv.style.background = "#fff";
                orderDiv.style.borderRadius = "18px";
                orderDiv.style.boxShadow = "0 4px 16px rgba(0,0,0,0.10)";
                orderDiv.style.padding = "28px 24px";
                orderDiv.style.margin = "0 auto";
                orderDiv.style.maxWidth = "350px";
                orderDiv.style.display = "flex";
                orderDiv.style.flexDirection = "column";
                orderDiv.style.alignItems = "flex-start";
                orderDiv.style.transition = "transform 0.2s";
                orderDiv.onmouseover = () => orderDiv.style.transform = "scale(1.03)";
                orderDiv.onmouseout = () => orderDiv.style.transform = "scale(1)";

                orderDiv.innerHTML = `
                    <div style="margin-bottom: 12px;">
                        <span style="font-size: 1.1em; font-weight: 600; color: #333;">Customer:</span><br>
                        <span style="color: #007b83; font-weight: 500;">${order.customerName}</span>
                    </div>
                    <div style="margin-bottom: 8px; color: #666; font-size: 0.98em;">
                        <span style="font-weight: 500;">Order ID:</span> <span style="font-family: monospace;">${order.orderId}</span>
                    </div>
                    <div style="margin-bottom: 12px; color: #4caf50; font-weight: 600; font-size: 1em;">
                        Status: ${order.status}
                    </div>
                    <div style="width: 100%;">
                        <span style="font-weight: 600; color: #222;">Dishes:</span>
                        <ul style="padding-left: 18px; margin: 8px 0 0 0;">
                            ${order.dishes.map(dish => `
                                <li style="margin-bottom: 6px;">
                                    <span style="font-weight: 500; color: #007b83;">${dish.dishName}</span>
                                    <span style="color: #888;">| Amount:</span> <b>${dish.amount}</b>
                                    <span style="color: #888;">| Price:</span> <b style="color: #4caf50;">$${dish.price}</b>
                                </li>
                            `).join('')}
                        </ul>
                    </div>
                `;
                container.appendChild(orderDiv);
            });
        })
        .catch(error => {
            console.error("Error fetching sold dishes:", error);
            container.innerHTML = '<p>Error loading sold dishes.</p>';
        });
}); 