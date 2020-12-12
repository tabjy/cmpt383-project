const {markdown} = require('markdown')
const {readFile} = require('fs').promises

module.exports = async () => ({
    _id: {
        $oid: "5fd2b81415299f79d743e90f"
    },
    title: 'Split Array With Same Average',
    difficulty: 'hard',
    description: markdown.toHTML(await readFile(__dirname + '/description.md', 'utf-8')),
    testCases: [
        {
            in: ['8', '1, 2, 3, 4, 5, 6, 7, 8'],
            out: 'true',
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