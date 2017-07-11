/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TotoSchedulerTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TherapyTypeDetailComponent } from '../../../../../../main/webapp/app/entities/therapy-type/therapy-type-detail.component';
import { TherapyTypeService } from '../../../../../../main/webapp/app/entities/therapy-type/therapy-type.service';
import { TherapyType } from '../../../../../../main/webapp/app/entities/therapy-type/therapy-type.model';

describe('Component Tests', () => {

    describe('TherapyType Management Detail Component', () => {
        let comp: TherapyTypeDetailComponent;
        let fixture: ComponentFixture<TherapyTypeDetailComponent>;
        let service: TherapyTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TotoSchedulerTestModule],
                declarations: [TherapyTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TherapyTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(TherapyTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TherapyTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TherapyTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TherapyType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.therapyType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
