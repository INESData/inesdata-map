import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MappingsBuilderComponent } from './mappings-builder/mappings-builder.component';
import { MappingsListComponent } from './mappings-list/mappings-list.component';

const routes: Routes = [
	{
		path: '',
		component: MappingsListComponent
	},
	{
		path: 'builder',
		component: MappingsBuilderComponent
	},
	{
		path: 'builder/edit/:id',
		component: MappingsBuilderComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class MappingsRoutingModule {}
