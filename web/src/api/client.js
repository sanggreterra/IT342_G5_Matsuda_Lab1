const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

function getToken() {
  return localStorage.getItem('timesheet_token');
}

function getHeaders(includeAuth = false) {
  const headers = {
    'Content-Type': 'application/json',
  };
  if (includeAuth) {
    const token = getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
  }
  return headers;
}

async function handleResponse(res) {
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    const message = data.error || data.message || `Request failed with ${res.status}`;
    throw new Error(message);
  }
  return data;
}

export const api = {
  async post(url, body, auth = false) {
    const res = await fetch(`${API_BASE}${url}`, {
      method: 'POST',
      headers: getHeaders(auth),
      body: body ? JSON.stringify(body) : undefined,
    });
    return handleResponse(res);
  },

  async get(url, auth = true) {
    const res = await fetch(`${API_BASE}${url}`, {
      method: 'GET',
      headers: getHeaders(auth),
    });
    return handleResponse(res);
  },

  getToken,
};
