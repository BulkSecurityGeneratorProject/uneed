import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserStat } from 'app/shared/model/user-stat.model';

type EntityResponseType = HttpResponse<IUserStat>;
type EntityArrayResponseType = HttpResponse<IUserStat[]>;

@Injectable({ providedIn: 'root' })
export class UserStatService {
  public resourceUrl = SERVER_API_URL + 'api/user-stats';

  constructor(protected http: HttpClient) {}

  create(userStat: IUserStat): Observable<EntityResponseType> {
    return this.http.post<IUserStat>(this.resourceUrl, userStat, { observe: 'response' });
  }

  update(userStat: IUserStat): Observable<EntityResponseType> {
    return this.http.put<IUserStat>(this.resourceUrl, userStat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserStat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserStat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
