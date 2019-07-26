import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export const enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export interface IUserInfo {
  id?: number;
  phone?: string;
  mobilePhone?: string;
  emailFlag?: boolean;
  smsFlag?: boolean;
  birthDate?: Moment;
  gender?: Gender;
  user?: IUser;
}

export class UserInfo implements IUserInfo {
  constructor(
    public id?: number,
    public phone?: string,
    public mobilePhone?: string,
    public emailFlag?: boolean,
    public smsFlag?: boolean,
    public birthDate?: Moment,
    public gender?: Gender,
    public user?: IUser
  ) {
    this.emailFlag = this.emailFlag || false;
    this.smsFlag = this.smsFlag || false;
  }
}
