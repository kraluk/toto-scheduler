import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Therapy } from './therapy.model';
import { TherapyService } from './therapy.service';

@Injectable()
export class TherapyPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private therapyService: TherapyService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.therapyService.find(id).subscribe((therapy) => {
                therapy.date = this.datePipe
                    .transform(therapy.date, 'yyyy-MM-ddThh:mm');
                this.therapyModalRef(component, therapy);
            });
        } else {
            return this.therapyModalRef(component, new Therapy());
        }
    }

    therapyModalRef(component: Component, therapy: Therapy): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.therapy = therapy;
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
