const {markdown} = require('markdown')
const {readFile} = require('fs').promises

module.exports = async () => ({
    title: 'Hello, world',
    difficulty: 'easy',
    description: markdown.toHTML(await readFile(__dirname + '/description.md', 'utf-8')),
    testCases: [
        {
            in: '',
            out: 'Hello, world!'
        }
    ],
    templates: [
        {
            language: 'javascript',
            files: [
                {
                    path: 'main.js',
                    content: await readFile(__dirname + '/javascript/main.js', 'utf-8'),
                    editable: false
                },
                {
                    path: 'solution.js',
                    content: await readFile(__dirname + '/javascript/solution.js', 'utf-8'),
                    editable: true
                }
            ]
        }
    ]
})