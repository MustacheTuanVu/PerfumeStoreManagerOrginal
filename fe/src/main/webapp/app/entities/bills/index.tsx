import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Bills from './bills';
import BillsDetail from './bills-detail';
import BillsUpdate from './bills-update';
import BillsDeleteDialog from './bills-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BillsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BillsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BillsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Bills} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BillsDeleteDialog} />
  </>
);

export default Routes;
