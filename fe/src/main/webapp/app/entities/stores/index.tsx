import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Stores from './stores';
import StoresDetail from './stores-detail';
import StoresUpdate from './stores-update';
import StoresDeleteDialog from './stores-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StoresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StoresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StoresDetail} />
      <ErrorBoundaryRoute path={match.url} component={Stores} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StoresDeleteDialog} />
  </>
);

export default Routes;
