import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TherapyEntry } from './therapy-entry.model';
import { TherapyEntryPopupService } from './therapy-entry-popup.service';
import { TherapyEntryService } from './therapy-entry.service';

@Component({
    selector: 'jhi-therapy-entry-delete-dialog',
    templateUrl: './therapy-entry-delete-dialog.component.html'
})
export class TherapyEntryDeleteDialogComponent {

    therapyEntry: TherapyEntry;

    constructor(
        private therapyEntryService: TherapyEntryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.therapyEntryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'therapyEntryListModification',
                content: 'Deleted an therapyEntry'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-therapy-entry-delete-popup',
    template: ''
})
export class TherapyEntryDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyEntryPopupService: TherapyEntryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.therapyEntryPopupService
                .open(TherapyEntryDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
