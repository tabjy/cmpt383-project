const {writeFile, mkdir} = require('fs').promises

module.exports = async () => {
    await mkdir('./out', {recursive: true})
    await writeFile('./out/problems.json', JSON.stringify([
        await require('./hello-world')()
    ]))
}
