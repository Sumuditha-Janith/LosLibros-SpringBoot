const API_BASE_URL = 'http://localhost:8080';

function getAuthHeaders(contentType = 'application/json') {
    const token = localStorage.getItem('token');
    const headers = {
        'Authorization': 'Bearer ' + token
    };

    if (contentType) {
        headers['Content-Type'] = contentType;
    }

    return headers;
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
    } else if (xhr.status === 404) {
        alert('Requested resource not found.');
        return true;
    } else if (xhr.status >= 500) {
        alert('Server error. Please try again later.');
        return true;
    }
    return false;
}

function makeApiCall(url, method, data = null, isText = false) {
    return new Promise((resolve, reject) => {
        const contentType = isText ? 'text/plain' : 'application/json';
        const config = {
            url: API_BASE_URL + url,
            method: method,
            headers: getAuthHeaders(contentType),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                if (!handleApiError(xhr)) {
                    const errorMsg = xhr.responseJSON?.message || 'An error occurred';
                    reject(errorMsg);
                }
            }
        };

        if (data) {
            config.data = isText ? data : JSON.stringify(data);
        }

        $.ajax(config);
    });
}