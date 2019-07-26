/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserJobDetailComponent } from 'app/entities/user-job/user-job-detail.component';
import { UserJob } from 'app/shared/model/user-job.model';

describe('Component Tests', () => {
  describe('UserJob Management Detail Component', () => {
    let comp: UserJobDetailComponent;
    let fixture: ComponentFixture<UserJobDetailComponent>;
    const route = ({ data: of({ userJob: new UserJob(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserJobDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserJobDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserJobDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userJob).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
