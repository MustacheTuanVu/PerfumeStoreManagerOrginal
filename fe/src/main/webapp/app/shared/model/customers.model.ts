import { IBills } from 'app/shared/model/bills.model';

export interface ICustomers {
  id?: number;
  customerID?: string | null;
  customerName?: string | null;
  address?: string | null;
  phone?: string | null;
  email?: string | null;
  gender?: number | null;
  bills?: IBills[] | null;
}

export const defaultValue: Readonly<ICustomers> = {};
