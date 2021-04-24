import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBills } from 'app/shared/model/bills.model';
import { getEntities as getBills } from 'app/entities/bills/bills.reducer';
import { IProducts } from 'app/shared/model/products.model';
import { getEntities as getProducts } from 'app/entities/products/products.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order-details.reducer';
import { IOrderDetails } from 'app/shared/model/order-details.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderDetailsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderDetailsUpdate = (props: IOrderDetailsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { orderDetailsEntity, bills, products, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/order-details' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBills();
    props.getProducts();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...orderDetailsEntity,
        ...values,
        bills: bills.find(it => it.id.toString() === values.billsId.toString()),
        products: products.find(it => it.id.toString() === values.productsId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="perfumeStoreManagerApp.orderDetails.home.createOrEditLabel" data-cy="OrderDetailsCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.orderDetails.home.createOrEditLabel">Create or edit a OrderDetails</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : orderDetailsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="order-details-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="order-details-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="orderIDLabel" for="order-details-orderID">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.orderID">Order ID</Translate>
                </Label>
                <AvField id="order-details-orderID" data-cy="orderID" type="text" name="orderID" />
              </AvGroup>
              <AvGroup>
                <Label id="billIDLabel" for="order-details-billID">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.billID">Bill ID</Translate>
                </Label>
                <AvField id="order-details-billID" data-cy="billID" type="text" name="billID" />
              </AvGroup>
              <AvGroup>
                <Label id="productIDLabel" for="order-details-productID">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.productID">Product ID</Translate>
                </Label>
                <AvField id="order-details-productID" data-cy="productID" type="text" name="productID" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="order-details-quantity">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.quantity">Quantity</Translate>
                </Label>
                <AvField id="order-details-quantity" data-cy="quantity" type="string" className="form-control" name="quantity" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="order-details-price">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.price">Price</Translate>
                </Label>
                <AvField id="order-details-price" data-cy="price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label for="order-details-bills">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.bills">Bills</Translate>
                </Label>
                <AvInput id="order-details-bills" data-cy="bills" type="select" className="form-control" name="billsId">
                  <option value="" key="0" />
                  {bills
                    ? bills.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-details-products">
                  <Translate contentKey="perfumeStoreManagerApp.orderDetails.products">Products</Translate>
                </Label>
                <AvInput id="order-details-products" data-cy="products" type="select" className="form-control" name="productsId">
                  <option value="" key="0" />
                  {products
                    ? products.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/order-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  bills: storeState.bills.entities,
  products: storeState.products.entities,
  orderDetailsEntity: storeState.orderDetails.entity,
  loading: storeState.orderDetails.loading,
  updating: storeState.orderDetails.updating,
  updateSuccess: storeState.orderDetails.updateSuccess,
});

const mapDispatchToProps = {
  getBills,
  getProducts,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderDetailsUpdate);
