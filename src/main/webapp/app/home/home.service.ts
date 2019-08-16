import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserJob } from 'app/shared/model/user-job.model';

type EntityResponseType = HttpResponse<IUserJob>;
type EntityArrayResponseType = HttpResponse<IUserJob[]>;

@Injectable({ providedIn: 'root' })
export class HomeService {
  public resourceUrl = SERVER_API_URL + 'api/pub/user-jobs';
  onQuote: BehaviorSubject<any>;

  constructor(protected http: HttpClient) {
    this.onQuote = new BehaviorSubject(null);
    this.getQuote();
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  getQuote() {
    const quote = localStorage.getItem('quote');
    if (quote) {
      console.log('Quote from local...');
      this.onQuote.next(JSON.parse(quote));
    } else {
      console.log('Fetch quote...');
      this.fetchQuote();
    }
  }

  fetchQuote() {
    const uri = 'https://quotes.rest/qod.json';
    this.http.get(uri).subscribe(res => {
      if (res && res['success']['total'] == 1) {
        const quote = res['contents']['quotes'][0];
        localStorage.setItem('quote', JSON.stringify(quote));
        this.onQuote.next(quote);
      }
    });
  }
}
