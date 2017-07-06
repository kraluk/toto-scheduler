import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TherapyEntry } from './therapy-entry.model';
import { TherapyEntryService } from './therapy-entry.service';

@Component({
    selector: 'jhi-therapy-entry-detail',
    templateUrl: './therapy-entry-detail.component.html'
})
export class TherapyEntryDetailComponent implements OnInit, OnDestroy {

    therapyEntry: TherapyEntry;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private therapyEntryService: TherapyEntryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTherapyEntries();
    }

    load(id) {
        this.therapyEntryService.find(id).subscribe((therapyEntry) => {
            this.therapyEntry = therapyEntry;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTherapyEntries() {
        this.eventSubscriber = this.eventManager.subscribe(
            'therapyEntryListModification',
            (response) => this.load(this.therapyEntry.id)
        );
    }
}
