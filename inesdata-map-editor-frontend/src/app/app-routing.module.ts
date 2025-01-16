import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Application routes
const routes: Routes = [
	{
		path: '',
		redirectTo: 'home',
		pathMatch: 'full'
	},
	{
		path: 'home',
		loadChildren: () => import('./pages/home/home.module').then((m) => m.HomeModule),
	},
	{
		path: 'ontologies',
		loadChildren: () => import('./pages/ontologies/ontologies.module').then(m => m.OntologiesModule)
	},
	{
		path: 'data-sources',
		loadChildren: () => import('./pages/data-sources/data-sources.module').then(m => m.DataSourcesModule)
	},
	{
		path: 'mappings',
		loadChildren: () => import('./pages/mappings/mappings.module').then(m => m.MappingsModule)
	},
	{
		path: '403',
		loadChildren: () => import('./pages/error/error403/error403.module').then((m) => m.Error403Module)
	},
	{
		path: '**',
		loadChildren: () => import('./pages/error/error404/error404.module').then((m) => m.Error404Module)
	}
];

/**
 * App routing module
 */
@NgModule({
	imports: [RouterModule.forRoot(routes, {})],
	exports: [RouterModule]
})
export class AppRoutingModule { }
