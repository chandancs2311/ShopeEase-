async function loadCart(userId, cartId) {
  const items = await apiRequest('GET', `/user/cart/items/${userId}/${cartId}`);
  const container = document.getElementById('cartItems');
  container.innerHTML = items.map(item => `
    <div>
      <p>${item.productName} - ${item.quantity} x ₹${item.price}</p>
    </div>
  `).join('');
}
