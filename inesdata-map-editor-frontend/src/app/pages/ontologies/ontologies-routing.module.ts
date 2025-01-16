import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OntologiesListComponent } from './ontologies-list/ontologies-list.component';

const routes: Routes = [
	{
		path: '',
		component: OntologiesListComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class OntologiesRoutingModule {}
