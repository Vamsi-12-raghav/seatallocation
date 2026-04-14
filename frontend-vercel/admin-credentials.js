// Tab switching
document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const tabName = btn.dataset.tab;
        
        // Hide all tabs
        document.querySelectorAll('.tab-content').forEach(content => {
            content.classList.remove('active');
        });
        
        // Remove active class from all buttons
        document.querySelectorAll('.tab-btn').forEach(b => {
            b.classList.remove('active');
        });
        
        // Show selected tab
        document.getElementById(tabName).classList.add('active');
        btn.classList.add('active');
        
        // Clear credentials display
        document.getElementById('credentialsDisplay').classList.remove('show');
        document.getElementById('message').textContent = '';
        document.getElementById('message').className = 'message';
    });
});

async function searchByUsername() {
    const username = document.getElementById('usernameInput').value.trim();
    
    if (!username) {
        showMessage('Please enter a username', 'error');
        return;
    }
    
    try {
        const response = await fetch(`/api/admin/credentials/username/${encodeURIComponent(username)}`);
        const data = await response.json();
        
        if (response.ok) {
            displayCredentials([data]);
            showMessage('Credentials found successfully', 'success');
        } else {
            showMessage(data.message || 'Username not found', 'error');
            document.getElementById('credentialsDisplay').classList.remove('show');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred while searching', 'error');
    }
}

async function searchByEmail() {
    const email = document.getElementById('emailInput').value.trim();
    
    if (!email) {
        showMessage('Please enter an email', 'error');
        return;
    }
    
    try {
        const response = await fetch(`/api/admin/credentials/email/${encodeURIComponent(email)}`);
        const data = await response.json();
        
        if (response.ok) {
            displayCredentials([data]);
            showMessage('Credentials found successfully', 'success');
        } else {
            showMessage(data.message || 'Email not found', 'error');
            document.getElementById('credentialsDisplay').classList.remove('show');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred while searching', 'error');
    }
}

async function getAllAdmins() {
    try {
        const response = await fetch('/api/admin/list/active');
        const data = await response.json();
        
        if (response.ok && data.length > 0) {
            displayCredentials(data);
            showMessage(`Found ${data.length} active admin(s)`, 'success');
        } else {
            showMessage('No active admins found', 'error');
            document.getElementById('credentialsDisplay').classList.remove('show');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred while fetching admins', 'error');
    }
}

function displayCredentials(admins) {
    const content = document.getElementById('credentialsContent');
    content.innerHTML = '';
    
    admins.forEach(admin => {
        const adminDiv = document.createElement('div');
        adminDiv.style.marginBottom = '20px';
        adminDiv.style.padding = '15px';
        adminDiv.style.backgroundColor = 'white';
        adminDiv.style.borderRadius = '4px';
        adminDiv.style.border = '1px solid #ddd';
        
        const createdDateMs = parseInt(admin.createdAt);
        const createdDate = new Date(createdDateMs).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        
        adminDiv.innerHTML = `
            <div class="credential-item">
                <strong>Admin ID:</strong> 
                <span class="credential-value">${admin.adminId}</span>
            </div>
            <div class="credential-item">
                <strong>Full Name:</strong> 
                <span class="credential-value">${admin.fullName}</span>
            </div>
            <div class="credential-item">
                <strong>Username:</strong> 
                <span class="credential-value">${admin.username}</span>
                <button class="copy-btn" onclick="copyToClipboard('${admin.username}')">Copy</button>
            </div>
            <div class="credential-item">
                <strong>Password:</strong> 
                <span class="credential-value">${maskPassword(admin.password)}</span>
                <button class="copy-btn" onclick="copyToClipboard('${admin.password}')">Copy</button>
            </div>
            <div class="credential-item">
                <strong>Email:</strong> 
                <span class="credential-value">${admin.email}</span>
                <button class="copy-btn" onclick="copyToClipboard('${admin.email}')">Copy</button>
            </div>
            <div class="credential-item">
                <strong>Department:</strong> 
                <span class="credential-value">${admin.department}</span>
            </div>
            <div class="credential-item">
                <strong>Status:</strong> 
                <span class="credential-value" style="color: ${admin.active ? 'green' : 'red'};">
                    ${admin.active ? 'Active' : 'Inactive'}
                </span>
            </div>
            <div class="credential-item">
                <strong>Created At:</strong> 
                <span class="credential-value">${createdDate}</span>
            </div>
        `;
        
        content.appendChild(adminDiv);
    });
    
    document.getElementById('credentialsDisplay').classList.add('show');
}

function maskPassword(password) {
    if (password.length <= 2) {
        return '*'.repeat(password.length);
    }
    return password.charAt(0) + '*'.repeat(password.length - 2) + password.charAt(password.length - 1);
}

function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showMessage('Copied to clipboard!', 'success');
    }).catch(() => {
        showMessage('Failed to copy', 'error');
    });
}

function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    
    if (type === 'success') {
        setTimeout(() => {
            messageDiv.classList.remove(type);
        }, 3000);
    }
}

// Allow Enter key to trigger search
document.getElementById('usernameInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') searchByUsername();
});

document.getElementById('emailInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') searchByEmail();
});
