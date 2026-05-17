(function () {
  const apiBase = '/api/v1/results';
  let currentPage = 0;
  let currentPageSize = 10;

  function setError(msg) {
    const el = document.getElementById('bbError');
    if (!el) return;
    if (!msg) {
      el.style.display = 'none';
      el.textContent = '';
      return;
    }
    el.textContent = msg;
    el.style.display = 'block';
  }

  function getElValue(id) {
    const el = document.getElementById(id);
    if (!el) return '';
    return (el.value ?? '').toString().trim();
  }

  function escapeHtml(str) {
    return String(str ?? '')
      .replaceAll('&', '&amp;')
      .replaceAll('<', '<')
      .replaceAll('>', '>')
      .replaceAll('"', '"')
      .replaceAll("'", '&#039;');
  }

  function mapStatusPill(status) {
    const s = (status || '').toString().toUpperCase();
    if (s === 'PUBLISHED' || s === 'ACTIVE') return '<span class="bb-pill bb-pill-active">' + escapeHtml(s) + '</span>';
    if (s === 'DRAFT' || s === 'UPCOMING') return '<span class="bb-pill bb-pill-upcoming">' + escapeHtml(s) + '</span>';
    if (s === 'EXPIRED' || s === 'CANCELLED' || s === 'CLOSED') return '<span class="bb-pill bb-pill-closed">' + escapeHtml(s) + '</span>';
    if (!s) return '<span class="bb-pill">—</span>';
    return '<span class="bb-pill">' + escapeHtml(s) + '</span>';
  }

  async function loadResults(page) {
    currentPage = page;

    setError('');
    const hint = document.getElementById('bbHint');
    if (hint) hint.textContent = 'Loading...';

    const params = new URLSearchParams();
    params.set('page', String(page));
    params.set('size', String(currentPageSize));
    params.set('sort', 'createdAt,desc');

    const jobId = getElValue('jobId');
    const status = getElValue('status');
    const from = getElValue('from');
    const to = getElValue('to');

    if (jobId) params.set('jobId', jobId);
    if (status) params.set('status', status);
    if (from) params.set('from', from);
    if (to) params.set('to', to);

    // Backend endpoint: /api/v1/results/search
    const url = apiBase + '/search?' + params.toString();

    try {
      const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(text || ('Request failed: ' + res.status));
      }

      const data = await res.json();
      const content = data?.content ?? [];

      const tbody = document.getElementById('resultsTbody');
      if (!tbody) return;
      tbody.innerHTML = '';

      if (!Array.isArray(content) || content.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="padding:18px 12px; color: rgba(255,255,255,0.55); font-weight:700;">No results found.</td></tr>';
        if (hint) hint.textContent = '0 items';
        return;
      }

      content.forEach((r, idx) => {
        const id = r.id ?? '';
        const jobTitle = r.jobTitle ?? r.jobName ?? r.job?.jobTitle ?? r.job?.title ?? '';
        const created = r.createdAt ?? r.examDate ?? r.createdOn ?? '';
        const createdText = created ? String(created).slice(0, 10) : '';
        const statusText = r.status ?? '';

        const rowIndex = page * currentPageSize + idx + 1;

        tbody.appendChild(renderRow({
          rowIndex,
          id,
          jobTitle,
          createdText,
          statusText
        }));
      });

      if (hint) hint.textContent = `Showing page ${page + 1}`;
    } catch (e) {
      setError(e?.message || 'Failed to load results');
      if (hint) hint.textContent = 'Error';
    }
  }

  function renderRow({ rowIndex, id, jobTitle, createdText, statusText }) {
    const tr = document.createElement('tr');

    const safeJob = escapeHtml(jobTitle);
    const safeCreated = escapeHtml(createdText);
    const safeStatus = escapeHtml(statusText);

    const actions = `
      <a href="#" onclick="alert('Result id: ${escapeHtml(id)}'); return false;">
        <i class="bi bi-eye"></i>View
      </a>
    `;

    tr.innerHTML = `
      <td>${rowIndex}</td>
      <td>${safeJob || '—'}</td>
      <td>${safeCreated || '—'}</td>
      <td>${mapStatusPill(safeStatus)}</td>
      <td class="bb-actions">${actions}</td>
    `;

    return tr;
  }

  function resetFilters() {
    const jobId = document.getElementById('jobId');
    const status = document.getElementById('status');
    const from = document.getElementById('from');
    const to = document.getElementById('to');
    if (jobId) jobId.value = '';
    if (status) status.value = '';
    if (from) from.value = '';
    if (to) to.value = '';
    loadResults(0);
  }

  function refreshCurrentPage() {
    loadResults(currentPage);
  }

  // expose
  window.loadResults = loadResults;
  window.resetFilters = resetFilters;
  window.refreshCurrentPage = refreshCurrentPage;

  // initial load
  loadResults(0);
})();

