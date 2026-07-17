/*jshint sub:true*/

// --- INICIALIZACIÓN ---
iniciarBaseDatos(function () {
  console.log("Base de datos lista.");
  if (document.getElementById("listaEntrenadores")) {
    renderizarListaEntrenadores();
  }
});

// --- LÓGICA DEL FORMULARIO ---
var btnGuardar = document.getElementById("btnGuardarEntrenador");

if (btnGuardar) {
  btnGuardar.addEventListener("click", function () {
    var nombreEl = document.getElementById("nombreEntrenador");
    var sexoEl = document.getElementById("sexoEntrenador");
    var resEl = document.getElementById("residenciaEntrenador");
    var urlEl = document.getElementById("fotoEntrenador");
    var fileEl = document.getElementById("fotoArchivoEntrenador");

    // 1. Validaciones
    if (!nombreEl.value.trim()) {
      alert("¡Debes ingresar un nombre!");
      return;
    }

    // Si residencia está vacío, ponemos el valor por defecto sugerido
    var residenciaFinal =
      resEl.value.trim() !== "" ? resEl.value.trim() : "Pueblo Paleta";

    // 2. Procesar imagen
    if (fileEl.files && fileEl.files[0]) {
      var reader = new FileReader();
      reader.onload = function (e) {
        ejecutarGuardado(
          nombreEl.value,
          sexoEl.value,
          residenciaFinal,
          e.target.result,
        );
      };
      reader.readAsDataURL(fileEl.files[0]);
    } else if (urlEl.value.trim()) {
      ejecutarGuardado(
        nombreEl.value,
        sexoEl.value,
        residenciaFinal,
        urlEl.value.trim(),
      );
    } else {
      alert("¡Debes ingresar una URL o seleccionar una imagen!");
    }
  });
}

function ejecutarGuardado(nombre, sexo, residencia, foto) {
  var nuevo = {
    nombre: nombre,
    sexo: sexo,
    residencia: residencia,
    foto: foto,
  };

  agregarDato("entrenadores", nuevo, function () {
    alert("Entrenador guardado exitosamente");
    window.location.href = "entrenadores.html";
  });
}

// --- LÓGICA DE LISTADO ---
function renderizarListaEntrenadores() {
  var listaDiv = document.getElementById("listaEntrenadores");
  if (!listaDiv) return;

  obtenerTodos("entrenadores", function (data) {
    listaDiv.innerHTML = "";
    if (data.length === 0) {
      listaDiv.innerHTML =
        "<p class='text-white'>No hay entrenadores registrados aún.</p>";
      return;
    }

    data.forEach(function (entrenador) {
      var card = document.createElement("div");
      card.className = "poke-card col-md-3 m-2";
      card.innerHTML = `
                <img src="${entrenador.foto}" style="width:100%; height:150px; object-fit:cover;">
                <div class="poke-name">${entrenador.nombre}</div>
                <div class="poke-card-info">Residencia: ${entrenador.residencia}</div>
            `;

      card.addEventListener("click", function () {
        abrirModalEntrenador(entrenador);
      });

      listaDiv.appendChild(card);
    });
  });
}

// --- MODAL DE DETALLE ---
function abrirModalEntrenador(entrenador) {
  var overlay = document.getElementById("detailOverlay");
  var banner = document.getElementById("modalBanner");
  if (!overlay) return;

  // Ocultamos el banner repetido
  banner.style.display = "none";

  var body = document.querySelector(".modal-body-inner");

  // Contenido del Modal
  body.innerHTML = `
        <div style="display: flex; gap: 30px; align-items: flex-start; padding: 20px;">
            <img src="${entrenador.foto}" style="width: 250px; height: 250px; object-fit: contain; border: 4px solid var(--border); background: white;">
            <div style="color: white; flex: 1;">
                <h2 style="color: var(--pokemon-yellow); font-size: 2rem;">${entrenador.nombre}</h2>
                <p><strong>ID:</strong> #${entrenador.id}</p>
                <p><strong>Residencia:</strong> ${entrenador.residencia}</p>
            </div>
        </div>
        <hr style="border-top: 2px solid var(--pokemon-blue); margin: 20px 0;">
        <div id="infoEquipo">Cargando equipo...</div>
    `;

  // Buscar equipo en base de datos
  obtenerTodos("equipos", function (equipos) {
    var equipoInfo = document.getElementById("infoEquipo");
    // Ajusta 'entrenadorId' si tu base de datos lo llama diferente
    var equipo = equipos.find(function (e) {
      return e.entrenadorId == entrenador.id;
    });

    if (equipo && equipo.pokemones) {
      equipoInfo.innerHTML = `
                <h4 class="subtitulo-blanco">Equipo: ${equipo.nombre}</h4>
                <div class="d-flex flex-wrap gap-2">
                    ${equipo.pokemones
                      .map(function (p) {
                        // AQUÍ ESTÁ EL CAMBIO:
                        // Si p es un objeto {nombre: 'pikachu'}, usa p.nombre
                        // Si p es solo un string 'pikachu', usa p
                        var nombrePoke =
                          typeof p === "object" ? p.nombre || p.name : p;
                        return `<span class="habilidad-badge">${nombrePoke}</span>`;
                      })
                      .join("")}
                </div>
            `;
    } else {
      equipoInfo.innerHTML = `<p class="subtitulo-blanco">No tiene equipo asociado.</p>`;
    }
  });

  overlay.classList.add("show");
}

// Asegurar que el banner vuelva al cerrar
document.getElementById("closeModal").addEventListener("click", function () {
  document.getElementById("detailOverlay").classList.remove("show");
  document.getElementById("modalBanner").style.display = "block";
});
