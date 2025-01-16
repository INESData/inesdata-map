import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { Error404Component } from './error404.component';
import { Error404RoutingModule } from './error404-routing.module';

/**
 * Error 404 component module
 */
@NgModule({
	declarations: [Error404Component],
	imports: [CommonModule, Error404RoutingModule]
})
export class Error404Module {}
