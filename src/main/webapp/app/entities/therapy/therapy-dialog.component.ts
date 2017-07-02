import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Therapy } from './therapy.model';
import { TherapyPopupService } from './therapy-popup.service';
import { TherapyService } from './therapy.service';
import { TherapyEntry, TherapyEntryService } from '../therapy-entry';
import { User, UserService } from '../../shared';
import { Period, PeriodService } from '../period';
import { Child, ChildService } from '../child';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-therapy-dialog',
    templateUrl: './therapy-dialog.component.html'
})
export class TherapyDialogComponent implements OnInit {

    therapy: Therapy;
    authorities: any[];
    isSaving: boolean;

    therapyentries: TherapyEntry[];

    users: User[];

    periods: Period[];

    children: Child[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private therapyService: TherapyService,
        private therapyEntryService: TherapyEntryService,
        private userService: UserService,
        private periodService: PeriodService,
        private childService: ChildService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.therapyEntryService
            .query({filter: 'therapy-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.therapy.therapyEntryId) {
                    this.therapyentries = res.json;
                } else {
                    this.therapyEntryService
                        .find(this.therapy.therapyEntryId)
                        .subscribe((subRes: TherapyEntry) => {
                            this.therapyentries = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.periodService.query()
            .subscribe((res: ResponseWrapper) => { this.periods = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.childService.query()
            .subscribe((res: ResponseWrapper) => { this.children = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.therapy.id !== undefined) {
            this.subscribeToSaveResponse(
                this.therapyService.update(this.therapy), false);
        } else {
            this.subscribeToSaveResponse(
                this.therapyService.create(this.therapy), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Therapy>, isCreated: boolean) {
        result.subscribe((res: Therapy) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Therapy, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'totoSchedulerApp.therapy.created'
            : 'totoSchedulerApp.therapy.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'therapyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackTherapyEntryById(index: number, item: TherapyEntry) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackPeriodById(index: number, item: Period) {
        return item.id;
    }

    trackChildById(index: number, item: Child) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-therapy-popup',
    template: ''
})
export class TherapyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyPopupService: TherapyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.therapyPopupService
                    .open(TherapyDialogComponent, params['id']);
            } else {
                this.modalRef = this.therapyPopupService
                    .open(TherapyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
