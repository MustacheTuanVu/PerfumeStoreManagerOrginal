import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bills.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBillsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BillsDetail = (props: IBillsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { billsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billsDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.bills.detail.title">Bills</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billsEntity.id}</dd>
          <dt>
            <span id="billID">
              <Translate contentKey="perfumeStoreManagerApp.bills.billID">Bill ID</Translate>
            </span>
          </dt>
          <dd>{billsEntity.billID}</dd>
          <dt>
            <span id="salesID">
              <Translate contentKey="perfumeStoreManagerApp.bills.salesID">Sales ID</Translate>
            </span>
          </dt>
          <dd>{billsEntity.salesID}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="perfumeStoreManagerApp.bills.date">Date</Translate>
            </span>
          </dt>
          <dd>{billsEntity.date ? <TextFormat value={billsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="discount">
              <Translate contentKey="perfumeStoreManagerApp.bills.discount">Discount</Translate>
            </span>
          </dt>
          <dd>{billsEntity.discount}</dd>
          <dt>
            <span id="vat">
              <Translate contentKey="perfumeStoreManagerApp.bills.vat">Vat</Translate>
            </span>
          </dt>
          <dd>{billsEntity.vat}</dd>
          <dt>
            <span id="payment">
              <Translate contentKey="perfumeStoreManagerApp.bills.payment">Payment</Translate>
            </span>
          </dt>
          <dd>{billsEntity.payment}</dd>
          <dt>
            <span id="total">
              <Translate contentKey="perfumeStoreManagerApp.bills.total">Total</Translate>
            </span>
          </dt>
          <dd>{billsEntity.total}</dd>
          <dt>
            <span id="customerID">
              <Translate contentKey="perfumeStoreManagerApp.bills.customerID">Customer ID</Translate>
            </span>
          </dt>
          <dd>{billsEntity.customerID}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="perfumeStoreManagerApp.bills.status">Status</Translate>
            </span>
          </dt>
          <dd>{billsEntity.status}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="perfumeStoreManagerApp.bills.description">Description</Translate>
            </span>
          </dt>
          <dd>{billsEntity.description}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.bills.customers">Customers</Translate>
          </dt>
          <dd>{billsEntity.customers ? billsEntity.customers.id : ''}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.bills.member">Member</Translate>
          </dt>
          <dd>{billsEntity.member ? billsEntity.member.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bills" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bills/${billsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bills }: IRootState) => ({
  billsEntity: bills.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BillsDetail);
