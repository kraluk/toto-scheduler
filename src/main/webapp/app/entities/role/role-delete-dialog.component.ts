import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Role } from './role.model';
import { RolePopupService } from './role-popup.service';
import { RoleService } from './role.service';

@Component({
    selector: 'jhi-role-delete-dialog',
    templateUrl: './role-delete-dialog.component.html'
})
export class RoleDeleteDialogComponent {

    role: Role;

    constructor(
        private roleService: RoleService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'roleListModification',
                content: 'Deleted an role'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('totoSchedulerApp.role.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-role-delete-popup',
    template: ''
})
export class RoleDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolePopupService: RolePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.rolePopupService
                .open(RoleDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
