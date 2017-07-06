import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TherapyEntry } from './therapy-entry.model';
import { TherapyEntryPopupService } from './therapy-entry-popup.service';
import { TherapyEntryService } from './therapy-entry.service';

@Component({
    selector: 'jhi-therapy-entry-dialog',
    templateUrl: './therapy-entry-dialog.component.html'
})
export class TherapyEntryDialogComponent implements OnInit {

    therapyEntry: TherapyEntry;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private therapyEntryService: TherapyEntryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.therapyEntry.id !== undefined) {
            this.subscribeToSaveResponse(
                this.therapyEntryService.update(this.therapyEntry), false);
        } else {
            this.subscribeToSaveResponse(
                this.therapyEntryService.create(this.therapyEntry), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TherapyEntry>, isCreated: boolean) {
        result.subscribe((res: TherapyEntry) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TherapyEntry, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'totoSchedulerApp.therapyEntry.created'
            : 'totoSchedulerApp.therapyEntry.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'therapyEntryListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-therapy-entry-popup',
    template: ''
})
export class TherapyEntryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyEntryPopupService: TherapyEntryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.therapyEntryPopupService
                    .open(TherapyEntryDialogComponent, params['id']);
            } else {
                this.modalRef = this.therapyEntryPopupService
                    .open(TherapyEntryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
