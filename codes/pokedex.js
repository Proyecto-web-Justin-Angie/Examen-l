/* ── Generaciones ─────────────────────────────────────── */
var GENERATIONS = {
  "0,151": { num: "I", label: "Kanto" },
  "151,100": { num: "II", label: "Johto" },
  "251,135": { num: "III", label: "Hoenn" },
  "386,107": { num: "IV", label: "Sinnoh" },
  "493,156": { num: "V", label: "Unova" },
};

var currentGenKey = "0,151";
var currentGenNum = "I";
var pokemonList = [];

/* ── Referencias del DOM ──────────────────────────────── */
var grid = document.getElementById("pokegrid");
var spinner = document.getElementById("loadingSpinner");
var overlay = document.getElementById("detailOverlay");
var genSel = document.getElementById("genSelect");

/* ── Función de consumo API con XMLHttpRequest ────────── */
// Obligatorio según las instrucciones del examen
function xhrGet(url, onSuccess, onError) {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", url, true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        try {
          onSuccess(JSON.parse(xhr.responseText));
        } catch (e) {
          if (onError) onError(e);
        }
      } else {
        if (onError) onError(xhr.status);
      }
    }
  };
  xhr.send();
}

/* ── Helpers para URLs de Imágenes ────────────────────── */
// Utilizando los enlaces sugeridos en el documento
function imgByName(name) {
  return (
    "https://img.pokemondb.net/sprites/omega-ruby-alpha-sapphire/dex/normal/" +
    name +
    ".png"
  );
}

function imgByNumber(n) {
  var paddedNum = String(n).padStart(3, "0");
  return (
    "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/full/" +
    paddedNum +
    ".png"
  );
}

/* ── Tarjetas de carga ─────────────────────── */
function showSkeletons(count) {
  grid.innerHTML = "";
  for (var i = 0; i < count; i++) {
    var sk = document.createElement("div");
    sk.className = "skeleton";
    sk.innerHTML =
      '<div class="skeleton-img"></div><div class="skeleton-txt"></div>';
    grid.appendChild(sk);
  }
}

/* ── Cargar Generación ────────────────────────────────── */
function loadGeneration(key) {
  var parts = key.split(",");
  var offset = parts[0];
  var limit = parts[1];
  var info = GENERATIONS[key];
  currentGenNum = info.num;

  showSkeletons(parseInt(limit));
  spinner.style.display = "none";

  var url =
    "https://pokeapi.co/api/v2/pokemon?offset=" + offset + "&limit=" + limit;

  xhrGet(
    url,
    function (data) {
      pokemonList = data.results;
      renderGrid(pokemonList, offset);
    },
    function (err) {
      grid.innerHTML =
        '<p style="color:var(--accent);padding:40px;text-align:center;">Error cargando datos. Intente de nuevo.</p>';
      console.error("Error XHR:", err);
    },
  );
}

/* ── Renderizar Cuadrícula ────────────────────────────── */
function renderGrid(list, offset) {
  grid.innerHTML = "";
  var startNum = parseInt(offset) + 1;

  for (var i = 0; i < list.length; i++) {
    (function (pokemon, num) {
      var card = document.createElement("div");
      card.className = "poke-card"; // Aplica el box-shadow y hover del CSS
      card.setAttribute("data-name", pokemon.name);
      card.setAttribute("data-num", num);
      card.setAttribute("tabindex", "0");
      card.setAttribute("role", "button");
      card.setAttribute("aria-label", "Ver detalles de " + pokemon.name);

      var padded = String(num).padStart(3, "0");

      // Intentamos cargar primero por número (pokemon.com), si falla usamos nombre (pokemondb)
      card.innerHTML =
        '<div class="poke-num">#' +
        padded +
        "</div>" +
        '<img src="' +
        imgByNumber(num) +
        '" alt="' +
        pokemon.name +
        '" ' +
        "onerror=\"this.src='" +
        imgByName(pokemon.name) +
        "'; this.onerror=null;\" />" +
        '<div class="poke-name">' +
        pokemon.name +
        "</div>";

      card.addEventListener("click", function () {
        openDetail(pokemon.name, num);
      });
      card.addEventListener("keydown", function (e) {
        if (e.key === "Enter" || e.key === " ") openDetail(pokemon.name, num);
      });

      grid.appendChild(card);
    })(list[i], startNum + i);
  }
}

/* ── Abrir Modal de Detalles ──────────────────────────── */
function openDetail(name, num) {
  /* Reiniciar valores previos */
  document.getElementById("modalName").textContent = name;
  document.getElementById("modalId").textContent =
    "Pokémon ID #" + String(num).padStart(3, "0");
  document.getElementById("modalGen").textContent = currentGenNum;
  document.getElementById("modalWeight").textContent = "…";
  document.getElementById("modalHeight").textContent = "…";
  document.getElementById("modalTypes").innerHTML = "";
  document.getElementById("modalAbilities").innerHTML = "";
  document.getElementById("modalMoves").innerHTML = "";

  var img = document.getElementById("modalImg");
  img.src = imgByName(name);
  img.onerror = function () {
    this.src = imgByNumber(num);
    this.onerror = null;
  };

  overlay.classList.add("show");
  document.body.style.overflow = "hidden";

  /* Obtener detalles específicos del Pokémon */
  var url = "https://pokeapi.co/api/v2/pokemon/" + name;
  xhrGet(
    url,
    function (data) {
      var weightKg = (data.weight / 10).toFixed(1) + " kgs";
      var heightM = (data.height / 10).toFixed(1) + " mts";

      document.getElementById("modalWeight").textContent = weightKg;
      document.getElementById("modalHeight").textContent = heightM;

      /* Tipos */
      var typesDiv = document.getElementById("modalTypes");
      typesDiv.innerHTML = "";
      data.types.forEach(function (t) {
        var span = document.createElement("span");
        span.className = "badge-type type-" + t.type.name; // Mapea con las variables de colores en CSS
        span.textContent = t.type.name;
        typesDiv.appendChild(span);
      });

      /* Habilidades */
      var abDiv = document.getElementById("modalAbilities");
      abDiv.innerHTML = "";
      data.abilities.forEach(function (a) {
        var span = document.createElement("span");
        // CAMBIO: Usamos 'habilidad-badge' para que tome el estilo de botón
        span.className = "habilidad-badge";
        span.textContent = a.ability.name.replace(/-/g, " ");
        abDiv.appendChild(span);
      });

      /* Movimientos (limitado a 10) */
      var movesDiv = document.getElementById("modalMoves");
      movesDiv.innerHTML = "";
      var moves = data.moves.slice(0, 10);
      moves.forEach(function (m) {
        var span = document.createElement("span");
        span.className = "movimiento-badge";
        span.textContent = m.move.name.replace(/-/g, " ");
        movesDiv.appendChild(span);
      });

      /* Ajustar color del banner según el primer tipo */
      var firstType = data.types[0].type.name;
      var banner = document.getElementById("modalBanner");
      banner.style.background = "var(--" + firstType + ", var(--bg-header))";
    },
    function (err) {
      console.error("Detail error:", err);
    },
  );
}

/* ── Cerrar Modal ─────────────────────────────────────── */
function closeModal() {
  overlay.classList.remove("show");
  document.body.style.overflow = "";
}

document.getElementById("closeModal").addEventListener("click", closeModal);
overlay.addEventListener("click", function (e) {
  if (e.target === overlay) closeModal();
});
document.addEventListener("keydown", function (e) {
  if (e.key === "Escape") closeModal();
});

/* ── Selector de Generación ───────────────────────────── */
if (genSel) {
  genSel.addEventListener("change", function () {
    currentGenKey = this.value;
    loadGeneration(currentGenKey);
  });
}

/* ── Inicialización ───────────────────────────────────── */
loadGeneration(currentGenKey);
