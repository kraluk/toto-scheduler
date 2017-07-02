import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Period } from './period.model';
import { PeriodService } from './period.service';

@Injectable()
export class PeriodPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private periodService: PeriodService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.periodService.find(id).subscribe((period) => {
                if (period.startDate) {
                    period.startDate = {
                        year: period.startDate.getFullYear(),
                        month: period.startDate.getMonth() + 1,
                        day: period.startDate.getDate()
                    };
                }
                if (period.endDate) {
                    period.endDate = {
                        year: period.endDate.getFullYear(),
                        month: period.endDate.getMonth() + 1,
                        day: period.endDate.getDate()
                    };
                }
                this.periodModalRef(component, period);
            });
        } else {
            return this.periodModalRef(component, new Period());
        }
    }

    periodModalRef(component: Component, period: Period): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.period = period;
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
