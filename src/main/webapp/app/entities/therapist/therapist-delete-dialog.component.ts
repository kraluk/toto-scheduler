import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Therapist } from './therapist.model';
import { TherapistPopupService } from './therapist-popup.service';
import { TherapistService } from './therapist.service';

@Component({
    selector: 'jhi-therapist-delete-dialog',
    templateUrl: './therapist-delete-dialog.component.html'
})
export class TherapistDeleteDialogComponent {

    therapist: Therapist;

    constructor(
        private therapistService: TherapistService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.therapistService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'therapistListModification',
                content: 'Deleted an therapist'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-therapist-delete-popup',
    template: ''
})
export class TherapistDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapistPopupService: TherapistPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.therapistPopupService
                .open(TherapistDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
