import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TimeTable } from './time-table.model';
import { TimeTableService } from './time-table.service';

@Injectable()
export class TimeTablePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private timeTableService: TimeTableService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.timeTableService.find(id).subscribe((timeTable) => {
                if (timeTable.startDate) {
                    timeTable.startDate = {
                        year: timeTable.startDate.getFullYear(),
                        month: timeTable.startDate.getMonth() + 1,
                        day: timeTable.startDate.getDate()
                    };
                }
                if (timeTable.endDate) {
                    timeTable.endDate = {
                        year: timeTable.endDate.getFullYear(),
                        month: timeTable.endDate.getMonth() + 1,
                        day: timeTable.endDate.getDate()
                    };
                }
                this.timeTableModalRef(component, timeTable);
            });
        } else {
            return this.timeTableModalRef(component, new TimeTable());
        }
    }

    timeTableModalRef(component: Component, timeTable: TimeTable): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.timeTable = timeTable;
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
