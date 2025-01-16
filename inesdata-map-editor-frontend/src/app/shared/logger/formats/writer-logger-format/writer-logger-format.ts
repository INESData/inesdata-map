import { INGXLoggerMetadata } from 'ngx-logger';

import { LoggerFormatModel } from '../../../models/logger-format.model';
import { LoggerFormat } from '../logger-format';
import { WriterRequestLoggerFormat } from './writer-request-logger-format';

/**
 * Writer logger format
 */
export class WriterLoggerFormat {
	private static OPEN_BRACKET = '[';
	private static CLOSE_BRACKET = ']';
	private static EMPTY = '';

	/**
	 * Get the format with the basic standard metadata to display in console
	 *
	 * @param metadata aditional information of logger interceptor
	 * @returns string with standart format fulfilled
	 */
	public static getFormat(metadata: INGXLoggerMetadata): string {
		const loggerFormatModel: LoggerFormatModel = LoggerFormat.getFormatModel(metadata);
		return (
			this.getTimeStamp(loggerFormatModel.timestamp) +
			this.getMsVersion(loggerFormatModel.msVersion) +
			this.getMsName(loggerFormatModel.msName) +
			this.getLevel(loggerFormatModel.level) +
			this.getLevelValue(loggerFormatModel.levelValue) +
			this.getAppId(loggerFormatModel.appId) +
			this.getPackage(loggerFormatModel.package) +
			this.getClass(loggerFormatModel.class) +
			this.getLine(loggerFormatModel.line) +
			WriterRequestLoggerFormat.getRequestFormat(loggerFormatModel.request)
		);
	}

	/**
	 * Get message with log timestamp
	 *
	 * @param timestamp Log timestamp
	 * @returns Log message
	 */
	private static getTimeStamp(timestamp: string): string {
		return this.OPEN_BRACKET + '@timestamp: ' + timestamp + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log ms version
	 *
	 * @param version Log ms version
	 * @returns Log message
	 */
	private static getMsVersion(version: string): string {
		return this.OPEN_BRACKET + 'ms_version: ' + version + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log ms name
	 *
	 * @param name Log ms name
	 * @returns Log message
	 */
	private static getMsName(name: string): string {
		return this.OPEN_BRACKET + 'ms_name: ' + name + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log level
	 *
	 * @param level Log level
	 * @returns Log message
	 */
	private static getLevel(level: string): string {
		return this.OPEN_BRACKET + 'level: ' + level + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log level value
	 *
	 * @param levelValue Log level value
	 * @returns Log message
	 */
	private static getLevelValue(levelValue: number): string {
		return this.OPEN_BRACKET + 'level_value: ' + levelValue + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log app id
	 *
	 * @param appId Log app id
	 * @returns Log message
	 */
	private static getAppId(appId: string): string {
		return this.OPEN_BRACKET + 'app_id: ' + appId + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log class name
	 *
	 * @param className Log class name
	 * @returns Log message
	 */
	private static getClass(className: string): string {
		return this.OPEN_BRACKET + 'class: ' + className + this.CLOSE_BRACKET;
	}

	/**
	 * Get message with log package path
	 *
	 * @param packagePath Log package path
	 * @returns Log message
	 */
	private static getPackage(packagePath: string): string {
		return packagePath ? this.OPEN_BRACKET + 'package: ' + packagePath + this.CLOSE_BRACKET : this.EMPTY;
	}

	/**
	 * Get message with log line
	 *
	 * @param line Log line
	 * @returns Log message
	 */
	private static getLine(line: string): string {
		return this.OPEN_BRACKET + 'line: ' + line + this.CLOSE_BRACKET;
	}
}
