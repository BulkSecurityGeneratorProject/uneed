import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserStat } from 'app/shared/model/user-stat.model';
import { UserStatService } from './user-stat.service';

@Component({
  selector: 'jhi-user-stat-delete-dialog',
  templateUrl: './user-stat-delete-dialog.component.html'
})
export class UserStatDeleteDialogComponent {
  userStat: IUserStat;

  constructor(protected userStatService: UserStatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userStatService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'userStatListModification',
        content: 'Deleted an userStat'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-user-stat-delete-popup',
  template: ''
})
export class UserStatDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userStat }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UserStatDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.userStat = userStat;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/user-stat', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/user-stat', { outlets: { popup: null } }]);
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
