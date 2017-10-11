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
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private therapyTypeService: TherapyTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TherapyType) {
        this.eventManager.broadcast({ name: 'therapyTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-therapy-type-popup',
    template: ''
})
export class TherapyTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyTypePopupService: TherapyTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.therapyTypePopupService
                    .open(TherapyTypeDialogComponent as Component, params['id']);
            } else {
                this.therapyTypePopupService
                    .open(TherapyTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
