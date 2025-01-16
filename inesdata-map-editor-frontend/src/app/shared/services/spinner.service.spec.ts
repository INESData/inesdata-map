import { TestBed } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';

import { SpinnerService } from './spinner.service';

describe('SpinnerService', () => {
	beforeEach(() =>
		TestBed.configureTestingModule({
			imports: [LoggerTestingModule]
		})
	);

	it('should be created', () => {
		const service: SpinnerService = TestBed.get(SpinnerService);
		expect(service).toBeTruthy();
	});

	it('start spinner', () => {
		const service: SpinnerService = TestBed.get(SpinnerService);
		service.startSpinner();
		expect(service.loading).toBeTruthy();
	});

	it('stop spinner', () => {
		const service: SpinnerService = TestBed.get(SpinnerService);
		service.startSpinner();
		service.stopSpinner();
		expect(service.loading).toBeFalsy();
	});
});
