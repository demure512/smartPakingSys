export default {
  data() {
    return {
      rules: {
        required: v => !!v || 'This field is required',
        email: v => /.+@.+\..+/.test(v) || 'Invalid email address',
        phone: v => /^\+?[\d\s-]{10,}$/.test(v) || 'Invalid phone number',
        password: v => v?.length >= 8 || 'Password must be at least 8 characters',
        numeric: v => !isNaN(v) || 'Must be a number',
        min: min => v => !v || v >= min || `Must be greater than ${min}`,
        max: max => v => !v || v <= max || `Must be less than ${max}`,
        length: len => v => !v || v.length === len || `Must be exactly ${len} characters`
      }
    };
  },
  methods: {
    validate() {
      return this.$refs.form.validate();
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    }
  }
}; 