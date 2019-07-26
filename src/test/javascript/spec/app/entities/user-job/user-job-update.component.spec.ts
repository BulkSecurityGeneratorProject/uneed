/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserJobUpdateComponent } from 'app/entities/user-job/user-job-update.component';
import { UserJobService } from 'app/entities/user-job/user-job.service';
import { UserJob } from 'app/shared/model/user-job.model';

describe('Component Tests', () => {
  describe('UserJob Management Update Component', () => {
    let comp: UserJobUpdateComponent;
    let fixture: ComponentFixture<UserJobUpdateComponent>;
    let service: UserJobService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserJobUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserJobUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserJobUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserJobService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserJob(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserJob();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
