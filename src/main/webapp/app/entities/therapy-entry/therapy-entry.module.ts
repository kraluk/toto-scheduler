import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import {
    TherapyEntryService,
    TherapyEntryPopupService,
    TherapyEntryComponent,
    TherapyEntryDetailComponent,
    TherapyEntryDialogComponent,
    TherapyEntryPopupComponent,
    TherapyEntryDeletePopupComponent,
    TherapyEntryDeleteDialogComponent,
    therapyEntryRoute,
    therapyEntryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...therapyEntryRoute,
    ...therapyEntryPopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TherapyEntryComponent,
        TherapyEntryDetailComponent,
        TherapyEntryDialogComponent,
        TherapyEntryDeleteDialogComponent,
        TherapyEntryPopupComponent,
        TherapyEntryDeletePopupComponent,
    ],
    entryComponents: [
        TherapyEntryComponent,
        TherapyEntryDialogComponent,
        TherapyEntryPopupComponent,
        TherapyEntryDeleteDialogComponent,
        TherapyEntryDeletePopupComponent,
    ],
    providers: [
        TherapyEntryService,
        TherapyEntryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerTherapyEntryModule {}
