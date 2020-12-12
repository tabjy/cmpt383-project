const {markdown} = require('markdown')
const {readFile} = require('fs').promises

module.exports = async () => ({
    _id: {
        $oid: "5fd40dde970bbd8a64bf71d9"
    },
    title: 'Two Sum',
    difficulty: 'medium',
    description: markdown.toHTML(await readFile(__dirname + '/description.md', 'utf-8')),
    testCases: [
        {
            in: ['4', '2, 7, 11, 15', '9'],
            out: '0, 1',
            hidden: true
        },
        {
            in: ['3', '3, 2, 4', '6'],
            out: '1, 2',
            hidden: false
        },
        {
            in: ['2', '3, 3', '6'],
            out: '0, 1',
            hidden: false
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