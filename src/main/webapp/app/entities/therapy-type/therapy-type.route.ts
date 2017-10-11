import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TherapyTypeComponent } from './therapy-type.component';
import { TherapyTypeDetailComponent } from './therapy-type-detail.component';
import { TherapyTypePopupComponent } from './therapy-type-dialog.component';
import { TherapyTypeDeletePopupComponent } from './therapy-type-delete-dialog.component';

export const therapyTypeRoute: Routes = [
    {
        path: 'therapy-type',
        component: TherapyTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'therapy-type/:id',
        component: TherapyTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const therapyTypePopupRoute: Routes = [
    {
        path: 'therapy-type-new',
        component: TherapyTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy-type/:id/edit',
        component: TherapyTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy-type/:id/delete',
        component: TherapyTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapyType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
