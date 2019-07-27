import { Moment } from 'moment';
import { ITag } from 'app/shared/model/tag.model';

export interface IUserJob {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  currency?: string;
  imageUrl?: string;
  createDate?: Moment;
  lastUpdateDate?: Moment;
  categoryName?: string;
  categoryId?: number;
  tags?: ITag[];
  userLogin?: string;
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
    public categoryName?: string,
    public categoryId?: number,
    public tags?: ITag[],
    public userLogin?: string,
    public userId?: number
  ) {}
}
