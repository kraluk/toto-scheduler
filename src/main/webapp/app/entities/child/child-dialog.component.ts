import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Child } from './child.model';
import { ChildPopupService } from './child-popup.service';
import { ChildService } from './child.service';

@Component({
    selector: 'jhi-child-dialog',
    templateUrl: './child-dialog.component.html'
})
export class ChildDialogComponent implements OnInit {

    child: Child;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private childService: ChildService,
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
        if (this.child.id !== undefined) {
            this.subscribeToSaveResponse(
                this.childService.update(this.child), false);
        } else {
            this.subscribeToSaveResponse(
                this.childService.create(this.child), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Child>, isCreated: boolean) {
        result.subscribe((res: Child) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Child, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'totoSchedulerApp.child.created'
            : 'totoSchedulerApp.child.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'childListModification', content: 'OK'});
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
    selector: 'jhi-child-popup',
    template: ''
})
export class ChildPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private childPopupService: ChildPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.childPopupService
                    .open(ChildDialogComponent, params['id']);
            } else {
                this.modalRef = this.childPopupService
                    .open(ChildDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
