import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-details.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderDetailsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderDetailsDetail = (props: IOrderDetailsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderDetailsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.orderDetails.detail.title">OrderDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.id}</dd>
          <dt>
            <span id="orderID">
              <Translate contentKey="perfumeStoreManagerApp.orderDetails.orderID">Order ID</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.orderID}</dd>
          <dt>
            <span id="billID">
              <Translate contentKey="perfumeStoreManagerApp.orderDetails.billID">Bill ID</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.billID}</dd>
          <dt>
            <span id="productID">
              <Translate contentKey="perfumeStoreManagerApp.orderDetails.productID">Product ID</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.productID}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="perfumeStoreManagerApp.orderDetails.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.quantity}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="perfumeStoreManagerApp.orderDetails.price">Price</Translate>
            </span>
          </dt>
          <dd>{orderDetailsEntity.price}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.orderDetails.bills">Bills</Translate>
          </dt>
          <dd>{orderDetailsEntity.bills ? orderDetailsEntity.bills.id : ''}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.orderDetails.products">Products</Translate>
          </dt>
          <dd>{orderDetailsEntity.products ? orderDetailsEntity.products.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-details/${orderDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderDetails }: IRootState) => ({
  orderDetailsEntity: orderDetails.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderDetailsDetail);
