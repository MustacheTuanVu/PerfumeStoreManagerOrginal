import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DayWorks from './day-works';
import DayWorksDetail from './day-works-detail';
import DayWorksUpdate from './day-works-update';
import DayWorksDeleteDialog from './day-works-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DayWorksUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DayWorksUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DayWorksDetail} />
      <ErrorBoundaryRoute path={match.url} component={DayWorks} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DayWorksDeleteDialog} />
  </>
);

export default Routes;
