import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DataSourcesListComponent } from './data-sources-list/data-sources-list.components';

const routes: Routes = [
	{
		path: '',
		component: DataSourcesListComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class DataSourcesRoutingModule {}
