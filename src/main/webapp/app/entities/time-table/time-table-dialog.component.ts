import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { TimeTablePopupService } from './time-table-popup.service';
import { TimeTableService } from './time-table.service';
import { Child, ChildService } from '../child';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-table-dialog',
    templateUrl: './time-table-dialog.component.html'
})
export class TimeTableDialogComponent implements OnInit {

    timeTable: TimeTable;
    authorities: any[];
    isSaving: boolean;

    children: Child[];
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private timeTableService: TimeTableService,
        private childService: ChildService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.childService.query()
            .subscribe((res: ResponseWrapper) => { this.children = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timeTable.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timeTableService.update(this.timeTable));
        } else {
            this.subscribeToSaveResponse(
                this.timeTableService.create(this.timeTable));
        }
    }

    private subscribeToSaveResponse(result: Observable<TimeTable>) {
        result.subscribe((res: TimeTable) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TimeTable) {
        this.eventManager.broadcast({ name: 'timeTableListModification', content: 'OK'});
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

    trackChildById(index: number, item: Child) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-time-table-popup',
    template: ''
})
export class TimeTablePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeTablePopupService: TimeTablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.timeTablePopupService
                    .open(TimeTableDialogComponent, params['id']);
            } else {
                this.modalRef = this.timeTablePopupService
                    .open(TimeTableDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
