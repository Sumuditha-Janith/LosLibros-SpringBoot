// api.js - Utility functions for API calls
const API_BASE_URL = 'http://localhost:8080';

function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

function handleApiError(xhr) {
    if (xhr.status === 401) {
        alert('Session expired. Please login again.');
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
        window.location.href = 'signIn.html';
        return true;
    } else if (xhr.status === 403) {
        alert('Access denied. You do not have permission to perform this action.');
        return true;
    }
    return false;
}

function makeApiCall(url, method, data = null) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + url,
            method: method,
            headers: getAuthHeaders(),
            data: data ? JSON.stringify(data) : null,
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                if (!handleApiError(xhr)) {
                    const errorMsg = xhr.responseJSON?.message || 'An error occurred';
                    reject(errorMsg);
                }
            }
        });
    });
}