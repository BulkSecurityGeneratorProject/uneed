import { IUser } from 'app/core/user/user.model';

export interface IUserStat {
  id?: number;
  viewCount?: number;
  reviewCount?: number;
  rating?: number;
  user?: IUser;
}

export class UserStat implements IUserStat {
  constructor(public id?: number, public viewCount?: number, public reviewCount?: number, public rating?: number, public user?: IUser) {}
}
