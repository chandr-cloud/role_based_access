(function () {
  const apiBase = '/api/users';

  function escapeHtml(str) {
    return String(str ?? '')
      .replaceAll('&', '&amp;')
      .replaceAll('<', '<')
      .replaceAll('>', '>')
      .replaceAll('"', '"')
      .replaceAll("'", '&#039;');
  }

  window.openAddUserModal = function () {
    const modalEl = document.getElementById('userModal');
    if (!modalEl) return;
    const modal = new bootstrap.Modal(modalEl);

    const titleEl = document.getElementById('userModalTitle');
    if (titleEl) titleEl.textContent = 'Add User';

    const u = document.getElementById('userFormUsername');
    const e = document.getElementById('userFormEmail');
    const r = document.getElementById('userFormRole');
    const s = document.getElementById('userFormStatus');

    if (u) u.value = '';
    if (e) e.value = '';
    if (r) r.value = 'USER';
    if (s) s.value = 'ACTIVE';

    modal.show();
  };

  window.saveUserUI = function () {
    alert('Save user UI (connect to backend).');
    const modalEl = document.getElementById('userModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal?.hide();
  };

  window.viewUser = function (id) {
    alert('View user #' + id + ' (connect to backend).');
  };

  window.editUser = function (id) {
    alert('Edit user #' + id + ' (connect to backend).');
  };

  window.toggleBan = function (id) {
    alert('Ban/Unban user #' + id + ' (connect to backend).');
  };

  window.applyUserFilters = function () {
    // demo only
  };

  window.resetUserFilters = function () {
    const el = document.getElementById('userSearch');
    const r = document.getElementById('roleFilter');
    const s = document.getElementById('statusFilter');
    if (el) el.value = '';
    if (r) r.value = '';
    if (s) s.value = '';
  };

  window.paginateUsers = function () {
    // demo only
  };
})();

