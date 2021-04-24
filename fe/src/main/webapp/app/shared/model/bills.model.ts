import dayjs from 'dayjs';
import { IOrderDetails } from 'app/shared/model/order-details.model';
import { ICustomers } from 'app/shared/model/customers.model';
import { IMember } from 'app/shared/model/member.model';

export interface IBills {
  id?: number;
  billID?: string | null;
  salesID?: string | null;
  date?: string | null;
  discount?: number | null;
  vat?: number | null;
  payment?: number | null;
  total?: number | null;
  customerID?: string | null;
  status?: number | null;
  description?: string | null;
  orderDetails?: IOrderDetails[] | null;
  customers?: ICustomers | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<IBills> = {};
