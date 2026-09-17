/**
 * Sitaram Ayurveda Admin Website - Dashboard Component
 * Renders the 6 KPI cards and the 3 sections with proper loading, empty and error states.
 */

const DashboardView = {
  renderLoading: () => `
    <div class="space-y-6 animate-pulse">
      <div class="h-8 bg-gray-200 rounded w-1/3"></div>
      
      <!-- Skeleton 6 Cards -->
      <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
        ${[1, 2, 3, 4, 5, 6].map(() => `
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] space-y-2">
            <div class="h-3 bg-gray-200 rounded w-1/2"></div>
            <div class="h-8 bg-gray-200 rounded w-3/4"></div>
            <div class="h-2 bg-gray-100 rounded w-full"></div>
          </div>
        `).join('')}
      </div>

      <!-- Skeleton Sections -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="bg-white p-6 rounded-xl border border-[#E2D9CC] h-64"></div>
        <div class="bg-white p-6 rounded-xl border border-[#E2D9CC] h-64"></div>
      </div>
      <div class="bg-white p-6 rounded-xl border border-[#E2D9CC] h-64"></div>
    </div>
  `,

  renderError: (errorMsg) => `
    <div class="max-w-2xl mx-auto my-12 bg-white p-8 rounded-2xl border border-red-200 shadow-sm text-center space-y-4">
      <div class="w-12 h-12 rounded-full bg-red-100 text-red-600 flex items-center justify-center mx-auto">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
        </svg>
      </div>
      <h3 class="font-serif font-bold text-lg text-gray-800">Failed to Load Dashboard Telemetry</h3>
      <p class="text-xs text-gray-500">${errorMsg || 'A network error occurred while retrieving backend metrics.'}</p>
      <button onclick="App.loadDashboardData()" class="px-5 py-2 bg-[#1B4D3E] text-white text-xs font-semibold rounded-lg hover:bg-[#0F382C]">
        Retry Connection
      </button>
    </div>
  `,

  renderSuccess: (metrics, recentProducts, recentlyUpdated, categorySummary) => {
    return `
      <div class="space-y-6">
        <!-- Page Title & Header Banner -->
        <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 class="font-serif text-2xl font-bold text-[#0F382C]">Therapeutic Catalogue Dashboard</h1>
            <p class="text-xs text-gray-500 mt-0.5">Centralized master metrics for Sitaram Ayurveda classical formulations.</p>
          </div>
          <div class="flex items-center gap-2">
            <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-50 text-emerald-800 border border-emerald-200 text-xs font-semibold">
              <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
              <span>Central SQLite Live</span>
            </span>
            <button onclick="App.loadDashboardData()" class="p-2 text-gray-500 hover:text-gray-900 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition" title="Refresh Telemetry">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path></svg>
            </button>
          </div>
        </div>

        <!-- 6 Core Dashboard Metric Cards -->
        <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
          <!-- Card 1: Total Products -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-gray-500 uppercase tracking-wider">Total Products</p>
              <p class="font-serif text-2xl font-bold text-[#0F382C] mt-1">${metrics.totalProducts}</p>
            </div>
            <p class="text-[10px] text-gray-400 mt-2">Master Catalogue</p>
          </div>

          <!-- Card 2: Active Products -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-emerald-600 uppercase tracking-wider">Active Products</p>
              <p class="font-serif text-2xl font-bold text-emerald-700 mt-1">${metrics.activeProducts}</p>
            </div>
            <p class="text-[10px] text-emerald-600/70 mt-2">Live on Future App</p>
          </div>

          <!-- Card 3: Inactive Products -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-gray-400 uppercase tracking-wider">Inactive Products</p>
              <p class="font-serif text-2xl font-bold text-gray-600 mt-1">${metrics.inactiveProducts}</p>
            </div>
            <p class="text-[10px] text-gray-400 mt-2">Internal Drafts</p>
          </div>

          <!-- Card 4: Categories -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-[#9A7B38] uppercase tracking-wider">Categories</p>
              <p class="font-serif text-2xl font-bold text-[#9A7B38] mt-1">${metrics.categories}</p>
            </div>
            <p class="text-[10px] text-gray-400 mt-2">Handbook Classifications</p>
          </div>

          <!-- Card 5: Ingredients -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-purple-600 uppercase tracking-wider">Ingredients</p>
              <p class="font-serif text-2xl font-bold text-purple-700 mt-1">${metrics.ingredients}</p>
            </div>
            <p class="text-[10px] text-gray-400 mt-2">Botanical Directory</p>
          </div>

          <!-- Card 6: Manufacturers -->
          <div class="bg-white p-4 rounded-xl border border-[#E2D9CC] shadow-xs flex flex-col justify-between">
            <div>
              <p class="text-[11px] font-bold text-blue-600 uppercase tracking-wider">Manufacturers</p>
              <p class="font-serif text-2xl font-bold text-blue-700 mt-1">${metrics.manufacturers}</p>
            </div>
            <p class="text-[10px] text-gray-400 mt-2">Ayush GMP Units</p>
          </div>
        </div>

        <!-- 2-Column Row: Recent Products & Recently Updated Products -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- Section 1: Recent Products -->
          <div class="bg-white rounded-xl border border-[#E2D9CC] p-5 shadow-xs flex flex-col">
            <div class="flex items-center justify-between border-b border-gray-100 pb-3 mb-3">
              <div class="flex items-center gap-2">
                <div class="w-2.5 h-2.5 rounded-full bg-[#1B4D3E]"></div>
                <h2 class="font-serif font-bold text-sm text-[#0F382C]">Recent Products</h2>
              </div>
              <span class="text-[11px] text-gray-400">Newly Added Entries</span>
            </div>

            <div class="flex-1">
              ${recentProducts.length === 0 ? `
                <!-- Empty State -->
                <div class="h-48 flex flex-col items-center justify-center text-center p-6 bg-gray-50/50 rounded-xl border border-dashed border-gray-200">
                  <svg class="w-8 h-8 text-gray-300 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                  </svg>
                  <p class="text-xs font-semibold text-gray-600">No Recent Products Recorded</p>
                  <p class="text-[11px] text-gray-400 mt-0.5">When new products are added, they will display in this stream.</p>
                </div>
              ` : `
                <div class="overflow-x-auto">
                  <table class="w-full text-left text-xs">
                    <thead class="text-gray-400 uppercase text-[10px] font-semibold border-b border-gray-100">
                      <tr>
                        <th class="py-2">Code</th>
                        <th class="py-2">Formulation</th>
                        <th class="py-2">Category</th>
                        <th class="py-2 text-center">Status</th>
                      </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50">
                      ${recentProducts.map(p => `
                        <tr class="medicine-table-row hover:bg-gray-50 transition cursor-pointer">
                          <td class="py-2.5 font-mono text-[11px] text-gray-500">${p.code}</td>
                          <td class="py-2.5 font-serif font-bold text-[#0F382C]">${p.name}</td>
                          <td class="py-2.5 text-gray-600">${p.category_name}</td>
                          <td class="py-2.5 text-center">
                            <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold ${p.status === 'Active' ? 'bg-emerald-50 text-emerald-800' : 'bg-gray-100 text-gray-600'}">
                              ${p.status}
                            </span>
                          </td>
                        </tr>
                      `).join('')}
                    </tbody>
                  </table>
                </div>
              `}
            </div>
          </div>

          <!-- Section 2: Recently Updated Products -->
          <div class="bg-white rounded-xl border border-[#E2D9CC] p-5 shadow-xs flex flex-col">
            <div class="flex items-center justify-between border-b border-gray-100 pb-3 mb-3">
              <div class="flex items-center gap-2">
                <div class="w-2.5 h-2.5 rounded-full bg-[#DFB15B]"></div>
                <h2 class="font-serif font-bold text-sm text-[#0F382C]">Recently Updated Products</h2>
              </div>
              <span class="text-[11px] text-gray-400">Modification Log</span>
            </div>

            <div class="flex-1">
              ${recentlyUpdated.length === 0 ? `
                <!-- Empty State -->
                <div class="h-48 flex flex-col items-center justify-center text-center p-6 bg-gray-50/50 rounded-xl border border-dashed border-gray-200">
                  <svg class="w-8 h-8 text-gray-300 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                  </svg>
                  <p class="text-xs font-semibold text-gray-600">No Formulations Recently Modified</p>
                  <p class="text-[11px] text-gray-400 mt-0.5">Edits to classical formulations and indications will appear here.</p>
                </div>
              ` : `
                <div class="overflow-x-auto">
                  <table class="w-full text-left text-xs">
                    <thead class="text-gray-400 uppercase text-[10px] font-semibold border-b border-gray-100">
                      <tr>
                        <th class="py-2">Code</th>
                        <th class="py-2">Formulation</th>
                        <th class="py-2">Category</th>
                        <th class="py-2 text-right">Modified</th>
                      </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50">
                      ${recentlyUpdated.map(p => `
                        <tr class="medicine-table-row hover:bg-gray-50 transition cursor-pointer">
                          <td class="py-2.5 font-mono text-[11px] text-gray-500">${p.code}</td>
                          <td class="py-2.5 font-serif font-bold text-[#0F382C]">${p.name}</td>
                          <td class="py-2.5 text-gray-600">${p.category_name}</td>
                          <td class="py-2.5 text-right text-gray-400 text-[10px]">
                            ${p.updated_at ? p.updated_at.split('T')[0] : 'Today'}
                          </td>
                        </tr>
                      `).join('')}
                    </tbody>
                  </table>
                </div>
              `}
            </div>
          </div>
        </div>

        <!-- Section 3: Category Summary -->
        <div class="bg-white rounded-xl border border-[#E2D9CC] p-5 shadow-xs flex flex-col">
          <div class="flex items-center justify-between border-b border-gray-100 pb-3 mb-3">
            <div>
              <h2 class="font-serif font-bold text-base text-[#0F382C]">Category Summary</h2>
              <p class="text-xs text-gray-500 mt-0.5">Therapeutic Index classification volume breakdown across all 24 classical categories.</p>
            </div>
            <span class="text-xs font-semibold text-[#9A7B38] bg-amber-50 px-2.5 py-1 rounded-full border border-amber-200">
              24 Master Categories
            </span>
          </div>

          <div class="flex-1">
            ${categorySummary.length === 0 ? `
              <!-- Empty State -->
              <div class="h-48 flex flex-col items-center justify-center text-center p-8 bg-gray-50/50 rounded-xl border border-dashed border-gray-200">
                <svg class="w-10 h-10 text-gray-300 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
                </svg>
                <p class="text-xs font-semibold text-gray-600">No Category Data Available</p>
                <p class="text-[11px] text-gray-400 mt-0.5">Categories from the Therapeutic Index Handbook have not yet loaded.</p>
              </div>
            ` : `
              <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
                ${categorySummary.map(c => `
                  <div class="p-3 rounded-xl border border-gray-100 bg-[#FBF9F5] hover:border-[#1B4D3E]/30 transition flex flex-col justify-between">
                    <div>
                      <div class="flex items-center justify-between">
                        <span class="font-mono text-[10px] text-gray-500 bg-white px-1.5 py-0.5 rounded border border-gray-200">${c.code}</span>
                        <span class="px-2 py-0.5 rounded-full text-[10px] font-bold ${c.product_count > 0 ? 'bg-emerald-100 text-emerald-800' : 'bg-gray-100 text-gray-400'}">
                          ${c.product_count} ${c.product_count === 1 ? 'Product' : 'Products'}
                        </span>
                      </div>
                      <h4 class="font-serif font-bold text-xs text-[#0F382C] mt-2">${c.name}</h4>
                      <p class="text-[10px] text-gray-500 mt-0.5 line-clamp-2" title="${c.description}">${c.description}</p>
                    </div>
                  </div>
                `).join('')}
              </div>
            `}
          </div>
        </div>
      </div>
    `;
  }
};

window.DashboardView = DashboardView;
