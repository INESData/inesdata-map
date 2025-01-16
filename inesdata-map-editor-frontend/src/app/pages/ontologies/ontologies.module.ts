import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

import { OntologiesComponent } from './ontologies.component';
import { OntologiesFormComponent } from './ontologies-form/ontologies-form.component';
import { OntologiesListComponent } from './ontologies-list/ontologies-list.component';
import { OntologiesRoutingModule } from './ontologies-routing.module';

@NgModule({
	declarations: [OntologiesComponent, OntologiesListComponent, OntologiesFormComponent],
	imports: [OntologiesRoutingModule, SharedModule]
})
export class OntologiesModule {}
