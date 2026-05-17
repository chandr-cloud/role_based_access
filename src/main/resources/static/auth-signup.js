function validatePasswords() {
  const form = document.getElementById('signupForm');
  const password = document.getElementById('password');
  const confirmPassword = document.getElementById('confirmPassword');
  const mismatch = document.getElementById('passwordMismatch');

  if (!password || !confirmPassword || !mismatch) return true;

  const p = password.value;
  const cp = confirmPassword.value;

  if (p !== cp) {
    mismatch.textContent = 'Passwords do not match. Please re-check.';
    mismatch.classList.remove('d-none');
    if (form) form.querySelector('button[type="submit"]').disabled = false;
    confirmPassword.focus();
    return false;
  }

  mismatch.classList.add('d-none');
  return true;
}

(function () {
  const password = document.getElementById('password');
  const confirmPassword = document.getElementById('confirmPassword');
  const form = document.getElementById('signupForm');

  if (confirmPassword) {
    confirmPassword.addEventListener('input', function () {
      validatePasswords();
    });
  }

  if (password) {
    password.addEventListener('input', function () {
      const mismatch = document.getElementById('passwordMismatch');
      if (!mismatch) return;

      if (password.value !== confirmPassword.value) {
        mismatch.textContent = 'Passwords do not match. Please re-check.';
        mismatch.classList.remove('d-none');
        if (form) {
          const submitBtn = form.querySelector('button[type="submit"]');
          if (submitBtn) submitBtn.disabled = false;
        }
      } else {
        mismatch.classList.add('d-none');
      }
    });
  }
})();

