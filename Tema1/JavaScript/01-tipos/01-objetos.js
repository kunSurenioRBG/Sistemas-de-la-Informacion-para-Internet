//prueba personaje TV
let nombre = "Tanjiro";
let anime = "Demos Slayer";
let edad = 16;

// creamos objeto nuevo
let personaje = {
    nombre: "Tanjiro", // par clave-valor
    anime: "Demon Slayer",
    edad: "16",
};

console.log(personaje);
console.log(personaje.nombre);
console.log(personaje['anime']);

personaje.edad = 13;

delete personaje.anime;
console.log(personaje);