import { HttpRequest } from '@angular/common/http';

import { RequestLoggerFormatModel } from '../../models/logger-format.model';

/**
 * Request logger format
 */
export class RequestLoggerFormat {
	private static BLANK = ' ';
	private static URI_REGEX = /^https?:\/\/[A-Za-z0-9:.]*([/]{1}.*\/?)$/;
	private static URI_REGEX_POSITION = 1;
	private static HOST_REGEX = /^(?:https?:\/\/)?(?:[^@/\n]+@)?(?:www\.)?([^:/\n]+)/;
	private static HOST_REGEX_POSITION = 1;
	private static HTTP_REQUEST_METADATA_POSITION = 0;

	/**
	 * Get the format model of request metadata.
	 *
	 * @param aditional input arguments provided in the logger
	 * @returns string request format
	 */
	public static getRequestFormatModel(aditional: unknown[]): RequestLoggerFormatModel {
		if (!aditional || aditional.length < 1 || !(aditional[this.HTTP_REQUEST_METADATA_POSITION] instanceof HttpRequest)) {
			return undefined;
		}

		const request = aditional[this.HTTP_REQUEST_METADATA_POSITION] as HttpRequest<unknown>;

		return {
			remoteHost: this.getRequestRemoteHost(request),
			requestUri: this.getRequestUri(request),
			requestUrl: request.url,
			userAgent: window.navigator.userAgent,
			method: request.method
		};
	}

	/**
	 * Get the request URI
	 *
	 * @param request HTTP request
	 * @returns URI
	 */
	private static getRequestUri(request: HttpRequest<unknown>): string {
		const uri = request.url.match(this.URI_REGEX);
		return uri !== null && uri.length > 1 ? uri[this.URI_REGEX_POSITION] : this.BLANK;
	}

	/**
	 * Get the request remote host
	 *
	 * @param request HTTP request
	 * @returns Remote host
	 */
	private static getRequestRemoteHost(request: HttpRequest<unknown>): string {
		const host = request.url.match(this.HOST_REGEX);
		return host !== null && host.length > 1 ? host[this.HOST_REGEX_POSITION] : this.BLANK;
	}
}
