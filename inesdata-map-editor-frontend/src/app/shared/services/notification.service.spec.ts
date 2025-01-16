import { TestBed } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';

import { NotificationService } from './notification.service';

describe('NotificationService', () => {
	let service: NotificationService;

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [LoggerTestingModule]
		});
		service = TestBed.inject(NotificationService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
