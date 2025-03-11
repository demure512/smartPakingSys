<template>
  <v-snackbar
    v-model="show"
    :color="type"
    :timeout="timeout"
    :position="position"
  >
    {{ message }}
    <template v-slot:action="{ attrs }">
      <v-btn
        text
        v-bind="attrs"
        @click="show = false"
      >
        Close
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
export default {
  name: 'Toast',
  data() {
    return {
      show: false,
      message: '',
      type: 'info',
      timeout: 3000,
      position: 'top'
    };
  },
  created() {
    this.$toast = {
      success: (msg) => this.showToast(msg, 'success'),
      error: (msg) => this.showToast(msg, 'error'),
      info: (msg) => this.showToast(msg, 'info'),
      warning: (msg) => this.showToast(msg, 'warning')
    };
  },
  methods: {
    showToast(message, type) {
      this.message = message;
      this.type = type;
      this.show = true;
    }
  }
};
</script> 