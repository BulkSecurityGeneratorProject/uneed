import { IUserJob } from 'app/shared/model/user-job.model';

export interface ITag {
  id?: number;
  name?: string;
  userJob?: IUserJob;
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public userJob?: IUserJob) {}
}
