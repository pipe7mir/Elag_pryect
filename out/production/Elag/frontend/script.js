/**
 * ELAG - CASA DE MODAS | Sistema de Gestión v2.2 (Final Corrected)
 */

const ElagApp = {
    // 1. CONFIGURACIÓN
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

    // 2. UTILIDADES
    utils: {
        formatter: new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP',
            maximumFractionDigits: 0
        }),

        format(valor) { return this.formatter.format(valor); },

        calcularFechaEntrega(diasHabiles) {
            let fecha = new Date();
            let contador = 0;
            while (contador < diasHabiles) {
                fecha.setDate(fecha.getDate() + 1);
                const diaSemana = fecha.getDay();
                if (diaSemana !== 0 && diaSemana !== 6) contador++;
            }
            return fecha.toLocaleDateString('es-ES', { 
                weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' 
            });
        },

        tryParse(key, fallback) {
            try {
                const item = localStorage.getItem(key);
                return item ? JSON.parse(item) : fallback;
            } catch (e) {
                console.error(`Error en localStorage[${key}]:`, e);
                return fallback;
            }
        }
    },

    // 3. GESTOR DE DATOS
    data: {
        init() {
            // Carga inicial de productos (Fuentes de datos)
            ElagApp.state.productos = ElagApp.utils.tryParse('elag_productos', [
                {
                    id: 1,
                    nombre: "Uniforme Médico Anti-fluido",
                    imagen: "../assets/uniforme-medico.jpg",
                    opciones: [
                        { etiqueta: "Talla S - M", precio: 85000 },
                        { etiqueta: "Talla L - XL", precio: 95000 },
                        { etiqueta: "Personalizado (Medidas)", precio: 120000 }
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
            ]);
            ElagApp.state.carrito = ElagApp.utils.tryParse('elag_carrito', []);
        },

        sincronizar() {
            localStorage.setItem('elag_carrito', JSON.stringify(ElagApp.state.carrito));
            ElagApp.ui.renderizarResumen();
        }
    },

    // 4. INTERFAZ DE USUARIO
    ui: {
        renderizarCatalogo() {
            const contenedor = document.getElementById('contenedor-productos');
            if (!contenedor) return;

            contenedor.innerHTML = ElagApp.state.productos.map(prod => `
                <article class="producto-card">
                    <img src="${prod.imagen}" alt="${prod.nombre}" loading="lazy">
                    <h3>${prod.nombre}</h3>
                    <label>Variante:</label>
                    <select id="select-${prod.id}" onchange="ElagApp.ui.actualizarPrecioPreview(${prod.id}, this.value)">
                        ${prod.opciones.map(opt => `<option value="${opt.precio}">${opt.etiqueta} - ${ElagApp.utils.format(opt.precio)}</option>`).join('')}
                    </select>
                    <div class="cantidad-container">
                        <input type="number" id="cant-${prod.id}" value="1" min="1">
                    </div>
                    <p>Unitario: <span id="p-${prod.id}">${ElagApp.utils.format(prod.opciones[0].precio)}</span></p>
                    <button onclick="ElagApp.cart.agregar(${prod.id})">Añadir al Pedido</button>
                </article>
            `).join('');
        },

        actualizarPrecioPreview(id, precio) {
            const span = document.getElementById(`p-${id}`);
            if (span) span.innerText = ElagApp.utils.format(parseInt(precio));
        },

        renderizarResumen() {
            const lista = document.getElementById('lista-pedido');
            const totalEl = document.getElementById('total-pedido');
            const txtEntrega = document.getElementById('contador-entrega');
            if (!lista) return;

            if (ElagApp.state.carrito.length === 0) {
                lista.innerHTML = "<p style='text-align:center;color:#999;'>Carrito vacío</p>";
                if (totalEl) totalEl.innerText = "$0";
                return;
            }

            let totalGeneral = 0;
            lista.innerHTML = ElagApp.state.carrito.map((item, index) => {
                totalGeneral += item.subtotal;
                return `
                    <div class="item-carrito">
                        <div><strong>${item.cantidad}x ${item.nombre}</strong><br><small>${item.variante}</small></div>
                        <div class="item-precio">
                            <span>${ElagApp.utils.format(item.subtotal)}</span>
                            <button onclick="ElagApp.cart.eliminar(${index})">✕</button>
                        </div>
                    </div>
                `;
            }).join('');

            if (totalEl) totalEl.innerText = ElagApp.utils.format(totalGeneral);
            if (txtEntrega) txtEntrega.innerText = ElagApp.utils.calcularFechaEntrega(ElagApp.config.diasEntrega);
        }
    },

    // 5. MANEJADOR DE EVENTOS
    cart: {
        agregar(idProducto) {
            const producto = ElagApp.state.productos.find(p => p.id === idProducto);
            const select = document.getElementById(`select-${idProducto}`);
            const cantInput = document.getElementById(`cant-${idProducto}`);
            const cantidad = parseInt(cantInput.value);

            if (!producto || cantidad < 1) return alert("Verifique la cantidad");

            const precioUnitario = parseInt(select.value);
            const etiqueta = select.options[select.selectedIndex].text.split(' - ')[0];

            ElagApp.state.carrito.push({
                nombre: producto.nombre,
                variante: etiqueta,
                cantidad,
                precioUnitario,
                subtotal: precioUnitario * cantidad
            });

            cantInput.value = 1;
            ElagApp.data.sincronizar();
        },
        eliminar(indice) {
            ElagApp.state.carrito.splice(indice, 1);
            ElagApp.data.sincronizar();
        }
    },

    // 6. INICIALIZACIÓN    
    init() {
        ElagApp.data.init();
        ElagApp.ui.renderizarCatalogo();
        ElagApp.ui.renderizarResumen();
    }
};

document.addEventListener('DOMContentLoaded', () => ElagApp.init());    
