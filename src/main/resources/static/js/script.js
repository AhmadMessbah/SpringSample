// ==================== مدیریت تم ====================
function toggleTheme() {
    const body = document.body;
    const newTheme = body.getAttribute('data-bs-theme') === 'dark' ? 'light' : 'dark';
    body.setAttribute('data-bs-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    updateThemeIcon(newTheme);
}

function updateThemeIcon(theme) {
    console.log(theme === 'dark')
    const icon = document.getElementById('themeIcon');
    if (icon) {
        icon.className = theme === 'dark' ? 'fa fa-sun' : 'fa fa-moon';
    }
}

// ==================== مدیریت سایدبار ====================
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
        sidebar.classList.toggle('active');
    }
}

// ==================== مدیریت مشتریان ====================


function initLanguageSwitcher() {
    const languageSwitcher = document.getElementById('languageSwitcher');
    if (languageSwitcher) {
        languageSwitcher.addEventListener('change', function () {
            const selectedLang = this.value;
            fetch('/admin/change-language', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `lang=${selectedLang}`
            }).then(() => location.reload());
        });
    }
}

// ==================== ابزارهای کمکی ====================
function showError(error) {
    const errorMsg = error.message || 'خطای ناشناخته رخ داد';
    alert(errorMsg);
}

// ==================== مقداردهی اولیه ====================
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.body.setAttribute('data-bs-theme', savedTheme);
    updateThemeIcon(savedTheme);

    initLanguageSwitcher();

    document.body.style.overflowY = 'hidden';
});
