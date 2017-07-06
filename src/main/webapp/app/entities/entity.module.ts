import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TotoSchedulerRoleModule } from './role/role.module';
import { TotoSchedulerPeriodModule } from './period/period.module';
import { TotoSchedulerTherapyModule } from './therapy/therapy.module';
import { TotoSchedulerTherapyEntryModule } from './therapy-entry/therapy-entry.module';
import { TotoSchedulerChildModule } from './child/child.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TotoSchedulerRoleModule,
        TotoSchedulerPeriodModule,
        TotoSchedulerTherapyModule,
        TotoSchedulerTherapyEntryModule,
        TotoSchedulerChildModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerEntityModule {}
