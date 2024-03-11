// usar para saber el tipo de nuestros objetos
let user = {
    id: 1,
    name: 'Cabra Voladora',
    edad: 69,
};

console.log('--------------');
for(const prop in user) {
    console.log(prop,':',user[prop]);
}