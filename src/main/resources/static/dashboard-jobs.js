(function () {
  const apiBase = '/api/jobs';

  function getEl(id) {
    return document.getElementById(id);
  }

  function escapeHtml(str) {
    return String(str ?? '')
      .replaceAll('&', '&amp;')
      .replaceAll('<', '<')
      .replaceAll('>', '>')
      .replaceAll('"', '"')
      .replaceAll("'", '&#039;');
  }

  function normalizeStatus(v) {
    if (!v) return 'ACTIVE';
    const s = String(v).toUpperCase();
    if (s.includes('CLOSE')) return 'CLOSED';
    if (s.includes('UPCOMING')) return 'UPCOMING';
    if (s.includes('ACTIVE')) return 'ACTIVE';
    return 'ACTIVE';
  }

  function normalizeDate(v) {
    if (!v) return '';
    if (/^\d{4}-\d{2}-\d{2}$/.test(v)) return v;
    const d = new Date(v);
    if (Number.isNaN(d.getTime())) return '';
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  function statusToPill(status) {
    const s = normalizeStatus(status);
    if (s === 'ACTIVE') return '<span class="bb-pill bb-pill-active">Active</span>';
    if (s === 'UPCOMING') return '<span class="bb-pill bb-pill-upcoming">Upcoming</span>';
    if (s === 'CLOSED') return '<span class="bb-pill bb-pill-closed">Closed</span>';
    return '<span class="bb-pill bb-pill-upcoming">Upcoming</span>';
  }

  function csrfHeaders() {
    // This is a best-effort for CSRF. If your APIs are stateless, it won't matter.
    const hidden = document.querySelector('input[name="_csrf"], input[id="_csrf"], input[name*="_csrf"], input[id*="_csrf"]');
    if (!hidden) return {};

    const paramName = hidden.getAttribute('name') || hidden.getAttribute('id');
    const token = hidden.value;
    if (!paramName || !token) return {};

    // Common header name
    return { 'X-CSRF-Token': token };
  }

  async function loadJobs() {
    const tbody = document.getElementById('jobsTbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    try {
      const res = await fetch(apiBase, { headers: { 'Accept': 'application/json' } });
      if (!res.ok) return;
      const jobs = await res.json().catch(() => null);
      if (!Array.isArray(jobs) || jobs.length === 0) return;

      jobs.slice(0, 8).forEach((job, idx) => {
        const id = job.id ?? job.jobId ?? idx + 1;
        const title = job.jobTitle?.title ?? job.title ?? job.jobTitle ?? 'Job';
        const category = job.jobTitle?.category ?? job.category ?? '—';
        const totalPosts = job.totalPosts ?? job.totalPost ?? 0;
        const fee = job.feeGeneral ?? job.fee ?? 0;
        const lastDate = job.applyLastDate ?? job.lastDate ?? '';
        const status = job.status ?? job.jobStatus ?? '';

        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${idx + 1}</td>
          <td>${escapeHtml(title)}</td>
          <td>${escapeHtml(category)}</td>
          <td>${escapeHtml(String(totalPosts))}</td>
          <td>₹${escapeHtml(String(fee))}</td>
          <td>${escapeHtml(normalizeDate(lastDate) || lastDate)}</td>
          <td>${statusToPill(status)}</td>
          <td>
            <div class="d-flex gap-2">
              <a href="#" class="bb-action-btn bb-action-view" onclick="viewJob(${id}); return false;"><i class="bi bi-eye"></i>View</a>
              <a href="#" class="bb-action-btn bb-action-edit" onclick="editJob(${id}); return false;"><i class="bi bi-pencil"></i>Edit</a>
              <a href="#" class="bb-action-btn bb-action-del" onclick="deleteJob(${id}); return false;"><i class="bi bi-trash"></i>Delete</a>
            </div>
          </td>
        `;
        tbody.appendChild(tr);
      });
    } catch {
      // ignore (template demo)
    }
  }

  window.viewJob = async function (jobId) {
    try {
      const res = await fetch(`${apiBase}/${jobId}`);
      if (!res.ok) throw new Error('Failed to fetch job');
      const job = await res.json();
      alert(`Job: ${job.jobTitle ?? job.title ?? jobId}\nStatus: ${job.status ?? job.jobStatus ?? ''}`);
    } catch (e) {
      alert('View failed: ' + (e?.message || e));
    }
  };

  window.editJob = async function (jobId) {
    try {
      const res = await fetch(`${apiBase}/${jobId}`);
      if (!res.ok) throw new Error('Failed to fetch job');
      const job = await res.json();
      // These modal helpers already exist inside jobs.html; if you split further, move them too.
      if (typeof window.openEditModal === 'function') window.openEditModal(jobId, job);
    } catch {
      if (typeof window.openCreateModal === 'function') window.openCreateModal();
    }
  };

  window.deleteJob = async function (jobId) {
    const ok = confirm('Delete this job?');
    if (!ok) return;

    try {
      const headers = { ...csrfHeaders() };
      const res = await fetch(`${apiBase}/${jobId}`, { method: 'DELETE', headers });
      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(text || `Delete failed: ${res.status}`);
      }
      await res.text().catch(() => '');
      alert('Job deleted successfully');
      loadJobs();
    } catch (e) {
      alert('Delete failed: ' + (e?.message || e));
    }
  };

  window.applyFilters = function () {
    loadJobs();
  };

  window.resetFilters = function () {
    const el = document.getElementById('jobSearch');
    const s1 = document.getElementById('statusFilter');
    const s2 = document.getElementById('categoryFilter');
    if (el) el.value = '';
    if (s1) s1.value = '';
    if (s2) s2.value = '';
    loadJobs();
  };

  window.paginate = function () {
    // demo
    loadJobs();
  };

  window.exportJobs = function () {
    const headers = ['Job Title', 'Category', 'Total Posts', 'Fee (General)', 'Apply Last Date', 'Status'];
    const rows = [];
    document.querySelectorAll('#jobsTable tbody tr').forEach((tr) => {
      const tds = tr.querySelectorAll('td');
      if (tds.length < 7) return;
      rows.push([
        tds[1].innerText.trim(),
        tds[2].innerText.trim(),
        tds[3].innerText.trim(),
        tds[4].innerText.trim(),
        tds[5].innerText.trim(),
        tds[6].innerText.trim(),
      ]);
    });

    const csv = [
      headers.join(','),
      ...rows.map((r) => r.map((x) => '"' + String(x).replaceAll('"', '""') + '"').join(',')),
    ].join('\n');

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'jobs_export.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  // Boot
  document.addEventListener('DOMContentLoaded', loadJobs);
})();

