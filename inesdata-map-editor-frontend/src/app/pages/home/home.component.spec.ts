import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';

import { HomeComponent } from './home.component';

const translations = {};
class FakeLoader implements TranslateLoader {
	getTranslation(): Observable<unknown> {
		return of(translations);
	}
}

describe('HomeComponent', () => {
	let component: HomeComponent;
	let fixture: ComponentFixture<HomeComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			imports: [
				TranslateModule.forRoot({
					loader: { provide: TranslateLoader, useClass: FakeLoader }
				})
			],
			declarations: [HomeComponent]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(HomeComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('should contain an image with the correct src', () => {
		const imgElement = fixture.debugElement.nativeElement.querySelector('img');
		expect(imgElement.src).toContain('assets/images/home.svg');
	});
});
