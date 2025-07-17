const API_BASE = 'http://localhost:8080/api/user';

// GET JWT token from localStorage
function getToken() {
  return localStorage.getItem('token');
}

// LOGIN FUNCTION
async function loginUser(event) {
  event.preventDefault();

  const email = document.querySelector('.username').value;
  const password = document.querySelector('.password').value;

  const payload = { email, password };

  try {
    const res = await fetch(`${API_BASE}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      alert("Login failed!");
      return;
    }

    const data = await res.json();
    const token = data.token;
    const role = data.role;

    localStorage.setItem('token', token);
    localStorage.setItem('role', role);

    // Redirect based on role
    if (role === 'ROLE_ADMIN') {
      window.location.href = 'admin.html';
    } else if (role === 'ROLE_USER') {
      window.location.href = 'home.html';
    } else {
      alert("Invalid role.");
    }

  } catch (err) {
    console.error(err);
    alert("Server error during login.");
  }
}

// REGISTER FUNCTION
async function registerUser(event) {
  event.preventDefault();

  const name = document.querySelector('.name').value;
  const email = document.querySelector('.email').value;
  const password = document.querySelector('.password').value;
  const role = document.querySelector('.role').value.toLowerCase();

  const payload = { name, email, password, role };

  try {
    const res = await fetch(`${API_BASE}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (res.ok) {
      alert("Registered successfully! Please login.");
      window.location.href = 'login.html';
    } else {
      const msg = await res.text();
      alert("Registration failed: " + msg);
    }

  } catch (err) {
    console.error(err);
    alert("Server error during registration.");
  }
}
