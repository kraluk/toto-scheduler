import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { TherapyEntry } from './therapy-entry.model';
import { TherapyEntryService } from './therapy-entry.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-therapy-entry',
    templateUrl: './therapy-entry.component.html'
})
export class TherapyEntryComponent implements OnInit, OnDestroy {
therapyEntries: TherapyEntry[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private therapyEntryService: TherapyEntryService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.therapyEntryService.query().subscribe(
            (res: ResponseWrapper) => {
                this.therapyEntries = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTherapyEntries();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TherapyEntry) {
        return item.id;
    }
    registerChangeInTherapyEntries() {
        this.eventSubscriber = this.eventManager.subscribe('therapyEntryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
