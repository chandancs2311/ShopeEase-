async function placeOrder() {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Please log in to place an order.");
    window.location.href = "login.html";
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/user/orders/place", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Failed to place order.");
    }

    const result = await response.json();
    alert("Order placed successfully!");

    // Clear cart UI after successful order
    document.getElementById("cartItems").innerHTML = "";
  } catch (err) {
    alert("Error placing order: " + err.message);
  }
}
