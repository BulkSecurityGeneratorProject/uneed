import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserInfo } from 'app/shared/model/user-info.model';

type EntityResponseType = HttpResponse<IUserInfo>;
type EntityArrayResponseType = HttpResponse<IUserInfo[]>;

@Injectable({ providedIn: 'root' })
export class AccInfoService {
  public resourceUrl = SERVER_API_URL + 'api/user-infos';

  constructor(protected http: HttpClient) {}

  create(userInfo: IUserInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userInfo);
    return this.http
      .post<IUserInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userInfo: IUserInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userInfo);
    return this.http
      .put<IUserInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  current(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IUserInfo[]>(this.resourceUrl, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(userInfo: IUserInfo): IUserInfo {
    const copy: IUserInfo = Object.assign({}, userInfo, {
      birthDate: userInfo.birthDate != null && userInfo.birthDate.isValid() ? userInfo.birthDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate != null ? moment(res.body.birthDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userInfo: IUserInfo) => {
        userInfo.birthDate = userInfo.birthDate != null ? moment(userInfo.birthDate) : null;
      });
    }
    return res;
  }
}
