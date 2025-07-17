const API_BASE = "http://localhost:8080/api/user";
const token = localStorage.getItem("jwt");
const userId = localStorage.getItem("userId");

function getHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
}

// ----------------- ADD TO CART -----------------
async function addToCart(productId) {
    if (!token || !userId) {
        alert("Please login to add to cart.");
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/${userId}/cart`, {
            method: "POST",
            headers: getHeaders(),
            body: JSON.stringify({ productId, quantity: 1 })
        });

        const data = await res.json();
        if (res.ok) {
            alert("Added to cart!");
        } else {
            alert(data.message || "Error adding to cart.");
        }
    } catch (err) {
        alert("Server error while adding to cart.");
    }
}

// ----------------- VIEW CART -----------------
async function viewCart() {
    try {
        const res = await fetch(`${API_BASE}/${userId}/cart`, {
            headers: getHeaders()
        });
        const cartItems = await res.json();

        const cartContainer = document.getElementById("cartContainer");
        cartContainer.innerHTML = "";

        cartItems.forEach(item => {
            const card = document.createElement("div");
            card.className = "cart-item";
            card.innerHTML = `
                <img src="${item.imageUrl}" alt="${item.name}" />
                <h3>${item.name}</h3>
                <p>₹${item.price} x ${item.quantity}</p>
                <button onclick="updateCart(${item.id}, ${item.quantity + 1})">+</button>
                <button onclick="updateCart(${item.id}, ${item.quantity - 1})">-</button>
                <button onclick="removeFromCart(${item.id})">Remove</button>
            `;
            cartContainer.appendChild(card);
        });
    } catch (err) {
        alert("Error loading cart.");
    }
}

// ----------------- UPDATE CART ITEM QUANTITY -----------------
async function updateCart(productId, quantity) {
    if (quantity < 1) {
        removeFromCart(productId);
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/${userId}/cart`, {
            method: "PUT",
            headers: getHeaders(),
            body: JSON.stringify({ productId, quantity })
        });

        if (res.ok) {
            viewCart();
        } else {
            const data = await res.json();
            alert(data.message || "Error updating cart.");
        }
    } catch (err) {
        alert("Server error.");
    }
}

// ----------------- DELETE ITEM FROM CART -----------------
async function removeFromCart(productId) {
    try {
        const res = await fetch(`${API_BASE}/${userId}/cart/${productId}`, {
            method: "DELETE",
            headers: getHeaders()
        });

        if (res.ok) {
            viewCart();
        } else {
            alert("Failed to remove item.");
        }
    } catch (err) {
        alert("Error deleting cart item.");
    }
}

// ----------------- PLACE ORDER -----------------
async function placeOrder() {
    try {
        const res = await fetch(`${API_BASE}/${userId}/order`, {
            method: "POST",
            headers: getHeaders()
        });

        const data = await res.json();
        if (res.ok) {
            alert("Order placed successfully!");
            viewCart(); // clear cart
        } else {
            alert(data.message || "Order failed.");
        }
    } catch (err) {
        alert("Error placing order.");
    }
}

// ----------------- VIEW ORDER HISTORY -----------------
async function viewOrders() {
    try {
        const res = await fetch(`${API_BASE}/${userId}/order`, {
            headers: getHeaders()
        });

        const orders = await res.json();
        const orderContainer = document.getElementById("orderContainer");
        orderContainer.innerHTML = "";

        orders.forEach(order => {
            const card = document.createElement("div");
            card.className = "order-item";
            card.innerHTML = `
                <h4>Order ID: ${order.id}</h4>
                <p>Total: ₹${order.totalAmount}</p>
                <p>Status: ${order.status}</p>
                <button onclick="cancelOrder(${order.id})">Cancel</button>
            `;
            orderContainer.appendChild(card);
        });
    } catch (err) {
        alert("Error fetching orders.");
    }
}

// ----------------- CANCEL ORDER -----------------
async function cancelOrder(orderId) {
    try {
        const res = await fetch(`${API_BASE}/${userId}/order/${orderId}`, {
            method: "DELETE",
            headers: getHeaders()
        });

        if (res.ok) {
            alert("Order cancelled.");
            viewOrders();
        } else {
            alert("Failed to cancel.");
        }
    } catch (err) {
        alert("Server error.");
    }
}
