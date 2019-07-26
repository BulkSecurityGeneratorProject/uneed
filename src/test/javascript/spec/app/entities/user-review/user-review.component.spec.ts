/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UneedTestModule } from '../../../test.module';
import { UserReviewComponent } from 'app/entities/user-review/user-review.component';
import { UserReviewService } from 'app/entities/user-review/user-review.service';
import { UserReview } from 'app/shared/model/user-review.model';

describe('Component Tests', () => {
  describe('UserReview Management Component', () => {
    let comp: UserReviewComponent;
    let fixture: ComponentFixture<UserReviewComponent>;
    let service: UserReviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserReviewComponent],
        providers: []
      })
        .overrideTemplate(UserReviewComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserReviewComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserReviewService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserReview(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userReviews[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
