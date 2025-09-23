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

function makeApiCall(url, method, data = null) {
    return new Promise((resolve, reject) => {
        const token = localStorage.getItem('token');

        if (!token && !url.includes('/auth/')) {
            const error = new Error('No authentication token found. Please login again.');
            reject(error);
            window.location.href = 'signIn.html';
            return;
        }

        const config = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        };

        if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
            config.body = JSON.stringify(data);
        }

        console.log('Making API call:', method, url, data);

        fetch(API_BASE_URL + url, config)
            .then(async response => {
                const text = await response.text();
                let responseData;

                try {
                    responseData = text ? JSON.parse(text) : {};
                } catch (e) {
                    console.error('Failed to parse JSON response:', text);
                    const error = new Error('Invalid JSON response from server');
                    error.status = response.status;
                    error.responseText = text;
                    reject(error);
                    return;
                }

                console.log('API response:', response.status, responseData);

                if (response.ok) {
                    resolve(responseData);
                } else {
                    const error = new Error(responseData.message || response.statusText);
                    error.status = response.status;
                    error.responseData = responseData;
                    reject(error);
                }
            })
            .catch(error => {
                console.error('API call failed:', error);
                if (error.name === 'TypeError' && error.message.includes('fetch')) {
                    reject(new Error('Network error: Unable to connect to server'));
                } else {
                    reject(error);
                }
            });
    });
}