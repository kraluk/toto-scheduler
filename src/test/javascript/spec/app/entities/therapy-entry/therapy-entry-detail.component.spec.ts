import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TotoSchedulerTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TherapyEntryDetailComponent } from '../../../../../../main/webapp/app/entities/therapy-entry/therapy-entry-detail.component';
import { TherapyEntryService } from '../../../../../../main/webapp/app/entities/therapy-entry/therapy-entry.service';
import { TherapyEntry } from '../../../../../../main/webapp/app/entities/therapy-entry/therapy-entry.model';

describe('Component Tests', () => {

    describe('TherapyEntry Management Detail Component', () => {
        let comp: TherapyEntryDetailComponent;
        let fixture: ComponentFixture<TherapyEntryDetailComponent>;
        let service: TherapyEntryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TotoSchedulerTestModule],
                declarations: [TherapyEntryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TherapyEntryService,
                    JhiEventManager
                ]
            }).overrideTemplate(TherapyEntryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TherapyEntryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TherapyEntryService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TherapyEntry(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.therapyEntry).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
