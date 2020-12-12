const nums = process.argv[3].split(', ').map(parseFloat)
const target = parseFloat(process.argv[4])

console.log(require('./solution')(nums, target).sort().join(', '))