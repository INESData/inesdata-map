import { NgxLoggerLevel } from 'ngx-logger';

/**
 * Environment model
 */
export interface Environment {
	production: boolean;
	info: Info;
	api: ApiEndpoints;
	messages: Messages;
	tables: Tables;
	runtime: Runtime;
	logging: Logging;
}

interface Logging {
	disableWindowConsoleLogging: boolean;
	disableNgxLogging: boolean;
	apiUrl: string;
	logLevel: NgxLoggerLevel;
	serverLogLevel: NgxLoggerLevel;
	enableSourceMaps: boolean;
}

interface Runtime {
	api: Api;
}

interface Api {
	baseUrl: string;
}

interface Tables {
	pagination: Pagination;
}

interface Pagination {
	available: number[];
}

interface Messages {
	life: number;
}

interface ApiEndpoints {
	endpoints: Record<string, string>;
}

interface Info {
	version: string;
	name: string;
	app: string;
}
