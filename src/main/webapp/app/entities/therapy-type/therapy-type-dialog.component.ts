import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TherapyType } from './therapy-type.model';
import { TherapyTypePopupService } from './therapy-type-popup.service';
import { TherapyTypeService } from './therapy-type.service';

@Component({
    selector: 'jhi-therapy-type-dialog',
    templateUrl: './therapy-type-dialog.component.html'
})
export class TherapyTypeDialogComponent implements OnInit {

    therapyType: TherapyType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private therapyTypeService: TherapyTypeService,
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
        if (this.therapyType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.therapyTypeService.update(this.therapyType));
        } else {
            this.subscribeToSaveResponse(
                this.therapyTypeService.create(this.therapyType));
        }
    }

    private subscribeToSaveResponse(result: Observable<TherapyType>) {
        result.subscribe((res: TherapyType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TherapyType) {
        this.eventManager.broadcast({ name: 'therapyTypeListModification', content: 'OK'});
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
    selector: 'jhi-therapy-type-popup',
    template: ''
})
export class TherapyTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyTypePopupService: TherapyTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.therapyTypePopupService
                    .open(TherapyTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.therapyTypePopupService
                    .open(TherapyTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
