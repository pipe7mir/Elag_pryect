// 1. Persistencia: Cargar productos de localStorage o usar los predeterminados
let productos = JSON.parse(localStorage.getItem('elag_productos')) || [
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
];

let carrito = [];

// 2. Lógica del Contador de Entrega (15 días hábiles)
function calcularFechaEntrega(diasHabil) {
    let fecha = new Date();
    let contador = 0;
    while (contador < diasHabil) {
        fecha.setDate(fecha.getDate() + 1);
        let diaSemana = fecha.getDay();
        if (diaSemana !== 0 && diaSemana !== 6) contador++;
    }
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return fecha.toLocaleDateString('es-ES', opciones);
}

// 3. Renderizar productos en el catálogo
function renderizarProductos() {
    const contenedor = document.getElementById('contenedor-productos');
    if (!contenedor) return;
    contenedor.innerHTML = "";

    productos.forEach(prod => {
        const opcionesHTML = prod.opciones.map(opt => 
            `<option value="${opt.precio}">${opt.etiqueta} - $${opt.precio.toLocaleString()}</option>`
        ).join('');

        const card = document.createElement('article');
        card.className = 'producto-card';
        card.innerHTML = `
            <img src="${prod.imagen}" alt="${prod.nombre}">
            <h3>${prod.nombre}</h3>
            <label>Variante/Talla:</label>
            <select id="select-${prod.id}" onchange="actualizarPrecioVisual(this, 'p-${prod.id}')">
                ${opcionesHTML}
            </select>
            <div class="cantidad-container" style="margin: 10px 0;">
                <label>Cantidad:</label>
                <input type="number" id="cant-${prod.id}" value="1" min="1" style="width: 50px;">
            </div>
            <p>Precio Unitario: <span id="p-${prod.id}">$${prod.opciones[0].precio.toLocaleString()}</span></p>
            <button onclick="agregarAlPedido(${prod.id})">Añadir al Pedido</button>
        `;
        contenedor.appendChild(card);
    });
}

// 4. Actualizar el precio visual
function actualizarPrecioVisual(select, idSpan) {
    const precio = parseInt(select.value);
    document.getElementById(idSpan).innerText = `$${precio.toLocaleString()}`;
}

// 5. Agregar producto al carrito
function agregarAlPedido(idProducto) {
    const producto = productos.find(p => p.id === idProducto);
    const select = document.getElementById(`select-${idProducto}`);
    const inputCant = document.getElementById(`cant-${idProducto}`);

    const precioUnitario = parseInt(select.value);
    const cantidad = parseInt(inputCant.value);
    const etiqueta = select.options[select.selectedIndex].text.split(' - ')[0];

    carrito.push({
        nombre: producto.nombre,
        variante: etiqueta,
        cantidad: cantidad,
        precioUnitario: precioUnitario,
        subtotal: precioUnitario * cantidad
    });

    actualizarResumenVisual();
    inputCant.value = 1;
}

// 6. Actualizar el resumen lateral con botón de eliminar
function actualizarResumenVisual() {
    const lista = document.getElementById('lista-pedido');
    const totalElemento = document.getElementById('total-pedido');
    lista.innerHTML = "";
    let totalGeneral = 0;

    carrito.forEach((item, index) => {
        const div = document.createElement('div');
        div.style.cssText = "display: flex; justify-content: space-between; margin-bottom: 10px; border-bottom: 1px solid #eee; padding-bottom: 5px;";
        div.innerHTML = `
            <div>
                <strong>${item.cantidad}x ${item.nombre}</strong><br>
                <small>${item.variante}</small>
            </div>
            <div>
                <span>$${item.subtotal.toLocaleString()}</span>
                <button onclick="eliminarDelPedido(${index})" style="background:none; border:none; color:red; cursor:pointer; margin-left:10px;">✕</button>
            </div>
        `;
        lista.appendChild(div);
        totalGeneral += item.subtotal;
    });

    if (totalElemento) totalElemento.innerText = `$${totalGeneral.toLocaleString()}`;
}

// 7. Eliminar item
function eliminarDelPedido(index) {
    carrito.splice(index, 1);
    actualizarResumenVisual();
}

// 8. Enviar pedido a WhatsApp
function enviarPedidoWhatsApp() {
    if (carrito.length === 0) return alert("El carrito está vacío");

    let mensaje = "🛍️ *Nuevo Pedido - Elag Casa de Modas*\n\n";
    let total = 0;
    carrito.forEach(item => {
        mensaje += `• ${item.cantidad}x ${item.nombre} (${item.variante}): $${item.subtotal.toLocaleString()}\n`;
        total += item.subtotal;
    });
    mensaje += `\n💰 *Total: $${total.toLocaleString()}*`;
    mensaje += `\n📅 *Entrega estimada:* ${calcularFechaEntrega(15)}`;

    const url = `https://api.whatsapp.com/send?phone=TU_NUMERO_AQUI&text=${encodeURIComponent(mensaje)}`;
    window.open(url, '_blank');
}

// 9. Inicialización
document.addEventListener('DOMContentLoaded', () => {
    renderizarProductos();
    const txtEntrega = document.getElementById('contador-entrega');
    if (txtEntrega) txtEntrega.innerText = calcularFechaEntrega(15);
    
    const btnFinalizar = document.getElementById('btn-finalizar');
    if (btnFinalizar) btnFinalizar.onclick = enviarPedidoWhatsApp;
});
// Función para limpiar todo el pedido
function vaciarCarrito() {
    if (confirm("¿Estás seguro de que deseas borrar todo el pedido?")) {
        carrito = [];
        actualizarResumenVisual();
    }
}

// Mejora en la visualización del Total
function actualizarResumenVisual() {
    const lista = document.getElementById('lista-pedido');
    const totalElemento = document.getElementById('total-pedido');
    
    lista.innerHTML = "";
    let totalGeneral = 0;

    if (carrito.length === 0) {
        lista.innerHTML = "<p style='color: #999; text-align: center;'>Tu pedido está vacío</p>";
        if (totalElemento) totalElemento.innerText = "$0";
        return;
    }

    carrito.forEach((item, index) => {
        totalGeneral += item.subtotal;
        lista.innerHTML += `
            <div class="item-carrito">
                <div>
                    <strong>${item.cantidad}x ${item.nombre}</strong><br>
                    <small>${item.variante}</small>
                </div>
                <div style="text-align: right;">
                    <span>$${item.subtotal.toLocaleString()}</span><br>
                    <button onclick="eliminarDelPedido(${index})" style="color:red; cursor:pointer; background:none; border:none; font-size: 0.8rem;">Quitar</button>
                </div>
            </div>
        `;
    });

    totalElemento.innerText = `$${totalGeneral.toLocaleString()}`;
}
