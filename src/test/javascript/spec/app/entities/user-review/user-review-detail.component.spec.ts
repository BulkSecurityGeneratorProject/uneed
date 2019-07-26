/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserReviewDetailComponent } from 'app/entities/user-review/user-review-detail.component';
import { UserReview } from 'app/shared/model/user-review.model';

describe('Component Tests', () => {
  describe('UserReview Management Detail Component', () => {
    let comp: UserReviewDetailComponent;
    let fixture: ComponentFixture<UserReviewDetailComponent>;
    const route = ({ data: of({ userReview: new UserReview(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserReviewDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserReviewDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserReviewDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userReview).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
