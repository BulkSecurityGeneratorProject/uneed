/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { UneedTestModule } from '../../../test.module';
import { UserStatUpdateComponent } from 'app/entities/user-stat/user-stat-update.component';
import { UserStatService } from 'app/entities/user-stat/user-stat.service';
import { UserStat } from 'app/shared/model/user-stat.model';

describe('Component Tests', () => {
  describe('UserStat Management Update Component', () => {
    let comp: UserStatUpdateComponent;
    let fixture: ComponentFixture<UserStatUpdateComponent>;
    let service: UserStatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UneedTestModule],
        declarations: [UserStatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserStatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserStatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserStatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserStat(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserStat();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
