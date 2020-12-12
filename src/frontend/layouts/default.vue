<template>
  <v-app dark>
    <v-navigation-drawer
      :mini-variant="!drawer"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title v-text="item.title" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar
      fixed
      app
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title v-text="title" />
    </v-app-bar>
    <v-main>
      <nuxt />
    </v-main>
  </v-app>
</template>

<script>
export default {
  data () {
    return {
      drawer: false,
      items: [
        {
          icon: 'mdi-chart-bubble',
          title: 'Welcome',
          to: '/'
        },
        {
          icon: 'mdi-format-list-bulleted',
          title: 'Problems',
          to: '/problems'
        },
        {
          icon: 'mdi-code-braces',
          title: 'Editor',
          to: '/editor?problemId=5fd2a2f10dfb1770642e13d4'
        }
      ],
      right: true,
      rightDrawer: false,
      title: 'Cinlc: Cinlc Is Not Leet Code'
    }
  }
}
</script>
