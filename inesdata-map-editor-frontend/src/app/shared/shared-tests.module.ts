import { NgModule } from '@angular/core';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';

import { SharedModule } from './shared.module';

const translations = {};
class FakeLoader implements TranslateLoader {
	getTranslation(): Observable<unknown> {
		return of(translations);
	}
}

/**
 * Shared test module
 *
 */
@NgModule({
	imports: [
		SharedModule,
		TranslateModule.forRoot({
			loader: { provide: TranslateLoader, useClass: FakeLoader }
		})
	],
	declarations: [],
	providers: [TranslateService],
	exports: [SharedModule]
})
export class SharedTestModule {}
