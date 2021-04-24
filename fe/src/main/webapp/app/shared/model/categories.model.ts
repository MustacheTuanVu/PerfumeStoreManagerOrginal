import { IProducts } from 'app/shared/model/products.model';

export interface ICategories {
  id?: number;
  categoryID?: string | null;
  categoryName?: string | null;
  products?: IProducts[] | null;
}

export const defaultValue: Readonly<ICategories> = {};
