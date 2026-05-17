// Admit Cards frontend JS
// Calls: GET /api/v1/admit-cards/search

const AdmitCardsPage = (() => {
  const apiBase = '/admit-cards';
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

  function mapStatusPill(status) {
    const s = (status || '').toString().toUpperCase();
    if (s === 'ACTIVE') return '<span class="bb-pill bb-pill-active">ACTIVE</span>';
    if (s === 'UPCOMING') return '<span class="bb-pill bb-pill-upcoming">UPCOMING</span>';
    if (s === 'EXPIRED' || s === 'CANCELLED') return '<span class="bb-pill bb-pill-closed">' + s + '</span>';
    if (!s) return '<span class="bb-pill">—</span>';
    return '<span class="bb-pill">' + s + '</span>';
  }

  function escapeHtml(str) {
    return String(str ?? '')
      .replaceAll('&', '&amp;')
      .replaceAll('<', '<')
      .replaceAll('>', '>')
      .replaceAll('"', '"')
      .replaceAll("'", '&#039;');
  }

  function renderRow({ rowIndex, id, jobTitle, exam, releaseDate, examDate, statusText, admitCardUrl }) {
    const safeJobTitle = escapeHtml(jobTitle);
    const safeExam = escapeHtml(exam);
    const safeRelease = escapeHtml(releaseDate);
    const safeExamDate = escapeHtml(examDate);
    const safeStatus = escapeHtml(statusText);

    let actions = '';

    if (admitCardUrl) {
      actions += `<a href="${escapeHtml(admitCardUrl)}" target="_blank" rel="noopener">
        <i class="bi bi-file-earmark-text"></i>Download
      </a>`;
    } else {
      actions += `<a href="#" onclick="return false;">
        <i class="bi bi-file-earmark-text"></i>No URL
      </a>`;
    }

    actions += `
      <a href="#" onclick="alert('AdmitCard id: ${escapeHtml(id)}'); return false;">
        <i class="bi bi-eye"></i>View
      </a>
    `;

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${rowIndex}</td>
      <td>${safeJobTitle || '—'}</td>
      <td>${safeExam || '—'}</td>
      <td>${safeRelease || '—'}</td>
      <td>${safeExamDate || '—'}</td>
      <td>${mapStatusPill(safeStatus)}</td>
      <td class="bb-actions">${actions}</td>
    `;

    return tr;
  }

  async function loadAdmitCards(page) {
    currentPage = page;
    setError('');

    const hint = document.getElementById('bbHint');
    if (hint) hint.textContent = 'Loading...';

    const params = new URLSearchParams();
    params.set('page', String(page));
    params.set('size', String(currentPageSize));
    params.set('sort', 'examDate,desc');

    const jobId = getElValue('jobId');
    const status = getElValue('status');
    const from = getElValue('from');
    const to = getElValue('to');

    if (jobId) params.set('jobId', jobId);
    if (status) params.set('status', status);
    if (from) params.set('from', from);
    if (to) params.set('to', to);

    // Backend endpoint: /api/v1/admit-cards/search
    const url = `${apiBase}/search?${params.toString()}`;

    try {
      const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(text || ('Request failed: ' + res.status));
      }

      const data = await res.json();
      const content = data?.content ?? [];

      const tbody = document.getElementById('admitTbody');
      if (!tbody) return;
      tbody.innerHTML = '';

      if (!Array.isArray(content) || content.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="bb-muted" style="padding:18px 12px;">No admit cards found.</td></tr>`;
        if (hint) hint.textContent = '0 items';
        return;
      }

      content.forEach((card, idx) => {
        const id = card.id ?? '';
        const jobTitle = card.jobTitle ?? '';
        const exam = (card.examName || '') + (card.examType ? ' (' + card.examType + ')' : '');
        const admitCardUrl = card.admitCardUrl || '';
        const releaseDate = card.admitCardReleaseDate || '';
        const examDate = card.examDate || '';
        const statusText = card.status || '';
        const rowIndex = page * currentPageSize + idx + 1;

        tbody.appendChild(renderRow({
          rowIndex,
          id,
          jobTitle,
          exam,
          releaseDate,
          examDate,
          statusText,
          admitCardUrl
        }));
      });

      if (hint) hint.textContent = `Showing page ${page + 1}`;
    } catch (e) {
      setError(e?.message || 'Failed to load admit cards');
      if (hint) hint.textContent = 'Error';
    }
  }

  function resetFilters() {
    document.getElementById('jobId').value = '';
    document.getElementById('status').value = '';
    document.getElementById('from').value = '';
    document.getElementById('to').value = '';
    loadAdmitCards(0);
  }

  function refreshCurrentPage() {
    loadAdmitCards(currentPage);
  }

  // Expose globals used by inline handlers in the template
  window.loadAdmitCards = loadAdmitCards;
  window.resetFilters = resetFilters;
  window.refreshCurrentPage = refreshCurrentPage;

  // Initial load on DOM ready
  document.addEventListener('DOMContentLoaded', () => {
    const tbody = document.getElementById('admitTbody');
    if (tbody) loadAdmitCards(0);
  });

  return { loadAdmitCards };
})();

