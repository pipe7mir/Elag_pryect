/**
 * ELAG - CASA DE MODAS | Sistema de Gestión v3.0 (Professional Edition)
 * Integrado con Backend Java PedidoController
 */

const ElagApp = {
    config: {
        diasEntrega: 15,
        numeroWhatsApp: "573000000000",
        moneda: 'es-CO',
        simboloMoneda: 'COP'
    },

    state: {
        productos: [],
        carrito: []
    },

    // 1. UTILIDADES AVANZADAS
    utils: {
        formatter: new Intl.NumberFormat('es-CO', {
            style: 'currency', currency: 'COP', maximumFractionDigits: 0
        }),

        format(valor) { return this.formatter.format(valor); },

        // Algoritmo sincronizado con LogisticaService.java
        calcularFechaEntrega(diasHabiles) {
            let fecha = new Date();
            let contador = 0;
            while (contador < diasHabiles) {
                fecha.setDate(fecha.getDate() + 1);
                const diaSemana = fecha.getDay(); // 0: Domingo, 6: Sábado
                if (diaSemana !== 0 && diaSemana !== 6) contador++;
            }
            return fecha.toLocaleDateString('es-ES', { 
                weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' 
            });
        }
    },

    // 2. GESTOR DE PERSISTENCIA Y SINCRONIZACIÓN
    data: {
        init() {
            // Carga inicial (Simulando base de datos o LocalStorage)
            this.cargarCatalogo();
            ElagApp.state.carrito = JSON.parse(localStorage.getItem('elag_carrito')) || [];
        },

        cargarCatalogo() {
            // Datos quemados para desarrollo, escalable a Fetch API
            ElagApp.state.productos = [
                {
                    id: 1,
                    nombre: "Uniforme Médico Anti-fluido",
                    imagen: "../assets/uniforme-medico.jpg",
                    opciones: [
                        { etiqueta: "Talla S - M", precio: 85000 },
                        { etiqueta: "Talla L - XL", precio: 95000 }
                    ]
                },
                {
                    id: 2,
                    nombre: "Bata de Laboratorio Blanca",
                    imagen: "../assets/bata.jpg",
                    opciones: [
                        { etiqueta: "Tela Estándar", precio: 45000 },
                        { etiqueta: "Tela Premium", precio: 65000 }
                    ]
                }
            ];
        },

        sincronizar() {
            localStorage.setItem('elag_carrito', JSON.stringify(ElagApp.state.carrito));
            ElagApp.ui.renderizarResumen();
        }
    },

    // 3. INTERFAZ DE USUARIO (UI)
    ui: {
        renderizarCatalogo() {
            const container = document.getElementById('contenedor-productos');
            if (!container) return;

            container.innerHTML = ElagApp.state.productos.map(prod => `
                <article class="producto-card">
                    <img src="${prod.imagen}" alt="${prod.nombre}" loading="lazy">
                    <h3>${prod.nombre}</h3>
                    <div class="control-group">
                        <label>Variante:</label>
                        <select id="select-${prod.id}" 
                                onchange="ElagApp.ui.actualizarPrecio(${prod.id}, this.value)">
                            ${prod.opciones.map(o => `<option value="${o.precio}">${o.etiqueta}</option>`).join('')}
                        </select>
                    </div>
                    <div class="control-group">
                        <label>Cantidad:</label>
                        <input type="number" id="cant-${prod.id}" value="1" min="1">
                    </div>
                    <p class="precio-preview">Precio: <span id="p-${prod.id}">${ElagApp.utils.format(prod.opciones[0].precio)}</span></p>
                    <button class="btn-add" onclick="ElagApp.cart.agregar(${prod.id})">Añadir al Pedido</button>
                </article>
            `).join('');
        },

        actualizarPrecio(id, precio) {
            const el = document.getElementById(`p-${id}`);
            if (el) el.innerText = ElagApp.utils.format(parseInt(precio));
        },

        renderizarResumen() {
            const lista = document.getElementById('lista-pedido');
            const totalEl = document.getElementById('total-pedido');
            const entregaEl = document.getElementById('fecha-entrega-texto');
            
            if (!lista) return;

            let total = 0;
            lista.innerHTML = ElagApp.state.carrito.length ? ElagApp.state.carrito.map((item, i) => {
                total += item.subtotal;
                return `
                    <div class="item-carrito">
                        <div class="item-info">
                            <strong>${item.cantidad}x ${item.nombre}</strong>
                            <small>${item.variante}</small>
                        </div>
                        <div class="item-actions">
                            <span>${ElagApp.utils.format(item.subtotal)}</span>
                            <button class="btn-del" onclick="ElagApp.cart.eliminar(${i})">✕</button>
                        </div>
                    </div>
                `;
            }).join('') : "<p class='empty-msg'>No hay productos seleccionados.</p>";

            if (totalEl) totalEl.innerText = ElagApp.utils.format(total);
            if (entregaEl) entregaEl.innerText = ElagApp.utils.calcularFechaEntrega(ElagApp.config.diasEntrega);
        }
    },

    // 4. LÓGICA DE NEGOCIO (CART & CHECKOUT)
    cart: {
        agregar(id) {
            const prod = ElagApp.state.productos.find(p => p.id === id);
            const sel = document.getElementById(`select-${id}`);
            const cant = parseInt(document.getElementById(`cant-${id}`).value);

            if (cant < 1) return alert("Cantidad no válida");

            const precio = parseInt(sel.value);
            const variante = sel.options[sel.selectedIndex].text;

            ElagApp.state.carrito.push({
                id, nombre: prod.nombre, variante, cantidad: cant,
                precioUnitario: precio, subtotal: precio * cant
            });

            ElagApp.data.sincronizar();
        },

        eliminar(index) {
            ElagApp.state.carrito.splice(index, 1);
            ElagApp.data.sincronizar();
        },

        /**
         * Simulación de envío al PedidoController.java
         */
        procesarPedido() {
            if (!ElagApp.state.carrito.length) return alert("El carrito está vacío.");

            const total = ElagApp.state.carrito.reduce((acc, item) => acc + item.subtotal, 0);
            
            // Estructura JSON compatible con el Backend Java trabajado anteriormente
            const payload = {
                cliente: prompt("Ingrese su nombre para el registro:"),
                institucion: prompt("Institución o Club (Opcional):") || "Particular",
                items: ElagApp.state.carrito,
                total: total,
                timestamp: new Date().toISOString()
            };

            console.log("SISTEMA: Enviando JSON a PedidoController...", payload);
            alert("Pedido sincronizado con el backend local (ver consola).");
            
            this.enviarWhatsApp(payload);
        },

        enviarWhatsApp(p) {
            let msg = `*NUEVO PEDIDO - ELAG CASA DE MODAS*\n`;
            msg += `👤 Cliente: ${p.cliente}\n`;
            msg += `🛡️ Institución: ${p.institucion}\n\n`;
            
            p.items.forEach(i => {
                msg += `- ${i.cantidad}x ${i.nombre} (${i.variante}): ${ElagApp.utils.format(i.subtotal)}\n`;
            });

            msg += `\n💰 *TOTAL: ${ElagApp.utils.format(p.total)}*`;
            msg += `\n📅 Entrega: ${ElagApp.utils.calcularFechaEntrega(ElagApp.config.diasEntrega)}`;

            const url = `https://wa.me/${ElagApp.config.numeroWhatsApp}?text=${encodeURIComponent(msg)}`;
            window.open(url, '_blank');
        }
    },

    init() {
        ElagApp.data.init();
        ElagApp.ui.renderizarCatalogo();
        ElagApp.ui.renderizarResumen();
    }
};

document.addEventListener('DOMContentLoaded', () => ElagApp.init());