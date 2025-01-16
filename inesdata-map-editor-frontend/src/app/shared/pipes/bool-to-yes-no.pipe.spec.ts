import { TestBed } from '@angular/core/testing';
import { TranslateStore } from '@ngx-translate/core';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { TranslateTestingModule } from 'ngx-translate-testing';

import enTranslations from '../../../assets/i18n/en.json';
import { LanguageService } from '../services/language.service';
import { BoolToYesNoPipe } from './bool-to-yes-no.pipe';

describe('BoolToYesNoPipe', () => {
	beforeEach(() =>
		TestBed.configureTestingModule({
			imports: [TranslateTestingModule.withTranslations('en', enTranslations), LoggerTestingModule],
			providers: [TranslateStore]
		})
	);

	it('create an instance', () => {
		const pipe: BoolToYesNoPipe = new BoolToYesNoPipe(TestBed.inject(LanguageService));
		expect(pipe).toBeTruthy();
	});

	it('return yes', () => {
		const pipe: BoolToYesNoPipe = new BoolToYesNoPipe(TestBed.inject(LanguageService));
		expect(pipe.transform(true)).toEqual('Yes');
	});

	it('return no', () => {
		const pipe: BoolToYesNoPipe = new BoolToYesNoPipe(TestBed.inject(LanguageService));
		expect(pipe.transform(false)).toEqual('No');
	});
});
