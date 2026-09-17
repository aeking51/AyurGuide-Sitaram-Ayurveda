/**
 * Sitaram Ayurveda Admin Website - Application Controller & Client Router
 */

class AdminApp {
  constructor() {
    this.currentPath = window.location.pathname || '/admin/dashboard';
    this.currentUser = null;
    this.profileMenuOpen = false;
    this.dashboardState = {
      loading: true,
      error: null,
      metrics: null,
      recentProducts: [],
      recentlyUpdated: [],
      categorySummary: []
    };
    this.init();
  }

  async init() {
    // 1. Listen for browser back/forward buttons
    window.addEventListener('popstate', () => {
      this.handleRoute(window.location.pathname);
    });

    // 2. Close profile dropdown on outside click
    document.addEventListener('click', (e) => {
      const container = document.getElementById('admin-profile-menu-container');
      if (container && !container.contains(e.target)) {
        const dropdown = document.getElementById('admin-profile-dropdown');
        if (dropdown && !dropdown.classList.contains('hidden')) {
          dropdown.classList.add('hidden');
          this.profileMenuOpen = false;
        }
      }
    });

    // 3. Verify session against backend on initial page load
    const sessionRes = await SitaramAuth.checkSession();
    if (sessionRes.authenticated) {
      this.currentUser = sessionRes.admin;
      if (this.currentPath === '/admin/login' || this.currentPath === '/' || this.currentPath === '/admin') {
        this.navigate('/admin/dashboard');
        return;
      }
      this.handleRoute(this.currentPath);
    } else {
      this.currentUser = null;
      if (this.currentPath !== '/admin/login') {
        this.navigate('/admin/login');
        return;
      }
      this.handleRoute('/admin/login');
    }
  }

  navigate(path, event) {
    if (event) {
      event.preventDefault();
    }
    if (window.location.pathname !== path) {
      window.history.pushState({}, '', path);
    }
    this.handleRoute(path);
  }

  async handleRoute(pathname) {
    this.currentPath = pathname;
    const root = document.getElementById('app-root');
    if (!root) return;

    // Login Route
    if (pathname === '/admin/login') {
      root.innerHTML = Views.login();
      this.attachLoginForm();
      return;
    }

    // Protection Check
    if (!this.currentUser) {
      const check = await SitaramAuth.checkSession();
      if (!check.authenticated) {
        this.navigate('/admin/login');
        return;
      }
      this.currentUser = check.admin;
    }

    // Determine target sub-page
    const subRoute = pathname.replace('/admin/', '').split('/')[0] || 'dashboard';

    if (subRoute === 'dashboard') {
      root.innerHTML = Views.layout('dashboard', this.currentUser, '<div id="dashboard-content"></div>');
      this.loadDashboardData();
    } 
    else if (subRoute === 'products') {
      const placeholderHtml = Views.placeholder(
        'Product Catalogue Management',
        'Direct classical formulation inventory, indications, packings and botanical registry.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>'
      );
      root.innerHTML = Views.layout('products', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'categories') {
      const placeholderHtml = Views.placeholder(
        'Therapeutic Categories (24)',
        'Master classification from the Sitaram Therapeutic Index Handbook.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>'
      );
      root.innerHTML = Views.layout('categories', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'ingredients') {
      const placeholderHtml = Views.placeholder(
        'Normalized Botanical Ingredients',
        'Botanical species, Sanskrit names, and herbal usage mapping across classical recipes.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"></path>'
      );
      root.innerHTML = Views.layout('ingredients', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'manufacturers') {
      const placeholderHtml = Views.placeholder(
        'Manufacturing Facilities & GMP Licenses',
        'Certified production laboratories, Ayush manufacturing units and addresses.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>'
      );
      root.innerHTML = Views.layout('manufacturers', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'media') {
      const placeholderHtml = Views.placeholder(
        'Media & Packaging Imagery',
        'Clinical asset storage for pharmaceutical packaging photographs and botanical illustrations.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>'
      );
      root.innerHTML = Views.layout('media', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'app-content') {
      const placeholderHtml = Views.placeholder(
        'Mobile App Content Governance',
        'Promotional banners, featured formulation shelves and classical formulation monographs for future Android apps.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"></path>'
      );
      root.innerHTML = Views.layout('app-content', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'reports') {
      const placeholderHtml = Views.placeholder(
        'Clinical Reports & Data Analytics',
        'Formulation distribution summaries, dosage compliance reports, and catalogue audits.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>'
      );
      root.innerHTML = Views.layout('reports', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'audit-logs') {
      const placeholderHtml = Views.placeholder(
        'Regulatory Audit Trail',
        'Immutable server-side logs recording administrative changes, formulation edits and exports.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>'
      );
      root.innerHTML = Views.layout('audit-logs', this.currentUser, placeholderHtml);
    }
    else if (subRoute === 'settings') {
      const placeholderHtml = Views.placeholder(
        'System Settings & Database Config',
        'Central cloud connection parameters, shared Android sync schema and backup snapshots.',
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>'
      );
      root.innerHTML = Views.layout('settings', this.currentUser, placeholderHtml);
    }
    else {
      this.navigate('/admin/dashboard');
    }
  }

  // Load live data for Dashboard
  async loadDashboardData() {
    const container = document.getElementById('dashboard-content');
    if (!container) return;

    // Show loading skeleton
    container.innerHTML = DashboardView.renderLoading();

    try {
      // Parallel fetch of backend metrics
      const [resMetrics, resRecent, resUpdated, resSummary] = await Promise.all([
        fetch('/api/dashboard/metrics', { headers: { 'Accept': 'application/json' } }),
        fetch('/api/dashboard/recent-products', { headers: { 'Accept': 'application/json' } }),
        fetch('/api/dashboard/recently-updated', { headers: { 'Accept': 'application/json' } }),
        fetch('/api/dashboard/category-summary', { headers: { 'Accept': 'application/json' } })
      ]);

      if (!resMetrics.ok) {
        throw new Error(`Failed to fetch metrics: ${resMetrics.statusText}`);
      }

      const [dataMetrics, dataRecent, dataUpdated, dataSummary] = await Promise.all([
        resMetrics.json(),
        resRecent.json(),
        resUpdated.json(),
        resSummary.json()
      ]);

      this.dashboardState = {
        loading: false,
        error: null,
        metrics: dataMetrics.data,
        recentProducts: dataRecent.data || [],
        recentlyUpdated: dataUpdated.data || [],
        categorySummary: dataSummary.data || []
      };

      container.innerHTML = DashboardView.renderSuccess(
        this.dashboardState.metrics,
        this.dashboardState.recentProducts,
        this.dashboardState.recentlyUpdated,
        this.dashboardState.categorySummary
      );
    } catch (err) {
      console.error('Dashboard data fetch error:', err);
      container.innerHTML = DashboardView.renderError(err.message);
    }
  }

  // Login form handler
  attachLoginForm() {
    const form = document.getElementById('admin-login-form');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idInput = document.getElementById('login-identifier');
      const passInput = document.getElementById('login-password');
      const submitBtn = document.getElementById('btn-submit-login');
      const errBox = document.getElementById('login-error-box');
      const errMsg = document.getElementById('login-error-msg');

      errBox.classList.add('hidden');
      submitBtn.disabled = true;
      submitBtn.innerHTML = `
        <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <span>Verifying Administrator Credentials...</span>
      `;

      const res = await SitaramAuth.login(idInput.value, passInput.value);

      if (res.success) {
        this.currentUser = res.admin;
        this.navigate('/admin/dashboard');
      } else {
        submitBtn.disabled = false;
        submitBtn.innerHTML = `
          <span>Sign In to Admin Portal</span>
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path>
          </svg>
        `;
        errBox.classList.remove('hidden');
        errMsg.innerText = res.message;
      }
    });
  }

  // Forgot password modal
  async showForgotPasswordPrompt() {
    const email = prompt('Enter your registered Sitaram Ayurveda Administrator email address:');
    if (!email) return;

    const res = await SitaramAuth.requestPasswordReset(email);
    alert(res.message);
  }

  // Profile menu dropdown toggle
  toggleProfileMenu() {
    const dropdown = document.getElementById('admin-profile-dropdown');
    if (!dropdown) return;
    this.profileMenuOpen = !this.profileMenuOpen;
    if (this.profileMenuOpen) {
      dropdown.classList.remove('hidden');
    } else {
      dropdown.classList.add('hidden');
    }
  }

  // Sidebar toggle for mobile/tablets
  toggleSidebar() {
    const sidebar = document.getElementById('main-sidebar');
    const backdrop = document.getElementById('mobile-sidebar-backdrop');
    if (!sidebar) return;
    sidebar.classList.toggle('-translate-x-full');
    if (backdrop) backdrop.classList.toggle('hidden');
  }
}

window.App = new AdminApp();
