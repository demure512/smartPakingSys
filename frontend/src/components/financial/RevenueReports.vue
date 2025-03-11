<template>
  <div class="revenue-reports">
    <div class="header">
      <h2>Revenue Reports</h2>
      <div class="controls">
        <v-select
          v-model="selectedReport"
          :options="reportTypes"
          placeholder="Select Report Type"
        />
        <v-btn
          color="primary"
          @click="generateReport"
          :loading="generating"
        >
          Generate Report
        </v-btn>
      </div>
    </div>

    <div class="report-filters">
      <div class="date-filters" v-if="showDateFilters">
        <v-date-picker
          v-model="dateRange"
          range
          :max-date="new Date()"
        >
          <template v-slot="{ inputValue, inputEvents }">
            <input
              :value="inputValue"
              v-on="inputEvents"
              placeholder="Select date range"
            />
          </template>
        </v-date-picker>
      </div>

      <div class="additional-filters">
        <v-select
          v-if="showParkingLotFilter"
          v-model="selectedParkingLot"
          :options="parkingLots"
          placeholder="Select Parking Lot"
        />
        <v-select
          v-if="showPaymentMethodFilter"
          v-model="selectedPaymentMethod"
          :options="paymentMethods"
          placeholder="Select Payment Method"
        />
      </div>

      <div class="format-selector">
        <v-select
          v-model="selectedFormat"
          :options="exportFormats"
          placeholder="Select Format"
        />
      </div>
    </div>

    <div class="report-preview" v-if="reportData">
      <div class="chart-container" v-if="showChart">
        <line-chart
          v-if="selectedReport === 'DAILY_REVENUE'"
          :chart-data="chartData"
          :options="chartOptions"
        />
        <bar-chart
          v-else-if="selectedReport === 'MONTHLY_REVENUE'"
          :chart-data="chartData"
          :options="chartOptions"
        />
        <pie-chart
          v-else-if="selectedReport === 'PAYMENT_METHOD_DISTRIBUTION'"
          :chart-data="chartData"
          :options="chartOptions"
        />
      </div>

      <div class="summary-stats">
        <div class="stat-card" v-for="stat in summaryStats" :key="stat.label">
          <h4>{{ stat.label }}</h4>
          <p>{{ stat.value }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Line, Bar, Pie } from 'vue-chartjs';
import moment from 'moment';

export default {
  name: 'RevenueReports',
  
  components: {
    LineChart: Line,
    BarChart: Bar,
    PieChart: Pie
  },

  data() {
    return {
      selectedReport: null,
      dateRange: null,
      selectedParkingLot: null,
      selectedPaymentMethod: null,
      selectedFormat: 'PDF',
      generating: false,
      reportData: null,
      
      reportTypes: [
        { value: 'DAILY_REVENUE', label: 'Daily Revenue' },
        { value: 'MONTHLY_REVENUE', label: 'Monthly Revenue' },
        { value: 'PAYMENT_METHOD_DISTRIBUTION', label: 'Payment Method Distribution' },
        { value: 'PARKING_LOT_REVENUE', label: 'Parking Lot Revenue' }
      ],
      
      exportFormats: [
        { value: 'PDF', label: 'PDF' },
        { value: 'EXCEL', label: 'Excel' },
        { value: 'CSV', label: 'CSV' }
      ],
      
      parkingLots: [],
      paymentMethods: [
        'CASH',
        'CREDIT_CARD',
        'DEBIT_CARD',
        'MOBILE_PAYMENT',
        'ONLINE_TRANSFER'
      ],
      
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      }
    };
  },

  computed: {
    showDateFilters() {
      return ['DAILY_REVENUE', 'MONTHLY_REVENUE', 'PARKING_LOT_REVENUE'].includes(this.selectedReport);
    },

    showParkingLotFilter() {
      return this.selectedReport === 'PARKING_LOT_REVENUE';
    },

    showPaymentMethodFilter() {
      return this.selectedReport === 'PAYMENT_METHOD_DISTRIBUTION';
    },

    showChart() {
      return this.reportData && this.reportData.chartData;
    },

    chartData() {
      if (!this.reportData?.chartData) return null;

      const { labels, datasets } = this.reportData.chartData;
      return {
        labels,
        datasets: datasets.map(dataset => ({
          ...dataset,
          borderColor: '#2c3e50',
          backgroundColor: this.selectedReport === 'PAYMENT_METHOD_DISTRIBUTION'
            ? this.generateColors(datasets.length)
            : 'rgba(44, 62, 80, 0.2)'
        }))
      };
    },

    summaryStats() {
      if (!this.reportData?.summary) return [];
      
      return Object.entries(this.reportData.summary).map(([label, value]) => ({
        label: this.formatLabel(label),
        value: this.formatValue(value)
      }));
    }
  },

  methods: {
    async loadParkingLots() {
      try {
        const response = await axios.get('/api/parking-lots');
        this.parkingLots = response.data.map(lot => ({
          value: lot.id,
          label: lot.name
        }));
      } catch (error) {
        console.error('Error loading parking lots:', error);
        this.$toast.error('Failed to load parking lots');
      }
    },

    async generateReport() {
      if (!this.validateFilters()) return;
      
      this.generating = true;
      try {
        const params = {
          reportType: this.selectedReport,
          format: this.selectedFormat,
          startDate: this.dateRange?.start,
          endDate: this.dateRange?.end,
          parkingLotId: this.selectedParkingLot,
          paymentMethod: this.selectedPaymentMethod
        };

        const response = await axios.get('/api/reports/revenue', { params });
        
        if (this.selectedFormat === 'PDF') {
          // Handle PDF download
          const blob = new Blob([response.data], { type: 'application/pdf' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `revenue_report_${moment().format('YYYY-MM-DD')}.pdf`;
          link.click();
        } else {
          // Handle data for chart display
          this.reportData = response.data;
        }
      } catch (error) {
        console.error('Error generating report:', error);
        this.$toast.error('Failed to generate report');
      } finally {
        this.generating = false;
      }
    },

    validateFilters() {
      if (this.showDateFilters && !this.dateRange) {
        this.$toast.error('Please select a date range');
        return false;
      }
      if (this.showParkingLotFilter && !this.selectedParkingLot) {
        this.$toast.error('Please select a parking lot');
        return false;
      }
      if (this.showPaymentMethodFilter && !this.selectedPaymentMethod) {
        this.$toast.error('Please select a payment method');
        return false;
      }
      return true;
    },

    generateColors(count) {
      const colors = [];
      for (let i = 0; i < count; i++) {
        colors.push(`hsl(${(i * 360) / count}, 70%, 50%)`);
      }
      return colors;
    },

    formatLabel(label) {
      return label.split('_').map(word => 
        word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
      ).join(' ');
    },

    formatValue(value) {
      if (typeof value === 'number') {
        return new Intl.NumberFormat('en-US', {
          style: 'currency',
          currency: 'USD'
        }).format(value);
      }
      return value;
    }
  },

  mounted() {
    this.loadParkingLots();
  }
};
</script>

<style scoped>
.revenue-reports {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.controls {
  display: flex;
  gap: 20px;
}

.report-filters {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}

.chart-container {
  height: 400px;
  margin-bottom: 30px;
}

.summary-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.stat-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.stat-card h4 {
  color: #666;
  margin-bottom: 10px;
}

.stat-card p {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
}
</style> 