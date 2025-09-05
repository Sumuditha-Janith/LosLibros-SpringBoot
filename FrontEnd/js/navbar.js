// navbar.js - Handle navigation bar functionality
$(document).ready(function() {
    // Load navbar dynamically
    $('#navbar-container').load('navbar.html', function() {
        const token = localStorage.getItem('token');
        const username = localStorage.getItem('username');
        const role = localStorage.getItem('role');
        
        if (token && username && role) {
            $('#userInfoNav').text(`Welcome, ${username} (${role})`);
            
            // Set up logout functionality
            $('#logoutBtnNav').on('click', function(e) {
                e.preventDefault();
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('role');
                window.location.href = 'index.html';
            });
            
            // Hide links based on user role
            if (role === 'USER') {
                $('#booksLink, #authorsLink, #categoriesLink').hide();
            }
        } else {
            window.location.href = 'signIn.html';
        }
    });
});