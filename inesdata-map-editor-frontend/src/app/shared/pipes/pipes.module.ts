import { NgModule } from '@angular/core';

import { BoolToYesNoPipe } from './bool-to-yes-no.pipe';

@NgModule({
	imports: [],
	declarations: [BoolToYesNoPipe],
	exports: [BoolToYesNoPipe]
})
export class PipesModule {}
