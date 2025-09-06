$(document).ready(function() {
    // Load navbar dynamically
    $('#navbar-container').load('navbar.html', function() {
        const token = localStorage.getItem('token');
        const username = localStorage.getItem('username');
        const role = localStorage.getItem('role');

        if (token && username && role) {
            $('#userInfoNav').text(`Welcome, ${username} (${role})`);

            // Set up dashboard link based on user role
            setupDashboardLink(role);

            // Set active link based on current page
            setActiveLink();

            // Hide links based on user role
            updateNavigationBasedOnRole(role);

            // Set up logout functionality
            $('#logoutBtnNav').on('click', function(e) {
                e.preventDefault();
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('role');
                window.location.href = 'index.html';
            });

        } else {
            window.location.href = 'signIn.html';
        }
    });
});

function setupDashboardLink(role) {
    const dashboardLink = $('#dashboardLink');

    // Set the correct href based on role
    switch(role) {
        case 'ADMIN':
            dashboardLink.attr('href', 'adminDashboard.html');
            dashboardLink.text('Admin Dashboard');
            break;
        case 'EMPLOYEE':
            dashboardLink.attr('href', 'employeeDashboard.html');
            dashboardLink.text('Employee Dashboard');
            break;
        case 'USER':
            dashboardLink.attr('href', 'userDashboard.html');
            dashboardLink.text('User Dashboard');
            break;
        default:
            dashboardLink.attr('href', 'signIn.html');
            dashboardLink.text('Dashboard');
    }
}

function setActiveLink() {
    const currentPage = window.location.pathname.split('/').pop();

    // Remove active class from all links
    $('.nav-link').removeClass('active');

    // Set active class based on current page
    switch(currentPage) {
        case 'adminDashboard.html':
        case 'employeeDashboard.html':
        case 'userDashboard.html':
            $('#dashboardLink').addClass('active');
            break;
        case 'books.html':
            $('#booksLink').addClass('active');
            break;
        case 'authors.html':
            $('#authorsLink').addClass('active');
            break;
        case 'categories.html':
            $('#categoriesLink').addClass('active');
            break;
        case 'users.html':
            $('#usersLink').addClass('active');
            break;
    }
}

function updateNavigationBasedOnRole(role) {
    // Hide admin-only elements for non-admin users
    if (role !== 'ADMIN') {
        $('.admin-only').hide();
    }

    // Hide management elements for regular users
    if (role === 'USER') {
        $('#authorsLink, #categoriesLink').hide();

        // Change text for user role
        $('#booksLink').text('Browse Books');
    } else {
        // For admin and employee, show management text
        $('#booksLink').text('Manage Books');
        $('#authorsLink').text('Manage Authors');
        $('#categoriesLink').text('Manage Categories');
    }
}