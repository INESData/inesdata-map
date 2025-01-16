import { NgModule } from '@angular/core';
import { FilterLiteralValuePipe } from 'src/app/shared/pipes/filter-literal-value.pipe';
import { JoinPipe } from 'src/app/shared/pipes/join.pipe';
import { SharedModule } from 'src/app/shared/shared.module';

import { MappingsComponent } from './mappings.component';
import { MappingsBuilderComponent } from './mappings-builder/mappings-builder.component';
import { MappingsFormComponent } from './mappings-form/mappings-form.component';
import { MappingsListComponent } from './mappings-list/mappings-list.component';
import { MappingsRecordsComponent } from './mappings-records/mappings-records.component';
import { MappingsRoutingModule } from './mappings-routing.module';
import { MappingsSummaryComponent } from './mappings-summary/mappings-summary.component';

@NgModule({
	declarations: [MappingsComponent, MappingsListComponent, MappingsFormComponent, MappingsRecordsComponent, MappingsBuilderComponent, MappingsSummaryComponent],
	imports: [MappingsRoutingModule, SharedModule, JoinPipe, FilterLiteralValuePipe]
})
export class MappingsModule { }
