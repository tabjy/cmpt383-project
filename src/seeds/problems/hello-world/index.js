const {markdown} = require('markdown')
const {readFile} = require('fs').promises

module.exports = async () => ({
    _id: {
        $oid: "5fd2a2f10dfb1770642e13d4"
    },
    title: 'Hello, world',
    difficulty: 'easy',
    description: markdown.toHTML(await readFile(__dirname + '/description.md', 'utf-8')),
    testCases: [
        {
            in: '',
            out: 'Hello, world!',
            hidden: false
        }
    ],
    templates: [
        {
            language: 'c',
            files: [
                {
                    path: 'main.c',
                    content: await readFile(__dirname + '/c/main.c', 'utf-8'),
                    editable: false
                },
                {
                    path: 'solution.c',
                    content: await readFile(__dirname + '/c/solution.c', 'utf-8'),
                    editable: true
                }
            ]
        },
        {
            language: 'cpp',
            files: [
                {
                    path: 'main.cpp',
                    content: await readFile(__dirname + '/cpp/main.cpp', 'utf-8'),
                    editable: false
                },
                {
                    path: 'solution.cpp',
                    content: await readFile(__dirname + '/cpp/solution.cpp', 'utf-8'),
                    editable: true
                }
            ]
        },
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