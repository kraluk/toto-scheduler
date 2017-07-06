import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChildComponent } from './child.component';
import { ChildDetailComponent } from './child-detail.component';
import { ChildPopupComponent } from './child-dialog.component';
import { ChildDeletePopupComponent } from './child-delete-dialog.component';

import { Principal } from '../../shared';

export const childRoute: Routes = [
    {
        path: 'child',
        component: ChildComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.child.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'child/:id',
        component: ChildDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.child.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const childPopupRoute: Routes = [
    {
        path: 'child-new',
        component: ChildPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.child.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'child/:id/edit',
        component: ChildPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.child.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'child/:id/delete',
        component: ChildDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'totoSchedulerApp.child.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
