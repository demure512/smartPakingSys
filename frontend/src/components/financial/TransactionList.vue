<template>
  <div class="transactions">
    <div class="header">
      <h2>Financial Transactions</h2>
      <div class="filters">
        <div class="date-range">
          <v-date-picker v-model="dateRange" range>
            <template v-slot="{ inputValue, inputEvents }">
              <input
                :value="inputValue"
                v-on="inputEvents"
                placeholder="Select date range"
              />
            </template>
          </v-date-picker>
        </div>
        <v-select
          v-model="selectedPaymentMethod"
          :options="paymentMethods"
          placeholder="Payment Method"
        />
      </div>
    </div>

    <v-data-table
      :headers="headers"
      :items="transactions"
      :loading="loading"
      :options.sync="options"
      :server-items-length="totalTransactions"
      class="transaction-table"
    >
      <template v-slot:item.amount="{ item }">
        {{ formatCurrency(item.amount) }}
      </template>
      <template v-slot:item.timestamp="{ item }">
        {{ formatDate(item.timestamp) }}
      </template>
      <template v-slot:item.status="{ item }">
        <v-chip :color="getStatusColor(item.status)">
          {{ item.status }}
        </v-chip>
      </template>
    </v-data-table>

    <div class="summary-panel">
      <div class="summary-card">
        <h3>Total Revenue</h3>
        <p>{{ formatCurrency(totalRevenue) }}</p>
      </div>
      <div class="summary-card">
        <h3>Today's Revenue</h3>
        <p>{{ formatCurrency(todayRevenue) }}</p>
      </div>
      <div class="summary-card">
        <h3>Transaction Count</h3>
        <p>{{ totalTransactions }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment';

export default {
  name: 'TransactionList',
  
  data() {
    return {
      transactions: [],
      loading: false,
      dateRange: null,
      selectedPaymentMethod: null,
      totalRevenue: 0,
      todayRevenue: 0,
      totalTransactions: 0,
      options: {},
      headers: [
        { text: 'ID', value: 'id' },
        { text: 'Timestamp', value: 'timestamp' },
        { text: 'Amount', value: 'amount' },
        { text: 'Payment Method', value: 'paymentMethod' },
        { text: 'Status', value: 'status' },
        { text: 'Reference', value: 'reference' }
      ],
      paymentMethods: [
        'CASH',
        'CREDIT_CARD',
        'DEBIT_CARD',
        'MOBILE_PAYMENT',
        'ONLINE_TRANSFER'
      ]
    };
  },

  watch: {
    options: {
      handler() {
        this.loadTransactions();
      },
      deep: true
    },
    dateRange() {
      this.loadTransactions();
    },
    selectedPaymentMethod() {
      this.loadTransactions();
    }
  },

  methods: {
    async loadTransactions() {
      this.loading = true;
      try {
        const { page, itemsPerPage, sortBy, sortDesc } = this.options;
        
        const params = {
          page: page - 1,
          size: itemsPerPage,
          sort: sortBy.length ? `${sortBy[0]},${sortDesc[0] ? 'desc' : 'asc'}` : null,
          startDate: this.dateRange?.start,
          endDate: this.dateRange?.end,
          paymentMethod: this.selectedPaymentMethod
        };

        const response = await axios.get('/api/financial/transactions', { params });
        this.transactions = response.data.content;
        this.totalTransactions = response.data.totalElements;
        
        await this.loadSummary();
      } catch (error) {
        console.error('Error loading transactions:', error);
        this.$toast.error('Failed to load transactions');
      } finally {
        this.loading = false;
      }
    },

    async loadSummary() {
      try {
        const params = {
          startDate: this.dateRange?.start || moment().startOf('month'),
          endDate: this.dateRange?.end || moment()
        };

        const [revenueResponse, todayResponse] = await Promise.all([
          axios.get('/api/financial/revenue', { params }),
          axios.get('/api/financial/revenue/daily', {
            params: {
              startDate: moment().startOf('day'),
              endDate: moment().endOf('day')
            }
          })
        ]);

        this.totalRevenue = revenueResponse.data;
        this.todayRevenue = todayResponse.data[moment().format('YYYY-MM-DD')] || 0;
      } catch (error) {
        console.error('Error loading summary:', error);
      }
    },

    formatCurrency(amount) {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
      }).format(amount);
    },

    formatDate(timestamp) {
      return moment(timestamp).format('YYYY-MM-DD HH:mm:ss');
    },

    getStatusColor(status) {
      const colors = {
        COMPLETED: 'success',
        PENDING: 'warning',
        FAILED: 'error',
        REFUNDED: 'info'
      };
      return colors[status] || 'grey';
    }
  },

  mounted() {
    this.loadTransactions();
  }
};
</script>

<style scoped>
.transactions {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 20px;
}

.date-range {
  width: 300px;
}

.transaction-table {
  margin-bottom: 20px;
}

.summary-panel {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.summary-card {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.summary-card h3 {
  margin-bottom: 10px;
  color: #666;
}

.summary-card p {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
}
</style> 