import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import bills, {
  BillsState
} from 'app/entities/bills/bills.reducer';
// prettier-ignore
import orderDetails, {
  OrderDetailsState
} from 'app/entities/order-details/order-details.reducer';
// prettier-ignore
import categories, {
  CategoriesState
} from 'app/entities/categories/categories.reducer';
// prettier-ignore
import customers, {
  CustomersState
} from 'app/entities/customers/customers.reducer';
// prettier-ignore
import dayWorks, {
  DayWorksState
} from 'app/entities/day-works/day-works.reducer';
// prettier-ignore
import products, {
  ProductsState
} from 'app/entities/products/products.reducer';
// prettier-ignore
import member, {
  MemberState
} from 'app/entities/member/member.reducer';
// prettier-ignore
import stores, {
  StoresState
} from 'app/entities/stores/stores.reducer';
// prettier-ignore
import roles, {
  RolesState
} from 'app/entities/roles/roles.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly bills: BillsState;
  readonly orderDetails: OrderDetailsState;
  readonly categories: CategoriesState;
  readonly customers: CustomersState;
  readonly dayWorks: DayWorksState;
  readonly products: ProductsState;
  readonly member: MemberState;
  readonly stores: StoresState;
  readonly roles: RolesState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  bills,
  orderDetails,
  categories,
  customers,
  dayWorks,
  products,
  member,
  stores,
  roles,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
