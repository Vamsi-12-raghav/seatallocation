document.getElementById('registrationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const fullName = document.getElementById('fullName').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const department = document.getElementById('department').value;
    
    const messageDiv = document.getElementById('message');
    
    // Validation
    if (!fullName || !username || !email || !password || !confirmPassword || !department) {
        showMessage('All fields are required', 'error', messageDiv);
        return;
    }
    
    if (password !== confirmPassword) {
        showMessage('Passwords do not match', 'error', messageDiv);
        return;
    }
    
    if (password.length < 6) {
        showMessage('Password must be at least 6 characters long', 'error', messageDiv);
        return;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showMessage('Please enter a valid email address', 'error', messageDiv);
        return;
    }
    
    try {
        const response = await fetch('/api/admin/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fullName: fullName,
                username: username,
                email: email,
                password: password,
                department: department
            })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            showMessage(`Admin registered successfully! Admin ID: ${data.adminId}`, 'success', messageDiv);
            
            // Create a credentials display
            const credentialsInfo = `
                <h3 style="margin-top: 20px;">Your Credentials:</h3>
                <div style="background: #f9f9f9; padding: 15px; border-radius: 4px; margin-top: 10px;">
                    <p><strong>Username:</strong> ${data.username}</p>
                    <p><strong>Password:</strong> ${data.password}</p>
                    <p><strong>Email:</strong> ${data.email}</p>
                    <p><strong>Full Name:</strong> ${data.fullName}</p>
                    <p><strong>Department:</strong> ${data.department}</p>
                    <p style="color: red; font-weight: bold;">Please save these credentials securely!</p>
                </div>
            `;
            document.getElementById('registrationForm').style.display = 'none';
            messageDiv.innerHTML += credentialsInfo;
            
            // Reset form
            setTimeout(() => {
                document.getElementById('registrationForm').reset();
            }, 2000);
        } else {
            showMessage(data.message || 'Registration failed', 'error', messageDiv);
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred during registration. Please try again.', 'error', messageDiv);
    }
});

function showMessage(message, type, element) {
    element.textContent = message;
    element.className = `message ${type}`;
    
    if (type === 'success') {
        setTimeout(() => {
            element.classList.remove(type);
        }, 5000);
    }
}
