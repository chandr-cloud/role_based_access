(function () {
  const toggle = document.getElementById('togglePassword');
  const password = document.getElementById('password');
  if (!toggle || !password) return;

  toggle.addEventListener('click', function () {
    const isPassword = password.type === 'password';
    password.type = isPassword ? 'text' : 'password';
    toggle.textContent = isPassword ? 'Hide' : 'Show';
  });
})();

