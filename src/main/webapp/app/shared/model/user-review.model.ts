import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IUserReview {
  id?: number;
  score?: number;
  comment?: string;
  date?: Moment;
  reviewer?: string;
  user?: IUser;
}

export class UserReview implements IUserReview {
  constructor(
    public id?: number,
    public score?: number,
    public comment?: string,
    public date?: Moment,
    public reviewer?: string,
    public user?: IUser
  ) {}
}
