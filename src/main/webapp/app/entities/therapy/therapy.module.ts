import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import {
    TherapyService,
    TherapyPopupService,
    TherapyComponent,
    TherapyDetailComponent,
    TherapyDialogComponent,
    TherapyPopupComponent,
    TherapyDeletePopupComponent,
    TherapyDeleteDialogComponent,
    therapyRoute,
    therapyPopupRoute,
} from './';

const ENTITY_STATES = [
    ...therapyRoute,
    ...therapyPopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TherapyComponent,
        TherapyDetailComponent,
        TherapyDialogComponent,
        TherapyDeleteDialogComponent,
        TherapyPopupComponent,
        TherapyDeletePopupComponent,
    ],
    entryComponents: [
        TherapyComponent,
        TherapyDialogComponent,
        TherapyPopupComponent,
        TherapyDeleteDialogComponent,
        TherapyDeletePopupComponent,
    ],
    providers: [
        TherapyService,
        TherapyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerTherapyModule {}
