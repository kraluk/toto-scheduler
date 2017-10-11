import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import {
    TherapyTypeService,
    TherapyTypePopupService,
    TherapyTypeComponent,
    TherapyTypeDetailComponent,
    TherapyTypeDialogComponent,
    TherapyTypePopupComponent,
    TherapyTypeDeletePopupComponent,
    TherapyTypeDeleteDialogComponent,
    therapyTypeRoute,
    therapyTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...therapyTypeRoute,
    ...therapyTypePopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TherapyTypeComponent,
        TherapyTypeDetailComponent,
        TherapyTypeDialogComponent,
        TherapyTypeDeleteDialogComponent,
        TherapyTypePopupComponent,
        TherapyTypeDeletePopupComponent,
    ],
    entryComponents: [
        TherapyTypeComponent,
        TherapyTypeDialogComponent,
        TherapyTypePopupComponent,
        TherapyTypeDeleteDialogComponent,
        TherapyTypeDeletePopupComponent,
    ],
    providers: [
        TherapyTypeService,
        TherapyTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerTherapyTypeModule {}
