import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserJob } from 'app/shared/model/user-job.model';
import { UserJobService } from './user-job.service';

@Component({
  selector: 'jhi-user-job-delete-dialog',
  templateUrl: './user-job-delete-dialog.component.html'
})
export class UserJobDeleteDialogComponent {
  userJob: IUserJob;

  constructor(protected userJobService: UserJobService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userJobService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'userJobListModification',
        content: 'Deleted an userJob'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-user-job-delete-popup',
  template: ''
})
export class UserJobDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userJob }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UserJobDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.userJob = userJob;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/user-job', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/user-job', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
