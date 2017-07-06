import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSchedulerSharedModule } from '../../shared';
import {
    ChildService,
    ChildPopupService,
    ChildComponent,
    ChildDetailComponent,
    ChildDialogComponent,
    ChildPopupComponent,
    ChildDeletePopupComponent,
    ChildDeleteDialogComponent,
    childRoute,
    childPopupRoute,
} from './';

const ENTITY_STATES = [
    ...childRoute,
    ...childPopupRoute,
];

@NgModule({
    imports: [
        TotoSchedulerSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChildComponent,
        ChildDetailComponent,
        ChildDialogComponent,
        ChildDeleteDialogComponent,
        ChildPopupComponent,
        ChildDeletePopupComponent,
    ],
    entryComponents: [
        ChildComponent,
        ChildDialogComponent,
        ChildPopupComponent,
        ChildDeleteDialogComponent,
        ChildDeletePopupComponent,
    ],
    providers: [
        ChildService,
        ChildPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TotoSchedulerChildModule {}
