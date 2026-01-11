// Simulación de base de datos de productos
const productos = [
    {
        id: 1,
        nombre: "Café Laguna Especial",
        imagen: "../assets/cafe.jpg",
        opciones: [
            { etiqueta: "250g", precio: 15000 },
            { etiqueta: "500g", precio: 28000 },
            { etiqueta: "1000g", precio: 52000 }
        ]
    },
    {
        id: 2,
        nombre: "Miel de Abeja Pura",
        imagen: "../assets/miel.jpg",
        opciones: [
            { etiqueta: "Pequeña", precio: 12000 },
            { etiqueta: "Grande", precio: 22000 }
        ]
    }
];
const productos = [
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

let carrito = []; // Aquí guardaremos los pedidos del cliente