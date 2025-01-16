import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { Observable, of } from 'rxjs';
import { MenuService } from 'src/app/shared/services/menu.service';

import { HeaderComponent } from './header.component';

const translations = {};
class FakeLoader implements TranslateLoader {
	getTranslation(): Observable<unknown> {
		return of(translations);
	}
}

describe('HeaderComponent', () => {
	let component: HeaderComponent;
	let fixture: ComponentFixture<HeaderComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [HeaderComponent],
			imports: [
				TranslateModule.forRoot({
					loader: { provide: TranslateLoader, useClass: FakeLoader }
				}),
				LoggerTestingModule
			],
			providers: [MenuService, provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(HeaderComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('header should contain the logo ', () => {
		fixture.detectChanges();
		const compiled = fixture.debugElement.nativeElement;
		expect(compiled.querySelector('img')).not.toEqual(null);
	});
});
