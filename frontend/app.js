/* =========================================
   TODOGRIFOS FRONTEND — APP LOGIC
   ========================================= */

const API = 'http://localhost:8080';
let token = null;

/* ---- UTILS ---- */
const $ = id => document.getElementById(id);

function money(val) {
  const n = parseFloat(val) || 0;
  return '$' + n.toLocaleString('es-CL');
}

function badge(text, color) {
  return `<span class="badge badge-${color}">${text}</span>`;
}

/* ---- AUTH ---- */
async function handleLogin(e) {
  e.preventDefault();
  const user = $('login-user').value.trim();
  const pass = $('login-pass').value.trim();

  $('login-label').classList.add('hidden');
  $('login-spinner').classList.remove('hidden');
  $('login-btn').disabled = true;
  $('login-error').classList.add('hidden');

  try {
    const res = await fetch(`${API}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: user, password: pass })
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || 'Credenciales inválidas');
    }

    const data = await res.json();
    token = data.token || data.jwt || data.access_token || (typeof data === 'string' ? data : null);
    if (!token) throw new Error('El servidor no retornó un token JWT válido.');

    $('user-name-display').textContent = user;
    $('login-screen').classList.remove('active');
    $('app-screen').classList.add('active');

    loadDashboard();
    showSection('dashboard', document.querySelector('#nav-dashboard'));

  } catch (err) {
    $('login-error').textContent = err.message;
    $('login-error').classList.remove('hidden');
  } finally {
    $('login-label').classList.remove('hidden');
    $('login-spinner').classList.add('hidden');
    $('login-btn').disabled = false;
  }
}

function logout() {
  token = null;
  $('app-screen').classList.remove('active');
  $('login-screen').classList.add('active');
}

/* ---- NAVIGATION ---- */
const sectionTitles = {
  dashboard:    'Dashboard',
  clientes:     'Clientes',
  productos:    'Catálogo de Productos',
  inventario:   'Control de Inventario',
  ventas:       'Registro de Ventas',
  compras:      'Órdenes de Compra',
  proveedores:  'Proveedores',
  vendedores:   'Equipo de Vendedores',
  despachos:    'Gestión de Despachos',
  devoluciones: 'Devoluciones',
};

function showSection(name, btn) {
  document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
  $(`section-${name}`).classList.add('active');
  if (btn) btn.classList.add('active');
  $('topbar-title').textContent = sectionTitles[name] || name;
  if (name !== 'dashboard') loadSection(name);
}

/* ---- API CALLS ---- */
async function apiFetch(path) {
  const res = await fetch(`${API}${path}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (!res.ok) throw new Error(`Error ${res.status}: ${res.statusText}`);
  return res.json();
}

/* ---- RENDER HELPERS ---- */
function renderTable(containerId, columns, rows, emptyMsg = 'Sin registros') {
  const el = $(containerId);
  if (!rows || rows.length === 0) {
    el.innerHTML = `<div class="loading-msg">${emptyMsg}</div>`;
    return;
  }
  const heads = columns.map(c => `<th>${c.label}</th>`).join('');
  const trs = rows.map(row => {
    const tds = columns.map(c => `<td>${c.render ? c.render(row) : (row[c.key] ?? '—')}</td>`).join('');
    return `<tr>${tds}</tr>`;
  }).join('');
  el.innerHTML = `<table class="data-table"><thead><tr>${heads}</tr></thead><tbody>${trs}</tbody></table>`;
}

function setError(containerId, msg) {
  const el = $(containerId);
  el.innerHTML = `<div class="loading-msg" style="color:#f87171;">⚠️ ${msg}</div>`;
}

/* ---- DASHBOARD ---- */
async function loadDashboard() {
  const endpoints = [
    { stat: 'clientes',    path: '/api/clientes' },
    { stat: 'productos',   path: '/api/productos' },
    { stat: 'ventas',      path: '/api/ventas' },
    { stat: 'inventario',  path: '/api/inventario' },
    { stat: 'compras',     path: '/api/compras' },
    { stat: 'proveedores', path: '/api/proveedores' },
    { stat: 'despachos',   path: '/api/despachos' },
    { stat: 'devoluciones',path: '/api/devoluciones' },
  ];

  for (const ep of endpoints) {
    try {
      const data = await apiFetch(ep.path);
      const count = Array.isArray(data) ? data.length : (data.content?.length ?? data.total ?? Object.keys(data).length);
      $(`stat-${ep.stat}`).textContent = count;
    } catch {
      $(`stat-${ep.stat}`).textContent = 'N/A';
    }
  }
}

/* ---- SECTION LOADERS ---- */
const loaders = {

  // Clientes: campos reales -> nombres, appaterno, apmaterno, rut, email, telefono
  async clientes() {
    const data = await apiFetch('/api/clientes');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-clientes', [
      { key: 'id',        label: 'ID' },
      { key: 'nombres',   label: 'Nombres',       render: r => r.nombres ?? '—' },
      { key: 'appaterno', label: 'Ap. Paterno',   render: r => r.appaterno ?? r.ap_paterno ?? '—' },
      { key: 'apmaterno', label: 'Ap. Materno',   render: r => r.apmaterno ?? r.ap_materno ?? '—' },
      { key: 'rut',       label: 'RUT' },
      { key: 'email',     label: 'Email' },
      { key: 'telefono',  label: 'Teléfono',      render: r => r.telefono ?? '—' },
      { key: 'direccion', label: 'Dirección',     render: r => r.direccion ?? '—' },
    ], rows, 'No hay clientes registrados aún.');
  },

  // Productos: campos reales -> sku, nombre, precio, activo, garantiaMeses, marca.nombre, categoria.nombre
  async productos() {
    const data = await apiFetch('/api/productos');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-productos', [
      { key: 'id',     label: 'ID' },
      { key: 'sku',    label: 'SKU',        render: r => r.sku ? `<span class="sku">${r.sku}</span>` : '—' },
      { key: 'nombre', label: 'Nombre' },
      { key: 'precio', label: 'Precio',     render: r => `<span class="money">${money(r.precio ?? r.price)}</span>` },
      { key: 'marca',  label: 'Marca',      render: r => r.marca?.nombre ?? r.marcaNombre ?? r.marca ?? '—' },
      { key: 'cat',    label: 'Categoría',  render: r => r.categoria?.nombre ?? r.categoriaNombre ?? r.categoria ?? '—' },
      { key: 'garantia', label: 'Garantía', render: r => r.garantiaMeses != null ? `${r.garantiaMeses} meses` : (r.garantia_meses != null ? `${r.garantia_meses} meses` : '—') },
      { key: 'activo', label: 'Estado',     render: r => r.activo || r.active ? badge('Activo', 'green') : badge('Inactivo', 'red') },
    ], rows, 'No hay productos en el catálogo aún.');
  },

  // Inventario: campos reales -> sku, cantidad, stockMinimo (o stock_minimo)
  async inventario() {
    const data = await apiFetch('/api/inventario');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-inventario', [
      { key: 'id',  label: 'ID' },
      { key: 'sku', label: 'SKU',          render: r => r.sku ? `<span class="sku">${r.sku}</span>` : '—' },
      { key: 'cantidad', label: 'Stock',   render: r => {
          const q = r.cantidad ?? r.stock ?? r.quantity ?? 0;
          const min = r.stockMinimo ?? r.stock_minimo ?? 10;
          const color = q <= 0 ? 'red' : q <= min ? 'yellow' : 'green';
          return badge(q, color);
      }},
      { key: 'stockMinimo', label: 'Stock Mínimo', render: r => r.stockMinimo ?? r.stock_minimo ?? '—' },
    ], rows, 'No hay registros de inventario aún.');
  },

  // Ventas: campos reales -> folio, fechaVenta, clienteId, vendedorId, total
  async ventas() {
    const data = await apiFetch('/api/ventas');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-ventas', [
      { key: 'id',         label: 'ID' },
      { key: 'folio',      label: 'Folio',      render: r => r.folio ? `<span class="sku">${r.folio}</span>` : '—' },
      { key: 'clienteId',  label: 'Cliente ID', render: r => r.clienteId ?? '—' },
      { key: 'vendedorId', label: 'Vendedor ID',render: r => r.vendedorId ?? '—' },
      { key: 'total',      label: 'Total',      render: r => `<span class="money">${money(r.total ?? r.totalAmount)}</span>` },
      { key: 'fechaVenta', label: 'Fecha',      render: r => {
          const f = r.fechaVenta ?? r.fecha_venta ?? r.fecha;
          return f ? new Date(f).toLocaleDateString('es-CL') : '—';
      }},
    ], rows, 'No hay ventas registradas aún.');
  },

  // Compras: campos reales -> numeroFactura, fechaCompra, proveedor (RUT), total
  async compras() {
    const data = await apiFetch('/api/compras');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-compras', [
      { key: 'id',             label: 'ID' },
      { key: 'numeroFactura',  label: 'N° Factura',   render: r => r.numeroFactura ?? r.numero_factura ?? '—' },
      { key: 'proveedor',      label: 'Proveedor RUT', render: r => r.proveedor ?? r.proveedorId ?? '—' },
      { key: 'total',          label: 'Total',        render: r => `<span class="money">${money(r.total ?? r.totalAmount)}</span>` },
      { key: 'fechaCompra',    label: 'Fecha',        render: r => {
          const f = r.fechaCompra ?? r.fecha_compra ?? r.fecha;
          return f ? new Date(f).toLocaleDateString('es-CL') : '—';
      }},
    ], rows, 'No hay órdenes de compra aún.');
  },

  // Proveedores: campos reales -> rut, razonSocial, email, telefono, direccion
  async proveedores() {
    const data = await apiFetch('/api/proveedores');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-proveedores', [
      { key: 'id',          label: 'ID' },
      { key: 'rut',         label: 'RUT' },
      { key: 'razonSocial', label: 'Razón Social',  render: r => r.razonSocial ?? r.razon_social ?? r.nombre ?? '—' },
      { key: 'email',       label: 'Email',         render: r => r.email ?? '—' },
      { key: 'telefono',    label: 'Teléfono',      render: r => r.telefono ?? '—' },
      { key: 'direccion',   label: 'Dirección',     render: r => r.direccion ?? '—' },
    ], rows, 'No hay proveedores registrados aún.');
  },

  // Vendedores: campos reales -> codigoInterno, rut, nombre, sucursal, porcentajeComision, comisionAcumulada
  async vendedores() {
    const data = await apiFetch('/api/vendedores');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-vendedores', [
      { key: 'id',                 label: 'ID' },
      { key: 'codigoInterno',      label: 'Código',          render: r => r.codigoInterno ?? r.codigo_interno ? `<span class="sku">${r.codigoInterno ?? r.codigo_interno}</span>` : '—' },
      { key: 'nombre',             label: 'Nombre' },
      { key: 'rut',                label: 'RUT' },
      { key: 'sucursal',           label: 'Sucursal' },
      { key: 'porcentajeComision', label: 'Comisión %',      render: r => {
          const v = r.porcentajeComision ?? r.porcentaje_comision ?? r.comision;
          return v != null ? `${v}%` : '—';
      }},
      { key: 'comisionAcumulada',  label: 'Comisión Acum.',  render: r => {
          const v = r.comisionAcumulada ?? r.comision_acumulada;
          return v != null ? `<span class="money">${money(v)}</span>` : '—';
      }},
    ], rows, 'No hay vendedores registrados aún.');
  },

  // Despachos: campos reales -> codigoSeguimiento, ventaId, direccionEnvio, comuna, estado, fechaCreacion
  async despachos() {
    const data = await apiFetch('/api/despachos');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-despachos', [
      { key: 'id',                 label: 'ID' },
      { key: 'codigoSeguimiento',  label: 'Tracking',        render: r => r.codigoSeguimiento ?? r.codigo_seguimiento ? `<span class="sku">${r.codigoSeguimiento ?? r.codigo_seguimiento}</span>` : '—' },
      { key: 'ventaId',            label: 'Venta ID',        render: r => r.ventaId ?? r.venta_id ?? '—' },
      { key: 'direccionEnvio',     label: 'Dirección',       render: r => r.direccionEnvio ?? r.direccion_envio ?? r.direccion ?? '—' },
      { key: 'comuna',             label: 'Comuna',          render: r => r.comuna ?? '—' },
      { key: 'estado',             label: 'Estado',          render: r => {
          const st = (r.estado ?? r.status ?? 'PENDIENTE').toUpperCase();
          const color = { ENTREGADO: 'green', EN_CAMINO: 'blue', PENDIENTE: 'yellow', CANCELADO: 'red' }[st] ?? 'gray';
          return badge(st.replace('_', ' '), color);
      }},
      { key: 'fechaCreacion',      label: 'Fecha',           render: r => {
          const f = r.fechaCreacion ?? r.fecha_creacion ?? r.fecha;
          return f ? new Date(f).toLocaleDateString('es-CL') : '—';
      }},
    ], rows, 'No hay despachos registrados aún.');
  },

  // Devoluciones: campos reales -> notaCreditoFolio, ventaId, sku, cantidad, motivo, destinoLogistico
  async devoluciones() {
    const data = await apiFetch('/api/devoluciones');
    const rows = Array.isArray(data) ? data : data.content ?? [];
    renderTable('table-devoluciones', [
      { key: 'id',                label: 'ID' },
      { key: 'notaCreditoFolio',  label: 'Nota de Crédito', render: r => r.notaCreditoFolio ?? r.nota_credito_folio ? `<span class="sku">${r.notaCreditoFolio ?? r.nota_credito_folio}</span>` : '—' },
      { key: 'ventaId',           label: 'Venta ID',        render: r => r.ventaId ?? r.venta_id ?? '—' },
      { key: 'sku',               label: 'SKU',             render: r => r.sku ? `<span class="sku">${r.sku}</span>` : '—' },
      { key: 'cantidad',          label: 'Cantidad' },
      { key: 'motivo',            label: 'Motivo',          render: r => r.motivo ?? r.reason ?? '—' },
      { key: 'destinoLogistico',  label: 'Destino',         render: r => {
          const d = (r.destinoLogistico ?? r.destino_logistico ?? 'PENDIENTE').toUpperCase();
          const color = { REINGRESADO_A_STOCK: 'green', DESECHADO: 'red', EN_REVISION: 'yellow' }[d] ?? 'gray';
          return badge(d.replace(/_/g, ' '), color);
      }},
    ], rows, 'No hay devoluciones registradas aún.');
  },
};

async function loadSection(name) {
  const containerId = `table-${name}`;
  const el = $(containerId);
  if (!el) return;
  el.innerHTML = `<div class="loading-msg">🔄 Consultando API...</div>`;
  try {
    await loaders[name]();
  } catch (err) {
    setError(containerId, err.message);
  }
}
