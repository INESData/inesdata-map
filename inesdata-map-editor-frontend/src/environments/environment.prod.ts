import { NgxLoggerLevel } from 'ngx-logger';
import { Environment } from 'src/app/shared/models/environment.model';

import packageJson from '../../package.json';

export const environment: Environment = {
	production: true,
	info: {
		version: packageJson.version,
		name: packageJson.name,
		app: packageJson.project
	},
	api: {
		endpoints: {}
	},
	messages: {
		life: 5000
	},
	tables: {
		pagination: {
			available: [10, 50, 100]
		}
	},
	runtime: {
		api: {
			baseUrl: ''
		}
	},
	logging: {
		disableWindowConsoleLogging: true,
		disableNgxLogging: false,
		apiUrl: '',
		logLevel: NgxLoggerLevel.ERROR,
		serverLogLevel: NgxLoggerLevel.ERROR,
		enableSourceMaps: false
	}
};
