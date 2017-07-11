import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Therapy } from './therapy.model';
import { TherapyPopupService } from './therapy-popup.service';
import { TherapyService } from './therapy.service';
import { TherapyType, TherapyTypeService } from '../therapy-type';
import { Therapist, TherapistService } from '../therapist';
import { TimeTable, TimeTableService } from '../time-table';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-therapy-dialog',
    templateUrl: './therapy-dialog.component.html'
})
export class TherapyDialogComponent implements OnInit {

    therapy: Therapy;
    authorities: any[];
    isSaving: boolean;

    therapytypes: TherapyType[];

    therapists: Therapist[];

    timetables: TimeTable[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private therapyService: TherapyService,
        private therapyTypeService: TherapyTypeService,
        private therapistService: TherapistService,
        private timeTableService: TimeTableService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.therapyTypeService
            .query({filter: 'therapy-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.therapy.therapyTypeId) {
                    this.therapytypes = res.json;
                } else {
                    this.therapyTypeService
                        .find(this.therapy.therapyTypeId)
                        .subscribe((subRes: TherapyType) => {
                            this.therapytypes = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.therapistService.query()
            .subscribe((res: ResponseWrapper) => { this.therapists = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.timeTableService.query()
            .subscribe((res: ResponseWrapper) => { this.timetables = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.therapy.id !== undefined) {
            this.subscribeToSaveResponse(
                this.therapyService.update(this.therapy));
        } else {
            this.subscribeToSaveResponse(
                this.therapyService.create(this.therapy));
        }
    }

    private subscribeToSaveResponse(result: Observable<Therapy>) {
        result.subscribe((res: Therapy) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Therapy) {
        this.eventManager.broadcast({ name: 'therapyListModification', content: 'OK'});
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

    trackTherapyTypeById(index: number, item: TherapyType) {
        return item.id;
    }

    trackTherapistById(index: number, item: Therapist) {
        return item.id;
    }

    trackTimeTableById(index: number, item: TimeTable) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-therapy-popup',
    template: ''
})
export class TherapyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapyPopupService: TherapyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.therapyPopupService
                    .open(TherapyDialogComponent, params['id']);
            } else {
                this.modalRef = this.therapyPopupService
                    .open(TherapyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
