/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TotoSchedulerTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TherapistDetailComponent } from '../../../../../../main/webapp/app/entities/therapist/therapist-detail.component';
import { TherapistService } from '../../../../../../main/webapp/app/entities/therapist/therapist.service';
import { Therapist } from '../../../../../../main/webapp/app/entities/therapist/therapist.model';

describe('Component Tests', () => {

    describe('Therapist Management Detail Component', () => {
        let comp: TherapistDetailComponent;
        let fixture: ComponentFixture<TherapistDetailComponent>;
        let service: TherapistService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TotoSchedulerTestModule],
                declarations: [TherapistDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TherapistService,
                    JhiEventManager
                ]
            }).overrideTemplate(TherapistDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TherapistDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TherapistService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Therapist(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.therapist).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
