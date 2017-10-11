import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import { TotoSchedulerAdminModule } from '../../admin/admin.module';
import {
    TherapistService,
    TherapistPopupService,
    TherapistComponent,
    TherapistDetailComponent,
    TherapistDialogComponent,
    TherapistPopupComponent,
    TherapistDeletePopupComponent,
    TherapistDeleteDialogComponent,
    therapistRoute,
    therapistPopupRoute,
} from './';

const ENTITY_STATES = [
    ...therapistRoute,
    ...therapistPopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        TotoSchedulerAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TherapistComponent,
        TherapistDetailComponent,
        TherapistDialogComponent,
        TherapistDeleteDialogComponent,
        TherapistPopupComponent,
        TherapistDeletePopupComponent,
    ],
    entryComponents: [
        TherapistComponent,
        TherapistDialogComponent,
        TherapistPopupComponent,
        TherapistDeleteDialogComponent,
        TherapistDeletePopupComponent,
    ],
    providers: [
        TherapistService,
        TherapistPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerTherapistModule {}
