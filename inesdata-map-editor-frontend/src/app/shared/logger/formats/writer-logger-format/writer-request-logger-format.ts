import { RequestLoggerFormatModel } from '../../../models/logger-format.model';

/**
 * Writer request logger format
 */
export class WriterRequestLoggerFormat {
	private static OPEN_BRACKET = '[';
	private static CLOSE_BRACKET = ']';
	private static BLANK = ' ';

	/**
	 * Get the format of request metadata to display in console.
	 * Whether the first element of the input argument has HttpRequest type, it is interpreted as a 'request log' and the corresponding additional metadata will filled.
	 *
	 * @param requestLoggerFormat input arguments provided in the logger
	 * @returns string request format
	 */
	public static getRequestFormat(requestLoggerFormat: RequestLoggerFormatModel): string {
		if (!requestLoggerFormat) {
			return this.BLANK;
		}

		return (
			this.getRequestUri(requestLoggerFormat.requestUri) +
			this.getRequestUrl(requestLoggerFormat.requestUrl) +
			this.getRequestMethod(requestLoggerFormat.method) +
			this.getRequestRemoteHost(requestLoggerFormat.remoteHost) +
			this.getRequestUserAgent(requestLoggerFormat.userAgent)
		);
	}

	/**
	 * Get message with log request URI
	 *
	 * @param uri Log request URI
	 * @returns Log message
	 */
	private static getRequestUri(uri: string): string {
		return uri ? this.OPEN_BRACKET + 'req_requestURI: ' + uri + this.CLOSE_BRACKET : this.BLANK;
	}

	/**
	 * Get message with log request URL
	 *
	 * @param url Log request URL
	 * @returns Log message
	 */
	private static getRequestUrl(url: string): string {
		return url ? this.OPEN_BRACKET + 'req_requestURL: ' + url + this.BLANK + this.CLOSE_BRACKET : this.BLANK;
	}

	/**
	 * Get message with log request method
	 *
	 * @param method Log request method
	 * @returns Log message
	 */
	private static getRequestMethod(method: string): string {
		return method ? this.OPEN_BRACKET + 'req_method: ' + method + this.CLOSE_BRACKET : this.BLANK;
	}

	/**
	 * Get message with log request remote host
	 *
	 * @param host Log request remote host
	 * @returns Log message
	 */
	private static getRequestRemoteHost(host: string): string {
		return host ? this.OPEN_BRACKET + 'req_remoteHost: ' + host + this.CLOSE_BRACKET : this.BLANK;
	}

	/**
	 * Get message with log request user agent
	 *
	 * @param userAgent Log request user agent
	 * @returns Log message
	 */
	private static getRequestUserAgent(userAgent: string): string {
		return userAgent ? this.OPEN_BRACKET + 'req_userAgent: ' + userAgent + this.CLOSE_BRACKET : this.BLANK;
	}
}
