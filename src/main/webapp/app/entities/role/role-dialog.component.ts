import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Role } from './role.model';
import { RolePopupService } from './role-popup.service';
import { RoleService } from './role.service';
import { Therapist, TherapistService } from '../therapist';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-role-dialog',
    templateUrl: './role-dialog.component.html'
})
export class RoleDialogComponent implements OnInit {

    role: Role;
    authorities: any[];
    isSaving: boolean;

    therapists: Therapist[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private roleService: RoleService,
        private therapistService: TherapistService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.therapistService.query()
            .subscribe((res: ResponseWrapper) => { this.therapists = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.role.id !== undefined) {
            this.subscribeToSaveResponse(
                this.roleService.update(this.role));
        } else {
            this.subscribeToSaveResponse(
                this.roleService.create(this.role));
        }
    }

    private subscribeToSaveResponse(result: Observable<Role>) {
        result.subscribe((res: Role) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Role) {
        this.eventManager.broadcast({ name: 'roleListModification', content: 'OK'});
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

    trackTherapistById(index: number, item: Therapist) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-role-popup',
    template: ''
})
export class RolePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolePopupService: RolePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.rolePopupService
                    .open(RoleDialogComponent, params['id']);
            } else {
                this.modalRef = this.rolePopupService
                    .open(RoleDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
