/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserReviewUpdateComponent } from 'app/entities/user-review/user-review-update.component';
import { UserReviewService } from 'app/entities/user-review/user-review.service';
import { UserReview } from 'app/shared/model/user-review.model';

describe('Component Tests', () => {
  describe('UserReview Management Update Component', () => {
    let comp: UserReviewUpdateComponent;
    let fixture: ComponentFixture<UserReviewUpdateComponent>;
    let service: UserReviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserReviewUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserReviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserReviewUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserReviewService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserReview(123);
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
        const entity = new UserReview();
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
