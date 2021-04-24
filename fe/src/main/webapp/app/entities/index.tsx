import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Bills from './bills';
import OrderDetails from './order-details';
import Categories from './categories';
import Customers from './customers';
import DayWorks from './day-works';
import Products from './products';
import Member from './member';
import Stores from './stores';
import Roles from './roles';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}bills`} component={Bills} />
      <ErrorBoundaryRoute path={`${match.url}order-details`} component={OrderDetails} />
      <ErrorBoundaryRoute path={`${match.url}categories`} component={Categories} />
      <ErrorBoundaryRoute path={`${match.url}customers`} component={Customers} />
      <ErrorBoundaryRoute path={`${match.url}day-works`} component={DayWorks} />
      <ErrorBoundaryRoute path={`${match.url}products`} component={Products} />
      <ErrorBoundaryRoute path={`${match.url}member`} component={Member} />
      <ErrorBoundaryRoute path={`${match.url}stores`} component={Stores} />
      <ErrorBoundaryRoute path={`${match.url}roles`} component={Roles} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
