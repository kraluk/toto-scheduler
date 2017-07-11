import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TherapistComponent } from './therapist.component';
import { TherapistDetailComponent } from './therapist-detail.component';
import { TherapistPopupComponent } from './therapist-dialog.component';
import { TherapistDeletePopupComponent } from './therapist-delete-dialog.component';

import { Principal } from '../../shared';

export const therapistRoute: Routes = [
    {
        path: 'therapist',
        component: TherapistComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'therapist/:id',
        component: TherapistDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const therapistPopupRoute: Routes = [
    {
        path: 'therapist-new',
        component: TherapistPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapist/:id/edit',
        component: TherapistPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapist/:id/delete',
        component: TherapistDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
