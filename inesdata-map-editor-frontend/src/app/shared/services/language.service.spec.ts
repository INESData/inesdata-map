import { TestBed } from '@angular/core/testing';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { Observable, of } from 'rxjs';

import { LanguageService } from './language.service';

const translations = {};
class FakeLoader implements TranslateLoader {
	getTranslation(): Observable<unknown> {
		return of(translations);
	}
}

describe('LanguageService', () => {
	beforeEach(() =>
		TestBed.configureTestingModule({
			imports: [
				TranslateModule.forRoot({
					loader: { provide: TranslateLoader, useClass: FakeLoader }
				}),
				LoggerTestingModule
			],
			providers: [TranslateService]
		})
	);

	it('should be created', () => {
		const service: LanguageService = TestBed.inject(LanguageService);
		expect(service).toBeTruthy();
	});
});
