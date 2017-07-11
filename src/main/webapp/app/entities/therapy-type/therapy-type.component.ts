import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { TherapyType } from './therapy-type.model';
import { TherapyTypeService } from './therapy-type.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-therapy-type',
    templateUrl: './therapy-type.component.html'
})
export class TherapyTypeComponent implements OnInit, OnDestroy {
therapyTypes: TherapyType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private therapyTypeService: TherapyTypeService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.therapyTypeService.query().subscribe(
            (res: ResponseWrapper) => {
                this.therapyTypes = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTherapyTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TherapyType) {
        return item.id;
    }
    registerChangeInTherapyTypes() {
        this.eventSubscriber = this.eventManager.subscribe('therapyTypeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
