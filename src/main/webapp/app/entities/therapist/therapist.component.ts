import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Therapist } from './therapist.model';
import { TherapistService } from './therapist.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-therapist',
    templateUrl: './therapist.component.html'
})
export class TherapistComponent implements OnInit, OnDestroy {
therapists: Therapist[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private therapistService: TherapistService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.therapistService.query().subscribe(
            (res: ResponseWrapper) => {
                this.therapists = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTherapists();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Therapist) {
        return item.id;
    }
    registerChangeInTherapists() {
        this.eventSubscriber = this.eventManager.subscribe('therapistListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
