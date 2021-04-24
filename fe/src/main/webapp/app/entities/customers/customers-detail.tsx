import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customers.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomersDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomersDetail = (props: ICustomersDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customersEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customersDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.customers.detail.title">Customers</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customersEntity.id}</dd>
          <dt>
            <span id="customerID">
              <Translate contentKey="perfumeStoreManagerApp.customers.customerID">Customer ID</Translate>
            </span>
          </dt>
          <dd>{customersEntity.customerID}</dd>
          <dt>
            <span id="customerName">
              <Translate contentKey="perfumeStoreManagerApp.customers.customerName">Customer Name</Translate>
            </span>
          </dt>
          <dd>{customersEntity.customerName}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="perfumeStoreManagerApp.customers.address">Address</Translate>
            </span>
          </dt>
          <dd>{customersEntity.address}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="perfumeStoreManagerApp.customers.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{customersEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="perfumeStoreManagerApp.customers.email">Email</Translate>
            </span>
          </dt>
          <dd>{customersEntity.email}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="perfumeStoreManagerApp.customers.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{customersEntity.gender}</dd>
        </dl>
        <Button tag={Link} to="/customers" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customers/${customersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customers }: IRootState) => ({
  customersEntity: customers.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomersDetail);
