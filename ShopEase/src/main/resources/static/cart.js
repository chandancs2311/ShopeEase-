const token = localStorage.getItem("token");
const API_BASE = "http://localhost:8080/api/user";

//  Add to Cart
function addToCart(productId, quantity = 1) {
    fetch(`${API_BASE}/cart/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ productId, quantity })
    })
    .then(res => res.json())
    .then(data => alert("Item added to cart!"))
    .catch(err => console.error(err));
}

//  View Cart Items
function loadCartItems() {
    fetch(`${API_BASE}/cart/view`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(cartItems => {
        const cartContainer = document.getElementById("cartItems");
        cartContainer.innerHTML = "";
        cartItems.forEach(item => {
            cartContainer.innerHTML += `
                <div class="cart-item">
                    <p>${item.productName} - Qty: ${item.quantity}</p>
                    <button onclick="removeFromCart(${item.id})">Remove</button>
                </div>
            `;
        });
    });
}

// Remove Item
function removeFromCart(cartItemId) {
    fetch(`${API_BASE}/cart/remove/${cartItemId}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(() => loadCartItems());
}
