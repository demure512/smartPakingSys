import * as Sentry from '@sentry/vue';
import { BrowserTracing } from '@sentry/tracing';
import config from '@/config';

export default {
  install(Vue) {
    if (process.env.NODE_ENV === 'production') {
      Sentry.init({
        Vue,
        dsn: config.sentry.dsn,
        integrations: [new BrowserTracing()],
        tracesSampleRate: 1.0,
        environment: config.sentry.environment,
        beforeSend(event) {
          if (event.exception) {
            Sentry.showReportDialog({ eventId: event.event_id });
          }
          return event;
        }
      });
    }
  }
}; 