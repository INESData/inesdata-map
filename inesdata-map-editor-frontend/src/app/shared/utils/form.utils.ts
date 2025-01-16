import { FormBuilder, FormControl, FormGroup } from '@angular/forms';

/**
 * Create a form group from a dto
 * @param fb the form builder
 * @param group the form group
 * @returns the form group of the dto
 */
const createDtoForm = (fb: FormBuilder, group: FormGroup): FormGroup => {
	const controlsConfig = {};

	// Iterate over all controls in the original FormGroup
	Object.keys(group.controls).forEach((key) => {
		const control = group.get(key) as FormControl;

		// Clone each control with its current value and validators
		controlsConfig[key] = new FormControl(
			control.value,
			control.validator,
			control.asyncValidator
		);
	});

	// Create and return a new FormGroup with the same configuration
	return fb.group(controlsConfig);
};

export { createDtoForm };
