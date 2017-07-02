import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Child } from './child.model';
import { ChildService } from './child.service';

@Component({
    selector: 'jhi-child-detail',
    templateUrl: './child-detail.component.html'
})
export class ChildDetailComponent implements OnInit, OnDestroy {

    child: Child;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private childService: ChildService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChildren();
    }

    load(id) {
        this.childService.find(id).subscribe((child) => {
            this.child = child;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChildren() {
        this.eventSubscriber = this.eventManager.subscribe(
            'childListModification',
            (response) => this.load(this.child.id)
        );
    }
}
