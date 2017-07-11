import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TherapyType } from './therapy-type.model';
import { TherapyTypeService } from './therapy-type.service';

@Injectable()
export class TherapyTypePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private therapyTypeService: TherapyTypeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.therapyTypeService.find(id).subscribe((therapyType) => {
                this.therapyTypeModalRef(component, therapyType);
            });
        } else {
            return this.therapyTypeModalRef(component, new TherapyType());
        }
    }

    therapyTypeModalRef(component: Component, therapyType: TherapyType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.therapyType = therapyType;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
