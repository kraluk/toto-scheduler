import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TotoSchedulerRoleModule } from './role/role.module';
import { TotoSchedulerTimeTableModule } from './time-table/time-table.module';
import { TotoSchedulerTherapyModule } from './therapy/therapy.module';
import { TotoSchedulerTherapyTypeModule } from './therapy-type/therapy-type.module';
import { TotoSchedulerChildModule } from './child/child.module';
import { TotoSchedulerTherapistModule } from './therapist/therapist.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TotoSchedulerRoleModule,
        TotoSchedulerTimeTableModule,
        TotoSchedulerTherapyModule,
        TotoSchedulerTherapyTypeModule,
        TotoSchedulerChildModule,
        TotoSchedulerTherapistModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerEntityModule {}
