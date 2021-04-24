import { IMember } from 'app/shared/model/member.model';

export interface IStores {
  id?: number;
  storeID?: string | null;
  storeName?: string | null;
  storePhone?: string | null;
  storeAdress?: string | null;
  storeRent?: number | null;
  members?: IMember[] | null;
}

export const defaultValue: Readonly<IStores> = {};
