export interface IAddress {
  id?: number;
  address?: string;
  postalCode?: string;
  city?: string;
  province?: string;
  country?: string;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public address?: string,
    public postalCode?: string,
    public city?: string,
    public province?: string,
    public country?: string
  ) {}
}
