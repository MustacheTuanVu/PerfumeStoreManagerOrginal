import { IBills } from 'app/shared/model/bills.model';
import { IProducts } from 'app/shared/model/products.model';

export interface IOrderDetails {
  id?: number;
  orderID?: string | null;
  billID?: string | null;
  productID?: string | null;
  quantity?: number | null;
  price?: number | null;
  bills?: IBills | null;
  products?: IProducts | null;
}

export const defaultValue: Readonly<IOrderDetails> = {};
