import { IBills } from 'app/shared/model/bills.model';
import { IDayWorks } from 'app/shared/model/day-works.model';
import { IStores } from 'app/shared/model/stores.model';
import { IRoles } from 'app/shared/model/roles.model';

export interface IMember {
  id?: number;
  userID?: string | null;
  name?: string | null;
  phone?: string | null;
  email?: string | null;
  roleID?: string | null;
  storeID?: string | null;
  salary?: number | null;
  bills?: IBills[] | null;
  dayWorks?: IDayWorks[] | null;
  stores?: IStores | null;
  roles?: IRoles | null;
}

export const defaultValue: Readonly<IMember> = {};
