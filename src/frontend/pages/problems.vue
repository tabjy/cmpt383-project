<template>
  <v-container>
    <v-row>
      <v-col class="text-center">
        <div>
          <v-data-table
            :headers="headers"
            :items="problems"
            item-key="name"
            class="elevation-1"
            :search="search"
            @click:row="open"
          >
            <template v-slot:top>
              <v-text-field
                v-model="search"
                label="Search"
                class="mx-4"
              />
            </template>
          </v-data-table>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style>
  tr {
    cursor: pointer;
  }
</style>

<script>
const baseUrl = document.location.origin
// const baseUrl = 'http://0.0.0.0:8080'

export default {
  data () {
    return {
      search: '',
      calories: '',
      problems: []
    }
  },
  computed: {
    headers () {
      return [
        {
          text: 'Title',
          align: 'start',
          value: 'title'
        },
        {
          text: 'Difficulty',
          value: 'difficulty'
        },
        {
          text: '# of Test Cases',
          value: 'testCases'
        },
        {
          text: 'Languages Supported',
          sortable: false,
          value: 'languages'
        }
      ]
    }
  },
  mounted () {
    this.$axios.get(`${baseUrl}/api/problems`).then((resp) => {
      this.problems = resp.data.map(problem => {
        problem.difficulty = problem.difficulty?.toUpperCase()
        problem.testCases = problem.testCases.length
        problem.languages = problem.templates.map(t => t.language).join(', ')

        return problem
      })
    }).catch((err) => {
      alert(err.response ? err.response.data.details : 'Cannot communicate with the server!')
    })
  },
  methods: {
    open (value) {
      window.open(`/editor?problemId=${encodeURIComponent(value.id)}`, '_self')
    }
  },
  head () {
    return {
      title: 'Problems'
    }
  }
}
</script>
