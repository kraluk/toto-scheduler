import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TherapyType } from './therapy-type.model';
import { TherapyTypeService } from './therapy-type.service';

@Component({
    selector: 'jhi-therapy-type-detail',
    templateUrl: './therapy-type-detail.component.html'
})
export class TherapyTypeDetailComponent implements OnInit, OnDestroy {

    therapyType: TherapyType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private therapyTypeService: TherapyTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTherapyTypes();
    }

    load(id) {
        this.therapyTypeService.find(id).subscribe((therapyType) => {
            this.therapyType = therapyType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTherapyTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'therapyTypeListModification',
            (response) => this.load(this.therapyType.id)
        );
    }
}
