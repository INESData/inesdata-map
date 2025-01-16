import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DataBaseSourceService } from 'projects/mapper-api-client';

@Component({
	selector: 'app-mappings-form',
	templateUrl: './mappings-form.component.html'
})
export class MappingsFormComponent implements OnInit {
	destroyRef = inject(DestroyRef);

	constructor(private databaseSourceService: DataBaseSourceService) {}

	ngOnInit() {
		this.databaseSourceService
			.getTableNames(1)
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe((data) => {
				console.log(data);
				console.log(data.length);
				console.log(data.join(','));
			});
	}
}
