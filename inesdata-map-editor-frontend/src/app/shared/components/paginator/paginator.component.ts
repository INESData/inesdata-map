import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PageSearchOntologyDTO } from 'projects/mapper-api-client/model/pageSearchOntologyDTO';

import { PaginatedResponse } from '../../models/paginated-response.model';
import { SIZE } from '../../utils/app.constants';

@Component({
	selector: 'app-paginator',
	templateUrl: './paginator.component.html'
})
export class PaginatorComponent {
	/**
	 * Pagination information, including the current page, total pages, and items per page
	 */
	@Input() paginationInfo: PaginatedResponse<PageSearchOntologyDTO>;
	/**
	 * Number of items per page
	 */
	@Input() rows: number = SIZE;

	/**
	 * Emits the current page number when the page changes
	 */
	@Output() pageChange = new EventEmitter<number>();

	/**
	 * Method that is triggered when the page changes. It emits the pageChange event
	 * with the current page number.
	 */
	onPageChange(event): void {
		this.pageChange.emit(event.page);
	}
}
