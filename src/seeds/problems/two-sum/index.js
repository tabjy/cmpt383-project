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
            hidden: false
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
            language: 'java',
            files: [
                {
                    path: 'Main.java',
                    content: await readFile(__dirname + '/java/Main.java', 'utf-8'),
                    editable: false
                },
                {
                    path: 'Solution.java',
                    content: await readFile(__dirname + '/java/Solution.java', 'utf-8'),
                    editable: true
                }
            ]
        },
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
        },
        {
            language: 'python',
            files: [
                {
                    path: 'main.py',
                    content: await readFile(__dirname + '/python/main.py', 'utf-8'),
                    editable: false
                },
                {
                    path: 'solution.py',
                    content: await readFile(__dirname + '/python/solution.py', 'utf-8'),
                    editable: true
                }
            ]
        }
    ]
})