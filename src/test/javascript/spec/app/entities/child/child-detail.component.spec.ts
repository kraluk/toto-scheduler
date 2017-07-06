import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TotoSchedulerTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChildDetailComponent } from '../../../../../../main/webapp/app/entities/child/child-detail.component';
import { ChildService } from '../../../../../../main/webapp/app/entities/child/child.service';
import { Child } from '../../../../../../main/webapp/app/entities/child/child.model';

describe('Component Tests', () => {

    describe('Child Management Detail Component', () => {
        let comp: ChildDetailComponent;
        let fixture: ComponentFixture<ChildDetailComponent>;
        let service: ChildService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TotoSchedulerTestModule],
                declarations: [ChildDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChildService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChildDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChildDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChildService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Child(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.child).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
