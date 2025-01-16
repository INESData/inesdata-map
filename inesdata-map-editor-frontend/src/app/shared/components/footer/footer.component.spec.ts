import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';

import { FooterComponent } from './footer.component';

describe('FooterComponent', () => {
	let component: FooterComponent;
	let fixture: ComponentFixture<FooterComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [FooterComponent],
			imports: [LoggerTestingModule]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(FooterComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('footer should contain the year ', () => {
		const compiled = fixture.debugElement.nativeElement;
		expect(compiled.querySelector('.footer__company').textContent).toContain(new Date().getFullYear());
	});
});
