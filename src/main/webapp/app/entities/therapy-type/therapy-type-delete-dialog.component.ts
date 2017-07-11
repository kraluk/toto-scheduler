import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TherapyType } from './therapy-type.model';
import { TherapyTypePopupService } from './therapy-type-popup.service';
import { TherapyTypeService } from './therapy-type.service';

@Component({
    selector: 'jhi-therapy-type-delete-dialog',
    templateUrl: './therapy-type-delete-dialog.component.html'
})
export class TherapyTypeDeleteDialogComponent {

    therapyType: TherapyType;

    constructor(
        private therapyTypeService: TherapyTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.therapyTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'therapyTypeListModification',
                content: 'Deleted an therapyType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-therapy-type-delete-popup',
    template: ''
})
export class TherapyTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyTypePopupService: TherapyTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.therapyTypePopupService
                .open(TherapyTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
