import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserReview } from 'app/shared/model/user-review.model';

type EntityResponseType = HttpResponse<IUserReview>;
type EntityArrayResponseType = HttpResponse<IUserReview[]>;

@Injectable({ providedIn: 'root' })
export class UserReviewService {
  public resourceUrl = SERVER_API_URL + 'api/user-reviews';

  constructor(protected http: HttpClient) {}

  create(userReview: IUserReview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userReview);
    return this.http
      .post<IUserReview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userReview: IUserReview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userReview);
    return this.http
      .put<IUserReview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserReview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserReview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(userReview: IUserReview): IUserReview {
    const copy: IUserReview = Object.assign({}, userReview, {
      date: userReview.date != null && userReview.date.isValid() ? userReview.date.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userReview: IUserReview) => {
        userReview.date = userReview.date != null ? moment(userReview.date) : null;
      });
    }
    return res;
  }
}
