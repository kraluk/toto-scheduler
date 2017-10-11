import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Therapist } from './therapist.model';
import { TherapistService } from './therapist.service';

@Component({
    selector: 'jhi-therapist-detail',
    templateUrl: './therapist-detail.component.html'
})
export class TherapistDetailComponent implements OnInit, OnDestroy {

    therapist: Therapist;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private therapistService: TherapistService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTherapists();
    }

    load(id) {
        this.therapistService.find(id).subscribe((therapist) => {
            this.therapist = therapist;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTherapists() {
        this.eventSubscriber = this.eventManager.subscribe(
            'therapistListModification',
            (response) => this.load(this.therapist.id)
        );
    }
}
