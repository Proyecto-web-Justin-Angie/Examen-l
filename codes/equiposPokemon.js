/* jshint sub:true */
var listaBasePokemon = [];

document.addEventListener("DOMContentLoaded", function () {
  iniciarBaseDatos(function () {
    cargarEntrenadores();
    cargarCachePokeAPI();
    initFormEquipo();
    if (document.getElementById("listaEquipos")) {
      renderEquipos();
    }
  });
});

// --- 1. CARGAR ENTRENADORES Y CACHE API ---
function cargarEntrenadores() {
  var select = document.getElementById("selectEntrenador");
  if (!select) return;
  obtenerTodos("entrenadores", function (entrenadores) {
    select.innerHTML = '<option value="">Seleccione...</option>';
    entrenadores.forEach(function (e) {
      var opt = document.createElement("option");
      opt.value = e.id;
      opt.textContent = e.nombre;
      select.appendChild(opt);
    });
  });
}

function cargarCachePokeAPI() {
  fetch("https://pokeapi.co/api/v2/pokemon?limit=151")
    .then((res) => res.json())
    .then((data) => {
      listaBasePokemon = data.results.map((p, i) => ({
        nombre: p.name,
        id: (i + 1).toString(),
        img: `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${i + 1}.png`,
      }));
      activarBusquedaInputs();
    });
}

// --- 2. RENDERIZADO DE EQUIPOS Y DETALLE ---
function renderEquipos() {
  var listaDiv = document.getElementById("listaEquipos");
  if (!listaDiv) return;

  obtenerTodos("equipos", function (equipos) {
    listaDiv.innerHTML = "";
    equipos.forEach(function (equipo) {
      var col = document.createElement("div");
      col.className = "col-12 col-md-6 col-lg-4 mb-4";
      var equipoJson = JSON.stringify(equipo).replace(/"/g, "&quot;");

      col.innerHTML = `
                <div class="poke-card h-100" onclick="abrirModalEquipo(${equipoJson})">
                    <img src="${equipo.imagen}" class="equipo-img" onerror="this.src='default.png'">
                    <div class="nombre-equipo">${equipo.nombre}</div>
                    <div class="id-entrenador-blanco">ID: ${equipo.entrenadorId}</div>
                    
                    <div class="d-flex justify-content-center flex-wrap mt-2">
                        ${equipo.pokemones
                          .map(
                            (p) => `
                            <span class="movimiento-badge">${p.toUpperCase()}</span>
                        `,
                          )
                          .join("")}
                    </div>
                </div>`;
      listaDiv.appendChild(col);
    });
  });
}

function abrirModalEquipo(equipo) {
  var modalElement = document.getElementById("modalDetalle");
  var modal = new bootstrap.Modal(modalElement);

  obtenerPorId("entrenadores", equipo.entrenadorId, function (entrenador) {
    document.getElementById("detNombre").textContent = equipo.nombre;
    document.getElementById("detNombre").className = "nombre-equipo";
    document.getElementById("detEntrenador").innerHTML =
      `Entrenador: ${entrenador ? entrenador.nombre : "Desconocido"}`;
    document.getElementById("detImagen").src = equipo.imagen;

    var contenedorPokes = document.getElementById("detPokemones");
    contenedorPokes.className = "modal-poke-grid";
    contenedorPokes.innerHTML = "Cargando...";

    var promesas = equipo.pokemones.map((nombre) =>
      fetch(`https://pokeapi.co/api/v2/pokemon/${nombre.toLowerCase()}`).then(
        (r) => r.json(),
      ),
    );

    Promise.all(promesas).then((pokesData) => {
      contenedorPokes.innerHTML = "";
      pokesData.forEach((p) => {
        var tiposHtml = p.types
          .map(
            (t) =>
              `<span class="badge type-${t.type.name}">${t.type.name}</span>`,
          )
          .join("");

        var divPoke = document.createElement("div");
        divPoke.className = "modal-poke-item";
        divPoke.innerHTML = `
        <img src="${p.sprites.front_default}" width="50">
        <div>
            <div class="movimiento-badge" style="margin-bottom: 5px;">${p.name.toUpperCase()}</div>
            <div style="font-size: 9px;">ID: #${p.id}</div>
            <div>${tiposHtml}</div>
        </div>`;
        contenedorPokes.appendChild(divPoke);
      });
    });
  });
  modal.show();
}

// --- 3. GUARDAR EQUIPO ---
function initFormEquipo() {
  var btn = document.getElementById("btnGuardarEquipo");
  if (!btn) return;
  btn.addEventListener("click", function () {
    var pokes = [];
    for (var i = 1; i <= 6; i++) {
      var input = document.getElementById("poke" + i);
      if (input && input.value) pokes.push(input.value);
    }
    var data = {
      nombre: document.getElementById("nombreEquipo").value,
      entrenadorId: document.getElementById("selectEntrenador").value,
      imagen: document.getElementById("fotoEquipo").value,
      pokemones: pokes,
    };
    if (!data.nombre || !data.entrenadorId || pokes.length === 0)
      return alert("Completa los campos.");
    agregarDato("equipos", data, function () {
      window.location.href = "equiposPokemon.html";
    });
  });
}

// --- BUSCADOR PREDICTIVO ---
function activarBusquedaInputs() {
  for (let i = 1; i <= 6; i++) {
    let input = document.getElementById("poke" + i);
    if (!input) continue;

    // Crear contenedor de sugerencias si no existe
    let container = document.createElement("div");
    container.className = "sugerencias-box";
    input.parentNode.style.position = "relative";
    input.parentNode.appendChild(container);

    input.addEventListener("input", function () {
      let val = this.value.toLowerCase();
      container.innerHTML = "";

      if (val.length < 1) return;

      // Filtrar por nombre o ID
      let resultados = listaBasePokemon
        .filter((p) => p.nombre.includes(val) || p.id === val)
        .slice(0, 5); // Limitar a 5 resultados

      resultados.forEach((p) => {
        let div = document.createElement("div");
        div.className = "sugerencia-item";
        div.innerHTML = `
                    <img src="${p.img}" width="32">
                    <span>#${p.id} ${p.nombre.toUpperCase()}</span>
                `;
        div.onclick = () => {
          input.value = p.nombre; // O p.id si prefieres
          container.innerHTML = "";
        };
        container.appendChild(div);
      });
    });

    // Ocultar al hacer clic fuera
    document.addEventListener("click", (e) => {
      if (e.target !== input) container.innerHTML = "";
    });
  }
}
