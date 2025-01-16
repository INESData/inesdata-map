import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';

import { SharedTestModule } from '../../shared-tests.module';
import { NotificationComponent } from './notification.component';

describe('NotificationComponent', () => {
	let component: NotificationComponent;
	let fixture: ComponentFixture<NotificationComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [NotificationComponent],
			imports: [SharedTestModule, LoggerTestingModule]
		}).compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(NotificationComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
