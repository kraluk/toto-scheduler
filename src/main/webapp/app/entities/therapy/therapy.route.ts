import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TherapyComponent } from './therapy.component';
import { TherapyDetailComponent } from './therapy-detail.component';
import { TherapyPopupComponent } from './therapy-dialog.component';
import { TherapyDeletePopupComponent } from './therapy-delete-dialog.component';

import { Principal } from '../../shared';

export const therapyRoute: Routes = [
    {
        path: 'therapy',
        component: TherapyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'therapy/:id',
        component: TherapyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const therapyPopupRoute: Routes = [
    {
        path: 'therapy-new',
        component: TherapyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy/:id/edit',
        component: TherapyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'therapy/:id/delete',
        component: TherapyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.therapy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
