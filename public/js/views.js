/**
 * Sitaram Ayurveda Admin Website - Views, Shell Layout & Navigation
 */

const Views = {
  // Login Screen
  login: () => `
    <div class="min-h-screen flex items-center justify-center bg-[#F4EFE6] px-4 py-12">
      <div class="max-w-md w-full bg-white rounded-2xl shadow-xl border border-[#E2D9CC] overflow-hidden">
        <!-- Header Banner with Traditional Ayurvedic Theme -->
        <div class="bg-gradient-to-br from-[#0F382C] to-[#1B4D3E] p-8 text-center text-white relative">
          <div class="inline-flex p-3 rounded-full bg-white/10 mb-3 backdrop-blur-sm border border-white/20">
            <svg class="w-10 h-10 text-[#DFB15B]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path>
            </svg>
          </div>
          <h1 class="font-serif text-2xl font-bold tracking-wide text-[#F9F6F0]">SITARAM AYURVEDA</h1>
          <p class="text-[11px] uppercase tracking-widest text-[#DFB15B] mt-1 font-semibold">Therapeutic Index Admin Panel</p>
          <div class="mt-3 text-[11px] text-white/80 bg-black/20 py-1 px-3 rounded-full inline-block border border-white/10">
            Authorized Personnel Only
          </div>
        </div>

        <!-- Login Form -->
        <form id="admin-login-form" class="p-8 space-y-5">
          <div id="login-error-box" class="hidden p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-xs flex items-center gap-2">
            <svg class="w-4 h-4 shrink-0" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
            </svg>
            <span id="login-error-msg"></span>
          </div>

          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase tracking-wider mb-1.5">Username or Email</label>
            <div class="relative">
              <input type="text" id="login-identifier" required 
                class="w-full pl-10 pr-4 py-2.5 text-sm rounded-lg border border-gray-300 focus:ring-2 focus:ring-[#1B4D3E] focus:border-[#1B4D3E] outline-none transition" 
                placeholder="sys.jerin@gmail.com" value="sys.jerin@gmail.com">
              <svg class="w-5 h-5 text-gray-400 absolute left-3 top-2.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
              </svg>
            </div>
          </div>

          <div>
            <div class="flex justify-between items-center mb-1.5">
              <label class="block text-xs font-bold text-gray-700 uppercase tracking-wider">Password</label>
              <button type="button" onclick="App.showForgotPasswordPrompt()" class="text-xs text-[#1B4D3E] font-medium hover:underline">Forgot password?</button>
            </div>
            <div class="relative">
              <input type="password" id="login-password" required 
                class="w-full pl-10 pr-4 py-2.5 text-sm rounded-lg border border-gray-300 focus:ring-2 focus:ring-[#1B4D3E] focus:border-[#1B4D3E] outline-none transition" 
                placeholder="••••••••" value="admin123">
              <svg class="w-5 h-5 text-gray-400 absolute left-3 top-2.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
              </svg>
            </div>
          </div>

          <button type="submit" id="btn-submit-login" class="w-full bg-[#1B4D3E] hover:bg-[#0F382C] text-white py-3 rounded-lg font-semibold text-sm transition-all shadow-md hover:shadow-lg flex justify-center items-center gap-2">
            <span>Sign In to Admin Portal</span>
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path>
            </svg>
          </button>

          <div class="pt-3 border-t border-gray-100 flex items-center justify-between text-xs text-gray-500">
            <span>Server-side PBKDF2 Auth</span>
            <span class="text-[#1B4D3E] font-medium">Sitaram Ayurveda Ltd</span>
          </div>
        </form>
      </div>
    </div>
  `,

  // Master Layout Shell (Header + Collapsible Sidebar + Content Container + Profile Menu)
  layout: (currentRoute, user, contentHtml) => {
    const admin = user || { name: 'Administrator', role: 'Staff', email: 'admin' };
    
    // Helper to render sidebar items
    const navItems = [
      { route: 'dashboard', label: 'Dashboard', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"></path>' },
      { route: 'products', label: 'Products', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>' },
      { route: 'categories', label: 'Categories', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>' },
      { route: 'ingredients', label: 'Ingredients', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"></path>' },
      { route: 'manufacturers', label: 'Manufacturers', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>' },
      { route: 'media', label: 'Media', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>' },
      { route: 'app-content', label: 'App Content', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"></path>' },
      { route: 'reports', label: 'Reports', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>' },
      { route: 'audit-logs', label: 'Audit Logs', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>' },
      { route: 'settings', label: 'Settings', icon: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>' }
    ];

    const sidebarLinksHtml = navItems.map(item => {
      const active = currentRoute === item.route;
      return `
        <a href="/admin/${item.route}" onclick="App.navigate('/admin/${item.route}', event)" 
          class="flex items-center gap-3 px-4 py-2.5 rounded-xl text-sm font-medium transition-all ${
            active 
              ? 'bg-[#DFB15B]/20 text-[#DFB15B] font-semibold border-l-4 border-[#DFB15B]' 
              : 'text-gray-300 hover:bg-white/5 hover:text-white'
          }">
          <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">${item.icon}</svg>
          <span class="flex-1">${item.label}</span>
          ${item.route === 'categories' ? '<span class="text-[10px] bg-white/10 px-1.5 py-0.5 rounded text-gray-300 font-mono">24</span>' : ''}
        </a>
      `;
    }).join('');

    return `
      <div class="min-h-screen flex bg-[#F9F7F2] font-sans antialiased text-[#1A2E26]">
        <!-- Mobile Sidebar Overlay -->
        <div id="mobile-sidebar-backdrop" onclick="App.toggleSidebar()" class="fixed inset-0 bg-black/50 z-40 lg:hidden hidden backdrop-blur-xs"></div>

        <!-- Left Sidebar -->
        <aside id="main-sidebar" class="fixed lg:static inset-y-0 left-0 z-50 w-64 bg-[#0F382C] text-white flex flex-col transition-transform duration-300 -translate-x-full lg:translate-x-0 shadow-2xl border-r border-[#1B4D3E]">
          <!-- Sidebar Header Branding -->
          <div class="p-6 border-b border-white/10 flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-xl bg-[#DFB15B] flex items-center justify-center text-[#0F382C] font-serif font-bold text-xl shadow-inner">
                SA
              </div>
              <div>
                <h2 class="font-serif font-bold text-base tracking-wide text-[#F9F6F0]">SITARAM</h2>
                <p class="text-[10px] uppercase tracking-wider text-[#DFB15B] font-semibold">Ayurveda Admin</p>
              </div>
            </div>
            <button onclick="App.toggleSidebar()" class="lg:hidden text-white/70 hover:text-white p-1">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
            </button>
          </div>

          <!-- Navigation Links -->
          <nav class="flex-1 overflow-y-auto px-4 py-4 space-y-1.5 text-sm custom-scrollbar">
            ${sidebarLinksHtml}
          </nav>

          <!-- Sidebar Footer with Quick Admin Status -->
          <div class="p-4 border-t border-white/10 bg-black/20 flex items-center justify-between">
            <div class="flex items-center gap-2.5 overflow-hidden">
              <div class="w-8 h-8 rounded-full bg-[#1B4D3E] border border-[#DFB15B]/50 flex items-center justify-center text-xs font-bold text-[#DFB15B]">
                ${admin.name.charAt(0)}
              </div>
              <div class="truncate">
                <p class="text-xs font-semibold text-white truncate">${admin.name}</p>
                <p class="text-[10px] text-[#DFB15B] truncate">${admin.role}</p>
              </div>
            </div>
            <button onclick="SitaramAuth.logout()" title="Logout" class="p-2 text-gray-400 hover:text-red-400 hover:bg-white/5 rounded-lg transition-colors">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
            </button>
          </div>
        </aside>

        <!-- Main Content Column -->
        <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
          <!-- Top Header -->
          <header class="bg-white border-b border-[#E2D9CC] h-16 flex items-center justify-between px-6 shrink-0 z-30 shadow-xs">
            <div class="flex items-center gap-3">
              <button onclick="App.toggleSidebar()" class="lg:hidden text-gray-600 hover:text-gray-900 p-2 rounded-lg hover:bg-gray-100">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
              </button>
              <div class="flex items-center gap-2 text-xs sm:text-sm text-gray-500">
                <span class="font-serif font-bold text-[#0F382C]">Sitaram Therapeutic Index</span>
                <span>/</span>
                <span class="capitalize text-gray-800 font-semibold">${currentRoute}</span>
              </div>
            </div>

            <!-- Top Header Right: DB Status + Admin Profile Dropdown -->
            <div class="flex items-center gap-3">
              <div class="hidden sm:flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-50 text-emerald-800 border border-emerald-200 text-xs font-medium">
                <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                <span>Central DB Live</span>
              </div>

              <!-- Admin Profile / Logout Menu Dropdown -->
              <div class="relative" id="admin-profile-menu-container">
                <button onclick="App.toggleProfileMenu()" class="flex items-center gap-2.5 p-1.5 rounded-xl hover:bg-gray-100 transition border border-transparent hover:border-gray-200">
                  <div class="w-8 h-8 rounded-full bg-[#0F382C] text-[#DFB15B] flex items-center justify-center text-xs font-bold ring-2 ring-[#DFB15B]/40">
                    ${admin.name.charAt(0)}
                  </div>
                  <div class="hidden md:block text-left">
                    <p class="text-xs font-bold text-gray-800 leading-tight">${admin.name}</p>
                    <p class="text-[10px] text-gray-500 leading-tight">${admin.email}</p>
                  </div>
                  <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
                </button>

                <!-- Profile Dropdown Menu -->
                <div id="admin-profile-dropdown" class="hidden absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-xl border border-gray-200 py-2 z-50 animate-fade-in">
                  <div class="px-4 py-2 border-b border-gray-100">
                    <p class="text-xs font-bold text-gray-800">${admin.name}</p>
                    <p class="text-[11px] text-gray-500 truncate">${admin.email}</p>
                    <span class="inline-block mt-1 px-2 py-0.5 rounded text-[10px] font-semibold bg-emerald-50 text-emerald-800">${admin.role}</span>
                  </div>
                  <a href="/admin/settings" onclick="App.navigate('/admin/settings', event); App.toggleProfileMenu();" class="flex items-center gap-2 px-4 py-2 text-xs text-gray-700 hover:bg-gray-50">
                    <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path></svg>
                    <span>System Settings</span>
                  </a>
                  <div class="border-t border-gray-100 my-1"></div>
                  <button onclick="SitaramAuth.logout()" class="w-full text-left flex items-center gap-2 px-4 py-2 text-xs text-red-600 hover:bg-red-50 font-medium">
                    <svg class="w-4 h-4 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
                    <span>Secure Sign Out</span>
                  </button>
                </div>
              </div>
            </div>
          </header>

          <!-- Main Content Body -->
          <main class="flex-1 overflow-y-auto p-4 sm:p-6 lg:p-8 custom-scrollbar">
            ${contentHtml}
          </main>
        </div>
      </div>
    `;
  },

  // Module Placeholder View (for non-dashboard routes in this foundation step)
  placeholder: (title, description, iconSvg) => `
    <div class="max-w-4xl mx-auto space-y-6">
      <div class="flex items-center justify-between border-b border-[#E2D9CC] pb-4">
        <div>
          <h1 class="font-serif text-2xl font-bold text-[#0F382C]">${title}</h1>
          <p class="text-sm text-gray-500 mt-0.5">${description}</p>
        </div>
      </div>

      <div class="bg-white rounded-2xl border border-[#E2D9CC] p-12 text-center shadow-xs">
        <div class="w-16 h-16 mx-auto rounded-2xl bg-amber-50 text-[#1B4D3E] flex items-center justify-center mb-4 border border-amber-200">
          <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">${iconSvg}</svg>
        </div>
        <h3 class="font-serif font-bold text-lg text-gray-800">${title} Module Foundation Ready</h3>
        <p class="text-xs text-gray-500 max-w-md mx-auto mt-1.5 leading-relaxed">
          The routing, backend schema, and database models are configured. Detailed management tools will be activated in the subsequent implementation steps.
        </p>
        <div class="mt-6 flex justify-center gap-3">
          <a href="/admin/dashboard" onclick="App.navigate('/admin/dashboard', event)" class="px-4 py-2 rounded-lg bg-[#1B4D3E] text-white text-xs font-semibold hover:bg-[#0F382C] shadow-sm">
            Return to Dashboard
          </a>
        </div>
      </div>
    </div>
  `
};

window.Views = Views;
