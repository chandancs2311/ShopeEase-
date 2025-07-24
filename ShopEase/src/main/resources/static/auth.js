const API_BASE = 'http://localhost:8080/api/user'; // Change if different

//  LOGIN FUNCTION
async function loginUser(event) {
  event.preventDefault();

  const username = document.querySelector('.username').value;
  const password = document.querySelector('.password').value;

  const payload = {
    email: username,   // assuming email as username
    password: password
  };

  try {
    const res = await fetch(`${API_BASE}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errMsg = await res.text(); // Get actual error
      console.error("Login failed:", errMsg);
      alert("Login failed: " + errMsg);
      return;
    }

    const data = await res.json();
    const token = data.token;

    localStorage.setItem('token', token);
    localStorage.setItem('role', data.role);

    if (data.role === 'ROLE_ADMIN') {
      window.location.href = 'admin.html';
    } else {
      window.location.href = 'home.html';
    }

  } catch (err) {
    console.error(err);
    alert("Server error.");
  }
}

//  REGISTER FUNCTION
async function registerUser(event) {
  event.preventDefault();


  const password = document.querySelector('.password').value;
  const name     = document.querySelector('.name').value;
  const email    = document.querySelector('.email').value;
  const role     = document.querySelector('.role').value;

  const payload = {
    name: name,
    email: email,
    password: password,
    role: role.toLowerCase()
  };


  try {
    const res = await fetch(`${API_BASE}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

   const msg = await res.text();

   if (res.ok) {
     alert(msg); //  Use it directly for success
     window.location.href = 'login.html';
   } else {
     alert("Registration failed: " + msg);
   }


  } catch (err) {
    console.error(err);
    alert("Server error.");
  }
}