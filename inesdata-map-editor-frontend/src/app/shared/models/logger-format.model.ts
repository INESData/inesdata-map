/**
 * Logger format model
 */
export interface LoggerFormatModel {
	timestamp: string;
	msVersion: string;
	msName: string;
	level: string;
	levelValue: number;
	appId: string;
	class: string;
	package: string;
	line: string;
	request: RequestLoggerFormatModel;
	message: string;
}

/**
 * Request Logger format model
 */
export interface RequestLoggerFormatModel {
	requestUri: string;
	requestUrl: string;
	method: string;
	remoteHost: string;
	userAgent: string;
}
