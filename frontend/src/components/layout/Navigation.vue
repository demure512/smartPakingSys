<template>
  <v-navigation-drawer
    v-model="drawer"
    app
    clipped
  >
    <v-list>
      <template v-for="(item, index) in menuItems">
        <v-list-group
          v-if="item.children"
          :key="index"
          :prepend-icon="item.icon"
          :value="isGroupActive(item)"
        >
          <template v-slot:activator>
            <v-list-item-content>
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </template>

          <v-list-item
            v-for="(child, childIndex) in item.children"
            :key="childIndex"
            :to="child.path"
            :disabled="child.disabled"
          >
            <v-list-item-content>
              <v-list-item-title>{{ child.title }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>

        <v-list-item
          v-else
          :key="index"
          :to="item.path"
          :disabled="item.disabled"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'Navigation',
  props: {
    value: Boolean
  },
  data() {
    return {
      menuItems: [
        {
          title: 'Dashboard',
          icon: 'mdi-view-dashboard',
          path: '/dashboard'
        },
        {
          title: 'Parking',
          icon: 'mdi-car',
          children: [
            { title: 'Parking Lots', path: '/parking/lots' },
            { title: 'Vehicles', path: '/parking/vehicles' }
          ]
        },
        {
          title: 'Financial',
          icon: 'mdi-currency-usd',
          children: [
            { title: 'Transactions', path: '/financial/transactions' },
            { title: 'Reports', path: '/financial/reports' }
          ]
        },
        {
          title: 'System',
          icon: 'mdi-cog',
          children: [
            { title: 'Logs', path: '/system/logs' },
            { title: 'Settings', path: '/system/settings' }
          ]
        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      hasRole: 'auth/hasRole'
    }),
    drawer: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit('input', value);
      }
    }
  },
  methods: {
    isGroupActive(item) {
      if (!item.children) return false;
      return item.children.some(child => 
        this.$route.path.startsWith(child.path)
      );
    }
  }
};
</script> 