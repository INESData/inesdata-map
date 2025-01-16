import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { Error403Component } from './error403.component';
import { Error403RoutingModule } from './error403-routing.module';

/**
 * Error 403 component module
 */
@NgModule({
	declarations: [Error403Component],
	imports: [CommonModule, Error403RoutingModule]
})
export class Error403Module {}
