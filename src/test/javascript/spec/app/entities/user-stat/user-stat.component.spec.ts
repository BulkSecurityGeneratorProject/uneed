/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UneedTestModule } from '../../../test.module';
import { UserStatComponent } from 'app/entities/user-stat/user-stat.component';
import { UserStatService } from 'app/entities/user-stat/user-stat.service';
import { UserStat } from 'app/shared/model/user-stat.model';

describe('Component Tests', () => {
  describe('UserStat Management Component', () => {
    let comp: UserStatComponent;
    let fixture: ComponentFixture<UserStatComponent>;
    let service: UserStatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserStatComponent],
        providers: []
      })
        .overrideTemplate(UserStatComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserStatComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserStatService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserStat(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userStats[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
