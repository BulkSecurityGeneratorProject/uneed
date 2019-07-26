/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UneedTestModule } from '../../../test.module';
import { UserStatDeleteDialogComponent } from 'app/entities/user-stat/user-stat-delete-dialog.component';
import { UserStatService } from 'app/entities/user-stat/user-stat.service';

describe('Component Tests', () => {
  describe('UserStat Management Delete Component', () => {
    let comp: UserStatDeleteDialogComponent;
    let fixture: ComponentFixture<UserStatDeleteDialogComponent>;
    let service: UserStatService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserStatDeleteDialogComponent]
      })
        .overrideTemplate(UserStatDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserStatDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserStatService);
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
