import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Error403Component } from './error403.component';

const routes: Routes = [{ path: '', component: Error403Component }];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class Error403RoutingModule {}
