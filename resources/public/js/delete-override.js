(function () {
  'use strict';

  function getAntiForgeryToken() {
    return document.querySelector('input[name="__anti-forgery-token"], input[name="anti-forgery-token"]');
  }

  function saveActiveTab() {
    try {
      var active = document.querySelector('.nav-tabs .nav-link.active');
      if (!active) return;
      var target = active.getAttribute('data-bs-target');
      if (!target) return;
      sessionStorage.setItem('activeTab', target);
      localStorage.setItem('activeTab', target);
    } catch (e) {
      // ignore storage issues
    }
  }

  function submitDelete(deleteUrl) {
    var tokenEl = getAntiForgeryToken();

    // If we cannot find CSRF token, fallback to GET route already supported by backend.
    if (!tokenEl) {
      window.location.assign(deleteUrl);
      return;
    }

    var form = document.createElement('form');
    form.method = 'POST';
    form.action = deleteUrl;
    form.style.display = 'none';

    var hidden = document.createElement('input');
    hidden.type = 'hidden';
    hidden.name = tokenEl.name || '__anti-forgery-token';
    hidden.value = tokenEl.value || '';

    form.appendChild(hidden);
    document.body.appendChild(form);
    form.submit();
  }

  document.addEventListener('click', function (e) {
    var target = e.target;
    if (!target || !target.closest) return;

    var btn = target.closest('button.delete-btn[data-delete-url], a.btn-danger[data-delete-url]');
    if (!btn) return;

    var deleteUrl = btn.getAttribute('data-delete-url');
    if (!deleteUrl) return;

    e.preventDefault();
    e.stopImmediatePropagation();

    if (!window.confirm('Are you sure?')) return;

    saveActiveTab();
    submitDelete(deleteUrl);
  }, true);
})();
