import dayjs from 'dayjs';
import { IOrderDetails } from 'app/shared/model/order-details.model';
import { ICategories } from 'app/shared/model/categories.model';

export interface IProducts {
  id?: number;
  productID?: string | null;
  productName?: string | null;
  quantityAvailable?: number | null;
  price?: number | null;
  dateImport?: string | null;
  expireDate?: string | null;
  description?: string | null;
  categoryID?: string | null;
  volume?: number | null;
  orderDetails?: IOrderDetails[] | null;
  categories?: ICategories | null;
}

export const defaultValue: Readonly<IProducts> = {};
