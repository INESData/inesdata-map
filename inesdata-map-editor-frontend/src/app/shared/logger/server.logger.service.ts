import { HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { INGXLoggerMetadata, NGXLoggerServerService } from 'ngx-logger';

import { LoggerFormat } from './formats/logger-format';

/**
 * Server logger service customization
 */
@Injectable()
export class ServerLoggerService extends NGXLoggerServerService {
	private CONTENT_TYPE = 'Content-Type';
	private APPLICATION_JSON = 'application/json';
	// Uncomment this lines if your application require jwt authentication
	//private AUTHORIZATION: string = 'Authorization';
	//private BEARER: string = 'Bearer ';
	//private TOKEN_KEY: string = 'jwt_token';

	/**
	 * Customse the body sent to the API
	 * @param metadata the data provided by NGXLogger
	 * @returns the body customised
	 */
	public customiseRequestBody(metadata: INGXLoggerMetadata): string {
		return this.getJsonFormatMetadata(metadata);
	}

	/**
	 * Edits HttpRequest object before sending request to server
	 *
	 * @param httpRequest default request object
	 * @returns altered httprequest
	 */
	public alterHttpRequest(httpRequest: HttpRequest<unknown>): HttpRequest<unknown> {
		httpRequest = httpRequest.clone({
			setHeaders: {
				[this.CONTENT_TYPE]: this.APPLICATION_JSON
				// Uncomment this line if your application require jwt authentication
				//[this.AUTHORIZATION]: token ? this.BEARER +  sessionStorage.getItem(this.TOKEN_KEY) : ''
			}
		});

		return httpRequest;
	}

	/**
	 * Get metadata in JSON format and convert from camelCase to snake_case
	 *
	 * @param metadata Metadata info
	 * @returns JSON
	 */
	private getJsonFormatMetadata(metadata: INGXLoggerMetadata): string {
		return JSON.stringify(LoggerFormat.getFormatModel(metadata), function (rootKey, value) {
			if (value && typeof value === 'object') {
				const replacement = {};
				for (const key in value) {
					if (Object.hasOwn(value, key)) {
						replacement[key.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`)] = value[key];
					}
				}

				return replacement;
			}

			return value;
		});
	}
}
