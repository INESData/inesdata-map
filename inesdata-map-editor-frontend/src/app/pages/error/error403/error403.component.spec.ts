import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { Error403Component } from './error403.component';

describe('Error403Component', () => {
	let component: Error403Component;
	let fixture: ComponentFixture<Error403Component>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [Error403Component]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(Error403Component);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
