import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Therapy } from './therapy.model';
import { TherapyService } from './therapy.service';

@Component({
    selector: 'jhi-therapy-detail',
    templateUrl: './therapy-detail.component.html'
})
export class TherapyDetailComponent implements OnInit, OnDestroy {

    therapy: Therapy;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private therapyService: TherapyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTherapies();
    }

    load(id) {
        this.therapyService.find(id).subscribe((therapy) => {
            this.therapy = therapy;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTherapies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'therapyListModification',
            (response) => this.load(this.therapy.id)
        );
    }
}
