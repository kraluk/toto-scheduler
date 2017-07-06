/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TotoSchedulerTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TherapyDetailComponent } from '../../../../../../main/webapp/app/entities/therapy/therapy-detail.component';
import { TherapyService } from '../../../../../../main/webapp/app/entities/therapy/therapy.service';
import { Therapy } from '../../../../../../main/webapp/app/entities/therapy/therapy.model';

describe('Component Tests', () => {

    describe('Therapy Management Detail Component', () => {
        let comp: TherapyDetailComponent;
        let fixture: ComponentFixture<TherapyDetailComponent>;
        let service: TherapyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TotoSchedulerTestModule],
                declarations: [TherapyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TherapyService,
                    JhiEventManager
                ]
            }).overrideTemplate(TherapyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TherapyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TherapyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Therapy(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.therapy).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
