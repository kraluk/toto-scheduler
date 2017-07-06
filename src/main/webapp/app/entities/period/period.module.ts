import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import {
    PeriodService,
    PeriodPopupService,
    PeriodComponent,
    PeriodDetailComponent,
    PeriodDialogComponent,
    PeriodPopupComponent,
    PeriodDeletePopupComponent,
    PeriodDeleteDialogComponent,
    periodRoute,
    periodPopupRoute,
} from './';

const ENTITY_STATES = [
    ...periodRoute,
    ...periodPopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PeriodComponent,
        PeriodDetailComponent,
        PeriodDialogComponent,
        PeriodDeleteDialogComponent,
        PeriodPopupComponent,
        PeriodDeletePopupComponent,
    ],
    entryComponents: [
        PeriodComponent,
        PeriodDialogComponent,
        PeriodPopupComponent,
        PeriodDeleteDialogComponent,
        PeriodDeletePopupComponent,
    ],
    providers: [
        PeriodService,
        PeriodPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerPeriodModule {}
