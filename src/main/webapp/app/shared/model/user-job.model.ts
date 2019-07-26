import { Moment } from 'moment';

export interface IUserJob {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  currency?: string;
  imageUrl?: string;
  createDate?: Moment;
  lastUpdateDate?: Moment;
  userId?: number;
}

export class UserJob implements IUserJob {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public price?: number,
    public currency?: string,
    public imageUrl?: string,
    public createDate?: Moment,
    public lastUpdateDate?: Moment,
    public userId?: number
  ) {}
}
