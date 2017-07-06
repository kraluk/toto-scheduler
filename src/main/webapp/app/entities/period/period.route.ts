import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PeriodComponent } from './period.component';
import { PeriodDetailComponent } from './period-detail.component';
import { PeriodPopupComponent } from './period-dialog.component';
import { PeriodDeletePopupComponent } from './period-delete-dialog.component';

import { Principal } from '../../shared';

export const periodRoute: Routes = [
    {
        path: 'period',
        component: PeriodComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.period.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'period/:id',
        component: PeriodDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.period.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const periodPopupRoute: Routes = [
    {
        path: 'period-new',
        component: PeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.period.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'period/:id/edit',
        component: PeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.period.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'period/:id/delete',
        component: PeriodDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.period.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
