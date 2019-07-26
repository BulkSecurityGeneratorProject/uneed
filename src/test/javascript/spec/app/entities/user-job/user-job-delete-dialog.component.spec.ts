/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UneedTestModule } from '../../../test.module';
import { UserJobDeleteDialogComponent } from 'app/entities/user-job/user-job-delete-dialog.component';
import { UserJobService } from 'app/entities/user-job/user-job.service';

describe('Component Tests', () => {
  describe('UserJob Management Delete Component', () => {
    let comp: UserJobDeleteDialogComponent;
    let fixture: ComponentFixture<UserJobDeleteDialogComponent>;
    let service: UserJobService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserJobDeleteDialogComponent]
      })
        .overrideTemplate(UserJobDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserJobDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserJobService);
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
