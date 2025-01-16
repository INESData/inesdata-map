import { Injectable } from '@angular/core';
import { INGXLoggerMetadata, NGXLoggerWriterService } from 'ngx-logger';

import { WriterLoggerFormat } from './formats/writer-logger-format/writer-logger-format';

/**
 * Write logger service
 */
@Injectable()
export class WriteLoggerService extends NGXLoggerWriterService {
	/**
	 * Modifies the metadata to be printed by console
	 *
	 * @param metadata additional logger information
	 * @returns string
	 */
	public prepareMetaString(metadata: INGXLoggerMetadata): string {
		return WriterLoggerFormat.getFormat(metadata);
	}
}
