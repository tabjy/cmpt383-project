const nums = process.argv[3].split(', ').map(parseFloat)

console.log(require('./solution')(nums))