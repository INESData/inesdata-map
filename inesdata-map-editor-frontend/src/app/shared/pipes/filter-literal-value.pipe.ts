import { Pipe, PipeTransform } from '@angular/core';
import { ObjectMapDTO } from 'projects/mapper-api-client';

import { RR_TEMPLATE } from '../utils/app.constants';

/**
 * Filters an array of objectMap to find the object with the key rr:template
 * and returns its corresponding literal value
 */
@Pipe({
	name: 'filterLiteralValue',
	standalone: true
})
export class FilterLiteralValuePipe implements PipeTransform {

	/**
	 * Transforms the input array by filtering it based on a specific key
	 * @param objectMap - The objectMap to be filtered
	 * @returns The literal value of the object where key matches
	 */
	transform(objectMap: ObjectMapDTO[]): string | null {
		// Validate input
		if (!objectMap || !Array.isArray(objectMap)) {
			return '';
		}
		// Find the object in the array where the key matches the rr:template
		const filteredItem = objectMap.find(item => item.key === RR_TEMPLATE);

		return filteredItem ? filteredItem.literalValue : '';
	}

}
