const API = 'http://localhost:8080';
let token = localStorage.getItem('academy_token');

window.onload = () => {
    if (token) showDashboard();
};

// Tab switching
function switchTab(tab) {
    document.getElementById('login-tab').style.display = tab === 'login' ? 'block' : 'none';
    document.getElementById('register-tab').style.display = tab === 'register' ? 'block' : 'none';
    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', (i === 0 && tab === 'login') || (i === 1 && tab === 'register'));
    });
}

// Auth
async function login() {
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    try {
        const res = await fetch(`${API}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (data.token) {
            token = data.token;
            localStorage.setItem('academy_token', token);
            showDashboard();
        } else {
            document.getElementById('login-error').textContent = data.error || 'Login failed';
        }
    } catch (e) {
        document.getElementById('login-error').textContent = 'Connection error';
    }
}

async function register() {
    const body = {
        username: document.getElementById('reg-username').value,
        password: document.getElementById('reg-password').value,
        email: document.getElementById('reg-email').value,
        firstName: document.getElementById('reg-firstname').value,
        lastName: document.getElementById('reg-lastname').value
    };
    try {
        const res = await fetch(`${API}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();
        if (data.token) {
            token = data.token;
            localStorage.setItem('academy_token', token);
            showDashboard();
        } else {
            document.getElementById('register-error').textContent = data.error || 'Registration failed';
        }
    } catch (e) {
        document.getElementById('register-error').textContent = 'Connection error';
    }
}

function logout() {
    token = null;
    localStorage.removeItem('academy_token');
    document.getElementById('auth-section').style.display = 'flex';
    document.getElementById('dashboard-section').style.display = 'none';
}

function showDashboard() {
    document.getElementById('auth-section').style.display = 'none';
    document.getElementById('dashboard-section').style.display = 'block';
    showPage('courses');
}

// Navigation
function showPage(page) {
    document.querySelectorAll('.page').forEach(p => p.style.display = 'none');
    document.getElementById(`page-${page}`).style.display = 'block';
    if (page === 'courses') loadCourses();
    if (page === 'enrolments') loadEnrolments();
    if (page === 'profile') loadProfile();
    if (page === 'graduation') loadGraduation();
}

// API calls
async function authFetch(url, options = {}) {
    return fetch(url, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
            ...options.headers
        }
    });
}

async function loadCourses() {
    const res = await authFetch(`${API}/api/courses`);
    const courses = await res.json();
    document.getElementById('courses-list').innerHTML = courses.map(c => `
        <div class="course-card">
            <h3>${c.title}</h3>
            <p>${c.description}</p>
            <div class="course-meta">
                <span class="badge">${c.credits} credits</span>
                <span class="badge fee">£${c.fee.toFixed(2)}</span>
            </div>
            <button class="btn-enrol" onclick="enrol(${c.id})">Enrol Now</button>
        </div>
    `).join('');
}

async function enrol(courseId) {
    try {
        const res = await authFetch(`${API}/api/enrolments`, {
            method: 'POST',
            body: JSON.stringify({ courseId })
        });
        const data = await res.json();
        if (data.error) {
            alert(data.error);
        } else {
            alert('Successfully enrolled!');
            showPage('enrolments');
        }
    } catch (e) {
        alert('Enrolment failed');
    }
}

async function loadEnrolments() {
    const res = await authFetch(`${API}/api/enrolments`);
    const data = await res.json();
    if (data.error) {
        document.getElementById('enrolments-list').innerHTML = `<p style="color:#94a3b8">${data.error}</p>`;
        return;
    }
    document.getElementById('enrolments-list').innerHTML = data.length === 0
        ? `<p style="color:#94a3b8">No enrolments yet. Go enrol in a course!</p>`
        : data.map(e => `
            <div class="enrolment-card">
                <h3>${e.course.title}</h3>
                <p>${e.course.description}</p>
                <p style="margin-top:6px">Credits: ${e.course.credits} | Fee: £${e.course.fee.toFixed(2)}</p>
                <span class="status-badge">${e.status}</span>
            </div>
        `).join('');
}

async function loadProfile() {
    const res = await authFetch(`${API}/api/profile`);
    const data = await res.json();
    if (data.error) {
        document.getElementById('profile-info').innerHTML = `<p style="color:#f87171">${data.error}</p>`;
        return;
    }
    document.getElementById('profile-info').innerHTML = `
        <div class="info-row"><span>Student ID</span><span>${data.studentId}</span></div>
        <div class="info-row"><span>First Name</span><span>${data.firstName || '—'}</span></div>
        <div class="info-row"><span>Last Name</span><span>${data.lastName || '—'}</span></div>
        <div class="info-row"><span>Username</span><span>${data.username}</span></div>
        <div class="info-row"><span>Email</span><span>${data.email}</span></div>
    `;
    document.getElementById('update-firstname').value = data.firstName || '';
    document.getElementById('update-lastname').value = data.lastName || '';
}

async function updateProfile() {
    const res = await authFetch(`${API}/api/profile`, {
        method: 'PUT',
        body: JSON.stringify({
            firstName: document.getElementById('update-firstname').value,
            lastName: document.getElementById('update-lastname').value
        })
    });
    const data = await res.json();
    if (data.error) {
        document.getElementById('profile-msg').textContent = data.error;
        document.getElementById('profile-msg').style.color = '#f87171';
    } else {
        document.getElementById('profile-msg').textContent = 'Profile updated successfully!';
        loadProfile();
    }
}

async function loadGraduation() {
    const res = await authFetch(`${API}/api/graduation`);
    const data = await res.json();
    const eligible = data.eligibleToGraduate;
    document.getElementById('graduation-result').className =
        `graduation-card ${eligible ? 'grad-eligible' : 'grad-not-eligible'}`;
    document.getElementById('graduation-result').innerHTML = `
        <div class="grad-icon">${eligible ? '🎓' : '⚠️'}</div>
        <div class="grad-id">Student ID: ${data.studentId}</div>
        <div class="grad-message" style="color:${eligible ? '#4ade80' : '#f87171'}">
            ${data.message}
        </div>
    `;
}