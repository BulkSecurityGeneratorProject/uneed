/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserStatDetailComponent } from 'app/entities/user-stat/user-stat-detail.component';
import { UserStat } from 'app/shared/model/user-stat.model';

describe('Component Tests', () => {
  describe('UserStat Management Detail Component', () => {
    let comp: UserStatDetailComponent;
    let fixture: ComponentFixture<UserStatDetailComponent>;
    const route = ({ data: of({ userStat: new UserStat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserStatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserStatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserStatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userStat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
