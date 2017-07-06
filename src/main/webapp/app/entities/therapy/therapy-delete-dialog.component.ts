import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Therapy } from './therapy.model';
import { TherapyPopupService } from './therapy-popup.service';
import { TherapyService } from './therapy.service';

@Component({
    selector: 'jhi-therapy-delete-dialog',
    templateUrl: './therapy-delete-dialog.component.html'
})
export class TherapyDeleteDialogComponent {

    therapy: Therapy;

    constructor(
        private therapyService: TherapyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.therapyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'therapyListModification',
                content: 'Deleted an therapy'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-therapy-delete-popup',
    template: ''
})
export class TherapyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyPopupService: TherapyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.therapyPopupService
                .open(TherapyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
