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
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private childService: ChildService,
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
        if (this.child.id !== undefined) {
            this.subscribeToSaveResponse(
                this.childService.update(this.child));
        } else {
            this.subscribeToSaveResponse(
                this.childService.create(this.child));
        }
    }

    private subscribeToSaveResponse(result: Observable<Child>) {
        result.subscribe((res: Child) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Child) {
        this.eventManager.broadcast({ name: 'childListModification', content: 'OK'});
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
    selector: 'jhi-child-popup',
    template: ''
})
export class ChildPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private childPopupService: ChildPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.childPopupService
                    .open(ChildDialogComponent as Component, params['id']);
            } else {
                this.childPopupService
                    .open(ChildDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
