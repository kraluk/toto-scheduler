import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TherapyEntryComponent } from './therapy-entry.component';
import { TherapyEntryDetailComponent } from './therapy-entry-detail.component';
import { TherapyEntryPopupComponent } from './therapy-entry-dialog.component';
import { TherapyEntryDeletePopupComponent } from './therapy-entry-delete-dialog.component';

import { Principal } from '../../shared';

export const therapyEntryRoute: Routes = [
    {
        path: 'therapy-entry',
        component: TherapyEntryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'therapy-entry/:id',
        component: TherapyEntryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const therapyEntryPopupRoute: Routes = [
    {
        path: 'therapy-entry-new',
        component: TherapyEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy-entry/:id/edit',
        component: TherapyEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy-entry/:id/delete',
        component: TherapyEntryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
