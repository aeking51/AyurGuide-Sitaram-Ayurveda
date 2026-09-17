/**
 * Sitaram Ayurveda Admin Website - Backend Authentication Client
 * Strictly interacts with backend session endpoints (/api/auth/*).
 */

class SitaramAuthClient {
  constructor() {
    this.currentUser = null;
  }

  async checkSession() {
    try {
      const res = await fetch('/api/auth/session', {
        headers: { 'Accept': 'application/json' }
      });
      if (res.ok) {
        const data = await res.json();
        this.currentUser = data.admin;
        return { authenticated: true, admin: data.admin };
      }
    } catch (e) {
      console.error('Session check failed:', e);
    }
    this.currentUser = null;
    return { authenticated: false };
  }

  async login(identifier, password) {
    try {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({ identifier, password })
      });

      const data = await res.json();
      if (res.ok && data.success) {
        this.currentUser = data.admin;
        if (data.token) {
          localStorage.setItem('sitaram_token', data.token);
        }
        return { success: true, admin: data.admin };
      }
      return { success: false, message: data.message || 'Login failed' };
    } catch (err) {
      return { success: false, message: 'Server communication error. Please try again.' };
    }
  }

  async logout() {
    try {
      await fetch('/api/auth/logout', {
        method: 'POST',
        headers: { 'Accept': 'application/json' }
      });
    } catch (e) {
      console.error('Logout error:', e);
    }
    localStorage.removeItem('sitaram_token');
    this.currentUser = null;
    window.location.href = '/admin/login';
  }

  async requestPasswordReset(email) {
    try {
      const res = await fetch('/api/auth/forgot-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({ email })
      });
      const data = await res.json();
      return data;
    } catch (err) {
      return { success: false, message: 'Server network failure.' };
    }
  }

  getUser() {
    return this.currentUser;
  }
}

window.SitaramAuth = new SitaramAuthClient();
