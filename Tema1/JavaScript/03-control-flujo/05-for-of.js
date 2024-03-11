// para acceder a los elementos de un array, USAR FOR-OF, NO FOR-IN
let animales = ['Chanchito Feliz', 'Cabra', 'Perra'];

for (const animal of animales) {
    console.log(animal);
}

let i = 0;
while (i < animales.length) {
    console.log(animales[i]);
    i++;
}