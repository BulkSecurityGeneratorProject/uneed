import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserReview } from 'app/shared/model/user-review.model';
import { UserReviewService } from './user-review.service';

@Component({
  selector: 'jhi-user-review-delete-dialog',
  templateUrl: './user-review-delete-dialog.component.html'
})
export class UserReviewDeleteDialogComponent {
  userReview: IUserReview;

  constructor(
    protected userReviewService: UserReviewService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userReviewService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'userReviewListModification',
        content: 'Deleted an userReview'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-user-review-delete-popup',
  template: ''
})
export class UserReviewDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userReview }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UserReviewDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.userReview = userReview;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/user-review', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/user-review', { outlets: { popup: null } }]);
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
