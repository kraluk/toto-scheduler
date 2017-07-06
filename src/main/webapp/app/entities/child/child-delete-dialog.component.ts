import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Child } from './child.model';
import { ChildPopupService } from './child-popup.service';
import { ChildService } from './child.service';

@Component({
    selector: 'jhi-child-delete-dialog',
    templateUrl: './child-delete-dialog.component.html'
})
export class ChildDeleteDialogComponent {

    child: Child;

    constructor(
        private childService: ChildService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.childService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'childListModification',
                content: 'Deleted an child'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('totoSchedulerApp.child.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-child-delete-popup',
    template: ''
})
export class ChildDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private childPopupService: ChildPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.childPopupService
                .open(ChildDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
