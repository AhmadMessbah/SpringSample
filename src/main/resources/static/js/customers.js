function loadCustomers() {
    const activePage = document.querySelector('.pagination .active');
    const page = activePage ? parseInt(activePage.dataset.page, 10) || 0 : 0;
    const size =  parseInt(document.getElementById('pageSize').value, 10);
    const sortBy = document.getElementById('sortBy').value;

    // بررسی مقادیر
    if (isNaN(page) || isNaN(size) || !sortBy) {
        showError(new Error('مقادیر صفحه‌بندی یا مرتب‌سازی نامعتبر هستند.'));
        return;
    }

    const url = `/customers?page=${encodeURIComponent(page)}&size=${encodeURIComponent(size)}&sortBy=${encodeURIComponent(sortBy)}&fragment=true`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('خطا در دریافت داده‌ها');
            }
            return response.text();
        })
        .then(html => {
            const customersTableContainer = document.getElementById('customers-table-container');
            if (customersTableContainer) {
                customersTableContainer.innerHTML = html;
                initPagination();
                initDeleteButtons();
            }
        })
        .catch(error => showError(error));
}

function handleCustomerFormSubmit(e) {
    e.preventDefault();

    const customer = {
        id: document.getElementById('customerId').value,
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: document.getElementById('phone').value.trim()
    };

    if (!customer.firstName || !customer.lastName) {
        alert(document.getElementById('validationErrorText').textContent);
        return;
    }

    fetch('/customers', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(customer)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('خطا در ارسال درخواست');
            }
            return response.json();
        })
        .then(() => {
            const modal = bootstrap.Modal.getInstance(document.getElementById('customerModal'));
            if (modal) {
                modal.hide();
            }
            loadCustomers();
        })
        .catch(error => showError(error));
}

function handleCustomerDelete(e) {
    const btn = e.target.closest('.btn-danger');
    const confirmText = btn.dataset.confirmText || 'آیا از حذف این مشتری اطمینان دارید؟';

    if (!confirm(confirmText)) return;

    const customerId = btn.dataset.id;

    fetch(`/customers/${customerId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('خطا در حذف مشتری');
            }
            loadCustomers();
        })
        .catch(error => showError(error));
}

// ==================== مدیریت رویدادها ====================
function initCustomerModal() {
    const customerModal = document.getElementById('customerModal');
    if (customerModal) {
        customerModal.addEventListener('show.bs.modal', function (e) {
            const btn = e.relatedTarget;
            const row = btn.closest('tr');

            if (btn.dataset.id) {
                document.getElementById('customerId').value = btn.dataset.id;
                document.getElementById('firstName').value = row.cells[0].textContent.trim();
                document.getElementById('lastName').value = row.cells[1].textContent.trim();
                document.getElementById('email').value = row.cells[2].textContent.trim();
                document.getElementById('phone').value = row.cells[3].textContent.trim();
            } else {
                document.getElementById('customerForm').reset();
                document.getElementById('customerId').value = '';
            }
        });
    }
}

function initPagination() {
    document.querySelectorAll('.page-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelectorAll('.page-item').forEach(item => item.classList.remove('active'));
            this.closest('.page-item').classList.add('active');
            loadCustomers();
        });
    });
}

function initSortAndPageSize() {
    const pageSize = document.getElementById('pageSize');
    const sortBy = document.getElementById('sortBy');

    if (pageSize) {
        pageSize.addEventListener('change', loadCustomers);
    }
    if (sortBy) {
        sortBy.addEventListener('change', loadCustomers);
    }
}

function initDeleteButtons() {
    document.querySelectorAll('.btn-danger').forEach(btn => {
        btn.addEventListener('click', handleCustomerDelete);
    });
}

// ==================== مقداردهی اولیه ====================
document.addEventListener('DOMContentLoaded', () => {
    loadCustomers();

    const customerForm = document.getElementById('customerForm');
    if (customerForm) {
        customerForm.addEventListener('submit', handleCustomerFormSubmit);
    }

    initCustomerModal();
    initPagination();
    initSortAndPageSize();
    initDeleteButtons();
});