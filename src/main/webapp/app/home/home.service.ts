import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserJob } from 'app/shared/model/user-job.model';

type EntityResponseType = HttpResponse<IUserJob>;
type EntityArrayResponseType = HttpResponse<IUserJob[]>;

@Injectable({ providedIn: 'root' })
export class HomeService {
  public resourceUrl = SERVER_API_URL + 'api/user-jobs';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.lastUpdateDate = res.body.lastUpdateDate != null ? moment(res.body.lastUpdateDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userJob: IUserJob) => {
        userJob.createDate = userJob.createDate != null ? moment(userJob.createDate) : null;
        userJob.lastUpdateDate = userJob.lastUpdateDate != null ? moment(userJob.lastUpdateDate) : null;
      });
    }
    return res;
  }
}
