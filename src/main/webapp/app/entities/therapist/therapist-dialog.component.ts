import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Therapist } from './therapist.model';
import { TherapistPopupService } from './therapist-popup.service';
import { TherapistService } from './therapist.service';
import { User, UserService } from '../../shared';
import { Role, RoleService } from '../role';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-therapist-dialog',
    templateUrl: './therapist-dialog.component.html'
})
export class TherapistDialogComponent implements OnInit {

    therapist: Therapist;
    isSaving: boolean;

    users: User[];

    roles: Role[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private therapistService: TherapistService,
        private userService: UserService,
        private roleService: RoleService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.roleService.query()
            .subscribe((res: ResponseWrapper) => { this.roles = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.therapist.id !== undefined) {
            this.subscribeToSaveResponse(
                this.therapistService.update(this.therapist));
        } else {
            this.subscribeToSaveResponse(
                this.therapistService.create(this.therapist));
        }
    }

    private subscribeToSaveResponse(result: Observable<Therapist>) {
        result.subscribe((res: Therapist) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Therapist) {
        this.eventManager.broadcast({ name: 'therapistListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackRoleById(index: number, item: Role) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-therapist-popup',
    template: ''
})
export class TherapistPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private therapistPopupService: TherapistPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.therapistPopupService
                    .open(TherapistDialogComponent as Component, params['id']);
            } else {
                this.therapistPopupService
                    .open(TherapistDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
