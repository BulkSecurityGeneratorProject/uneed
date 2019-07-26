/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UneedTestModule } from '../../../test.module';
import { UserReviewDeleteDialogComponent } from 'app/entities/user-review/user-review-delete-dialog.component';
import { UserReviewService } from 'app/entities/user-review/user-review.service';

describe('Component Tests', () => {
  describe('UserReview Management Delete Component', () => {
    let comp: UserReviewDeleteDialogComponent;
    let fixture: ComponentFixture<UserReviewDeleteDialogComponent>;
    let service: UserReviewService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserReviewDeleteDialogComponent]
      })
        .overrideTemplate(UserReviewDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserReviewDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserReviewService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
